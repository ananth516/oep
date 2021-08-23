package com.oracle.oep.order.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.oracle.oep.brm.opcode.root.Opcode;
import com.oracle.oep.brm.opcodes.CUSFLDADDONPKG;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.PARAMS;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.brmorder.model.PreCreateservice;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmMapping;
import com.oracle.oep.order.model.AddActionPlanType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.presistence.model.BulkBatchMaster;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.CreateOrder;

public class ProcessAddOrderFulfillmentProcessor {
	
	
	public static final Logger log = LogManager.getLogger(ProcessAddOrderFulfillmentProcessor.class); 	
	
	OepOrder oepOrderDO = null;
	ProducerTemplate template = null;
	/* 
	 * 
	 * This method processes the add order for all services including Add Mobile, BB, Short code and Bulk SMS
	 * 
	 */
	public void processAddOrder(final OrderEntryRequest request,final Exchange exchange, final String oepOrderAction, final String orderPayload, final String oepOrderNumber, final String oepServiceType) throws Exception {
		
		template = exchange.getContext().createProducerTemplate();
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing");
		
		try{
			
			List<AddActionPlanType> orderedPlans = null;
			List<AddActionPlanType> addOnPlans=null;
			AddActionPlanType basePlan = null;
			Map<String,OEPConfigPlanAttributesT> productSpecMap = new HashMap<String,OEPConfigPlanAttributesT>();
			CUSOPSEARCHOutputFlist searchResult=null;
			RESULTS result = null;
			String portType = ProcessAddOrderEbmHelper.getPortType(request, oepOrderAction);
			String channelName = ProcessAddOrderEbmHelper.getChannelName(request, oepOrderAction);

			log.info("OEP order number : "+oepOrderNumber+". Before Fetching plans from order");
			
			orderedPlans = ProcessAddOrderEbmHelper.getPlansFromOrder(request,oepOrderAction);
			
			log.info("OEP order number : "+oepOrderNumber+". Fetched plans from order");
			 
			for(AddActionPlanType plan : orderedPlans){
				
				String planType=null;
				
				//queryplan and initialize base plan and addOnPlan
				
				
				OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(plan.getPlanName(), template,oepOrderNumber);
				
				if(planAttrbiutes == null){
					
					throw new RuntimeException(plan.getPlanName()+" plan not found");
				}
				
				else if(planAttrbiutes.getPs()==null){
					
					throw new RuntimeException("PS is not found for "+plan.getPlanName());
				}
				
				planType = planAttrbiutes.getPackageType();
				log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+planType+", product spec is "+planAttrbiutes.getPs()+", and COS is "+planAttrbiutes.getCos());
				
				if(planType.equalsIgnoreCase(OEPConstants.OEP_BASE_PLAN_TYPE)) {
					
					basePlan = plan;
					
				}
				else if(planType.equals(OEPConstants.OEP_ADDON_PLAN_TYPE)){				
					if(addOnPlans==null){
						
						addOnPlans = new ArrayList<AddActionPlanType>();
						
					}
					
					addOnPlans.add(plan);	
					
				}			
				productSpecMap.put(plan.getPlanName(),planAttrbiutes);
				
			}			
			
			// If action is Prepaid to Postpaid or Postpaid to Prepaid. Remove the existing addon's of the service
			
			if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
					|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION) ||
							oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
							|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)){
				
				//check any  AddOn's existed in the service
				
				
				
				String serviceIdentifier = ProcessAddOrderEbmHelper.getServiceNo(request, oepOrderAction);
				
		
				String userName = "CSR";
				log.info("Get Addon's from existing service ,MSISDN--"+serviceIdentifier+"-- Plan Name--"+basePlan.getPlanName());
				
				//Query BRM for Account details
				searchResult = getAccountDataFromBRMForMSISDN(template, serviceIdentifier, channelName, userName, oepOrderNumber);	
				
				result = getActiveServiceFromSearchResult(searchResult);
				
				if(result != null){
					
								
				
				log.info("Remove exiting service  Addon's called");
				
				if(result != null && result.getCUSFLDADDONPKG() != null && result.getCUSFLDADDONPKG().size() > 0){
					
					if(addOnPlans==null){
						
						addOnPlans = new ArrayList<AddActionPlanType>();
						
					}
					
					Iterator<CUSFLDADDONPKG> incompatibleAddonIterator= result.getCUSFLDADDONPKG().iterator();
					
					while (incompatibleAddonIterator.hasNext()) {
						CUSFLDADDONPKG incompatibleAddon=(CUSFLDADDONPKG)incompatibleAddonIterator.next();
						
						log.info("Incompatible Plan Name"+incompatibleAddon.getCUSFLDADDONPLANNAME());
						log.info("Incompatible Package Id"+incompatibleAddon.getPACKAGEID());
						
						AddActionPlanType removeaddonPlan=new AddActionPlanType();
						removeaddonPlan.setPlanName(incompatibleAddon.getCUSFLDADDONPLANNAME());
						removeaddonPlan.setPlanAction(OEPConstants.OSM_REMOVE_ACTION_CODE);
						OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(incompatibleAddon.getCUSFLDADDONPLANNAME(), template,oepOrderNumber);
						productSpecMap.put(incompatibleAddon.getCUSFLDADDONPLANNAME(),planAttrbiutes);
						addOnPlans.add(removeaddonPlan);
					}
					
					//log.info("Incompatible Addon Name"+incompatibleAddonResult.getCUSFLDADDONPKG().get);
					
				}// end of if result !=null
				
				}// end of if searchResult !=null
			}
			
			//generate Order and OrderLine DataObject's		
			oepOrderDO = getOepOrderDO(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, addOnPlans, productSpecMap, oepServiceType, channelName);
			oepOrderDO.setBaAccountNo(ProcessAddOrderEbmHelper.getBillingAccountId(request, oepOrderAction));
			
			ProcessAddOrderEbmMapping addOrderEbmMapping = new ProcessAddOrderEbmMapping();
			
			log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
			
			BulkBatchMaster master= new BulkBatchMaster();
			master.setBatchId(OEPConstants.OEP_BATCH_ID_DUMMY_VALUE);
			
			oepOrderDO.setBulkBatchMaster(master);		
			
			CreateOrder salesOrderEbm  = null;
			
			if(portType == null || !portType.equals(OEPConstants.OEP_PORTIN))
			{
			
				salesOrderEbm = addOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap,result,template);						

			}
			
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			
			//Save Order Entiry to DB			
			saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
			
			log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
			
			//send sales order to OSM queue			
			
			if(salesOrderEbm != null)
			{
				
				sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber);			
				log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
				
			}
			else
			{
				
				
				log.info("OEP Order Number : "+oepOrderNumber+".Order is Parked in database, request is not sent to OSM queue");
				
				Map<String,Object> eventHeaders = new HashMap<String, Object>();
				eventHeaders.put("JMSCorrelationID", OEPConstants.PRE_CREATE_SERVICE_EVENT);
				eventHeaders.put("CHANNEL_NAME", OEPConstants.NOTIF_CHANNEL_NAME);
				
				PreCreateservice preCreateServiceEvent =  new PreCreateservice();
				preCreateServiceEvent.setAccountNo(ProcessAddOrderEbmHelper.getBillingAccountId(request, oepOrderAction));
				preCreateServiceEvent.setServiceNo(Long.parseLong(ProcessAddOrderEbmHelper.getServiceNo(request, oepOrderAction)));
				preCreateServiceEvent.setOrderId(oepOrderNumber);
				preCreateServiceEvent.setEventDescr(OEPConstants.PRE_CREATE_SERVICE_DESC);
				preCreateServiceEvent.setReason(OEPConstants.PRE_CREATE_SERVICE_REASON);
				preCreateServiceEvent.setChannel(OEPConstants.NOTIF_CHANNEL_NAME);
				
				template.sendBodyAndHeaders("weblogicJMS:OEP_EVENTS_NOTIFICATTION_QUEUE", template.requestBody("direct:convertToString", preCreateServiceEvent),eventHeaders);
				
				log.info("OEP Order Number : "+oepOrderNumber+" .PreServiceCreate event is sent");
				
			}		
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			errorMessage = errorMessage!=null?(errorMessage.length()>200?errorMessage.substring(0, 200):errorMessage) : null;
			log.error("OEP Order Number : "+oepOrderNumber+". Exception caught while processing the order.. "+errorMessage);
			//log.info(e);
			
			GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
			oepOrderDO = entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);
			
			oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_FAILED);
			oepOrderDO.setStatusDescription(e.getMessage());
						
			saveOepOrderDO(oepOrderDO, template, oepOrderNumber);
	
			throw e;
		}
		
	}
	
	/* 
	 * 
	 * This method generate OepOrder entity bean and OepOrderLine entity bean associate OepOrderLine with OepOrder
	 * 
	 */
	private OepOrder getOepOrderDO(final OrderEntryRequest request, final String oepOrderAction, final String oepOrderNumber, final String orderPayload,
			final AddActionPlanType basePlan, List<AddActionPlanType> addOnPlans, final Map<String,OEPConfigPlanAttributesT> productSpecMap, final String oepServiceType, final String channelName ){
		
		log.info("OEP Order Number : "+oepOrderNumber+".Started Initializing Oep Order Entity Bean .Order payload is "+orderPayload);	
		
		
		GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
		
		String csrId = ProcessAddOrderEbmHelper.getCsrId(request, oepOrderAction);
		Timestamp orderStartDate = ProcessAddOrderEbmHelper.getOrderStartDate(request, oepOrderAction);
//		String orderActionFromRequest = ProcessAddOrderEbmHelper.getOrderActionFromRequest(request, oepOrderAction);
		String accountId= ProcessAddOrderEbmHelper.getAccountId(request, oepOrderAction);
		String serviceNo = ProcessAddOrderEbmHelper.getServiceNo(request, oepOrderAction);
		String portType = ProcessAddOrderEbmHelper.getPortType(request, oepOrderAction);
		
		log.info("OEP Order Number : "+oepOrderNumber+". Before calling entityBeanGenerator.getOepOrderDO()");
		
		OepOrder oepOrderDO= entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);	
			
		entityBeanGenerator.getOepOrderDO(oepOrderDO, serviceNo, csrId,  orderStartDate, oepOrderAction, oepOrderNumber, orderPayload, null, channelName);	
		
		
		List<OepOrderLine> oepOrderLines = new ArrayList<OepOrderLine>();		
		
		log.info("OEP Order Number : "+oepOrderNumber+".Adding base plan,"+basePlan.getPlanName()+" to  Order Entity Bean ");
		
		OepOrderLine basePlanOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, basePlan.getPlanName(), 
				productSpecMap.get(basePlan.getPlanName()).getPs(), basePlan.getPlanAction(), accountId, oepServiceType, null, oepOrderNumber);
		
		basePlanOrderLine.setOepOrder(oepOrderDO);
		oepOrderLines.add(basePlanOrderLine);
		
		if(addOnPlans != null){
			
			for (AddActionPlanType addOnPlan : addOnPlans){
				
				log.info("OEP Order Number : "+oepOrderNumber+".Adding base plan,"+addOnPlan.getPlanName()+" to  Order Entity Bean ");
				
				OepOrderLine addOnOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, addOnPlan.getPlanName(),
						productSpecMap.get(addOnPlan.getPlanName()).getPs(), addOnPlan.getPlanAction(), accountId, oepServiceType,  basePlanOrderLine.getOrderLineId(), oepOrderNumber);
				
				addOnOrderLine.setOepOrder(oepOrderDO);
				
				oepOrderLines.add(addOnOrderLine);
				
			}
		}
		
		oepOrderDO.setOepOrderLines(oepOrderLines);

		
		if(portType != null && portType.equals(OEPConstants.OEP_PORTIN))
		{
			oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_Y);
			oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_Y);
					
		}
		else {
			
			oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_N);
			oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_N);
		}
		
		
		log.info("OEP Order Number : "+oepOrderNumber+".Oep Order Entity Bean is set");
		
		return oepOrderDO;
		
	}
	
	/* 
	 * 
	 * This is the helper method to query BRM for config plan attributes for given system plan name. 
	 * 
	 */
	private OEPConfigPlanAttributesT queryBRMForPlanAttributes(final String planName,final ProducerTemplate template, final String oepOrderNumber){
		
		log.info("OEP Order Number : "+oepOrderNumber+".Querying plan attributes from BRM DB for "+planName);
		
		Map<String, String> planMap = new HashMap<String, String>();
		
		planMap.put("param", planName);
		
		Object object =  template.requestBody("direct:queryPlanFromBRM", planMap);
		
		log.info("Query Plan Attributes Object type.........{}",object.getClass());
		
		List<OEPConfigPlanAttributesT> planAttributesList = (List<OEPConfigPlanAttributesT>) object;
		
		Iterator<OEPConfigPlanAttributesT> iterator = planAttributesList.listIterator();
		
		OEPConfigPlanAttributesT planAttributeObject = null;
		
		while(iterator.hasNext()){
			
			planAttributeObject = iterator.next();
			
		}
		
		log.info("OEP Order Number : "+oepOrderNumber+". Returning for Querying plan attributes");
		return planAttributeObject;
		
	}	
	
	/* 
	 * 
	 * This is the helper method to persist OepOrder enity bean to OEP DB
	 * 
	 */
	
	private void saveOepOrderDO(final OepOrder oepOrder,final ProducerTemplate template, final String oepOrderNumber){
		
		template.sendBody("direct:saveOrderToRespository", oepOrder);		
		
		
	}	
	
	
	/* 
	 * 
	 * This is the helper method to send process order ebm to OSM sales order queue
	 * 
	 */
	private void sendSalesOrderEBM(final CreateOrder salesOrder,final ProducerTemplate template, final String oepOrderNumber) throws SOAPException{
		
		
		Document soapBodyDoc = (Document) template.requestBody("direct:convertToDocument", salesOrder);	
		SOAPMessage soapMessage = OrderDataUtils.createSOAPRequest(soapBodyDoc, oepOrderNumber, template);
		
		Map<String, Object> orderHeaders = new HashMap<String,Object>();
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_URI, OEPConstants.OSM_WEBSERVICE_URI);
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_WLS_CONTENT_TYPE, OEPConstants.OSM_WEBSERVICE_CONTETNT_TYPE);
		orderHeaders.put("JMSReplyTo", "queue:oracle/communications/ordermanagement/oepWSResponseQueue");
		
		template.sendBodyAndHeaders("direct:sendSalesOrderEBMToOSM", soapMessage.getSOAPPart().getEnvelope(),orderHeaders);
		
		
	}
	
	private CUSOPSEARCHOutputFlist getAccountDataFromBRMForMSISDN(final ProducerTemplate template, 
				final String msisdn, final String channelName, final String userName, final String oepOrderNumber){
			
			
			CUSOPSEARCHInputFlist opcodeSearch = new CUSOPSEARCHInputFlist();
			PARAMS param = new PARAMS();
			opcodeSearch.setPOID(OEPConstants.BRM_POID);
			opcodeSearch.setPROGRAMNAME(channelName);
			opcodeSearch.setUSERNAME(userName);
			opcodeSearch.setFLAGS(new BigInteger("2"));
			opcodeSearch.setPARAMNAME("MSISDN");
			param.setVALUE(msisdn);
			param.setElem(new BigInteger("0"));		
			opcodeSearch.getPARAMS().add(param);
			
			String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);

			return queryBRMForAccountData(template, inputXML, oepOrderNumber);
			
		}
	 
	 
	 private CUSOPSEARCHOutputFlist queryBRMForAccountData(final ProducerTemplate template, final String inputXML, final String oepOrderNumber){
			
			CUSOPSEARCHOutputFlist searchResult = null;
			
			Opcode opcode = new Opcode();
			opcode.setOpcodeName(OEPConstants.BRM_CUS_SEARCH_OPCODE);
			opcode.setInputXml(inputXML);		
			
			try{
			
			 log.info("OEP order number : "+oepOrderNumber+". Calling BRM webservice to fetch Account data. Input xml : \n"+inputXML);
			 
			 Exchange syncExchange = template.request("direct:getAccountAndPlanDetailsFromBRM", new Processor() {
					
					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						Message in = exchange.getIn();
						in.setBody(opcode);
					}
				});
				 
				 Exception cause = syncExchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				
				 if(cause!=null){
					 
					 throw cause;
					 
				}
				 
				searchResult = (CUSOPSEARCHOutputFlist) syncExchange.getOut().getBody();
			 
			if(searchResult.getERRORCODE()!=null){
				 
				 log.error("OEP order number : "+oepOrderNumber+". Error while fetching Account data from BRM "+searchResult.getERRORDESCR());
				 throw new RuntimeException(searchResult.getERRORDESCR()); 
				 
				 
			 }
			 
			}
			catch (Exception e) {
				
				String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
				 log.error("OEP order number : "+oepOrderNumber+". Exception while fetching Account data from BRM "+errorMessage);
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
			
			return searchResult;
			
		}
	 
	 	private RESULTS getActiveServiceFromSearchResult(CUSOPSEARCHOutputFlist searchResult){
			
			
			for (RESULTS result: searchResult.getRESULTS()){
				
				if((result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_ACTIVE_STATUS_CODE)
					||(result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_SUSPENDED_STATUS_CODE)){
					
					log.info("Found Active Service. ServiceId: "+result.getLOGIN());
					return result;
				}
			}
			return null;
		}
		
	 
	 
}
