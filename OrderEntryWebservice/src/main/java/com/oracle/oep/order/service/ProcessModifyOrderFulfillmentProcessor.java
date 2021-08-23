package com.oracle.oep.order.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.oracle.oep.brm.opcode.root.Opcode;
import com.oracle.oep.brm.opcode.root.OpcodeResponse;
import com.oracle.oep.brm.opcodes.CUSFLDADDONPKG;
import com.oracle.oep.brm.opcodes.CUSOPCUSTADDPLANInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTADDPLANOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTCANCELPLANInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTCANCELPLANOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTINCOMPATIBLEADDONInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTINCOMPATIBLEADDONOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTUPDATESERVICEInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTUPDATESERVICEOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.DEPOSITS;
import com.oracle.oep.brm.opcodes.PARAMS;
import com.oracle.oep.brm.opcodes.PLAN;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.notif.xsd.NotifAddOnSubscription;
import com.oracle.oep.notif.xsd.NotifAddOnSubscriptionFailure;
import com.oracle.oep.notif.xsd.NotifCancelAddOnSubscription;
import com.oracle.oep.notif.xsd.NotifCancelAddOnSubscriptionFailure;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmMapping;
import com.oracle.oep.order.ebm.transform.ProcessModifyOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessModifyOrderEbmMapping;
import com.oracle.oep.order.model.AddActionPlanType;
import com.oracle.oep.order.model.ChangePlanActionPlanType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ServiceTypeEnum;
import com.oracle.oep.order.presistence.model.BulkBatchMaster;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.CreateOrder;


public class ProcessModifyOrderFulfillmentProcessor {
	
	public static final Logger log = LogManager.getLogger(ProcessModifyOrderFulfillmentProcessor.class);
	ProducerTemplate template = null;
	OepOrder oepOrderDO = null;
	
	
	public void processModifyOrder(final OrderEntryRequest request,final Exchange exchange, final String oepOrderAction, final String orderPayload, final String oepOrderNumber, final String oepServiceType) throws Exception {
		
		Map<String,OEPConfigPlanAttributesT> productSpecMap = new HashMap<String,OEPConfigPlanAttributesT>();
		
		try{
		
			if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION) || oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)
					|| oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN) || oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)){
				
				processAddOnOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
			}
			
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)
					|| oepOrderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)
					|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)
					|| oepOrderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)
					|| oepOrderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)){
				
				processChangePlanOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
			}
			
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)
					|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)){
				
				processSIMChange(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
	
			} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)
					|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)
					|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)){
				
				processMsisdnChange(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
				
	
			} else if(oepOrderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)){
				
				processChangeDetailForBulkSMSOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
	
			} 	
			
			
			else if(oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)){
				
				processMobileServiceManagedCugServices(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, oepServiceType);
	
			} else if(oepOrderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
				
				processFcaMobileService(request, exchange, oepOrderAction, orderPayload, oepOrderNumber);
	
			} 
		}
		
		catch(Exception e){
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			errorMessage = errorMessage!=null?(errorMessage.length()>200?errorMessage.substring(0, 200):errorMessage) : null;
			
			log.error("OEP Order Number : "+oepOrderNumber+". Persisting the exception message to DB "+ errorMessage);
			GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
			oepOrderDO = entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);
			
			oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_FAILED);
			oepOrderDO.setStatusDescription(errorMessage);
			
			saveOepOrderDO(oepOrderDO, template, oepOrderNumber);	
			
			throw e;
			
		}
	}
	
	private void processMsisdnChange(OrderEntryRequest request, Exchange exchange, String oepOrderAction,
			String orderPayload, String oepOrderNumber, Map<String, OEPConfigPlanAttributesT> productSpecMap, String oepServiceType) throws Exception {
		// TODO Auto-generated method stub
		try{

			if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)) {
			
				processMsisdnChangeForOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
			
			}
			else if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)) {
				
				processMsisdnChangeForOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
			}			
			
			else if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)) {
				
				processMsisdnChangeForOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
			}		
			else {
				
			}			
		}
		
		catch(Exception e){
			
			e.printStackTrace();
			log.info("OEP Order number "+oepOrderNumber+". Exception while processing Addon Order. "+e.getMessage());
			throw e;
			
		}

	}

	private void processMsisdnChangeForOrder(OrderEntryRequest request, Exchange exchange, String oepOrderAction,
			String orderPayload, String oepOrderNumber, Map<String, OEPConfigPlanAttributesT> productSpecMap, String oepServiceType) throws SOAPException {
		
		
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing ");
		log.info("OEP order number : "+oepOrderNumber+" .Order Payload :\n "+orderPayload);
		
		String serviceNo = ProcessModifyOrderEbmHelper.getOldServiceNo(request, oepOrderAction);
		String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, oepOrderAction);
		String userName = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
		String accountId = ProcessModifyOrderEbmHelper.getAccountId(request, oepOrderAction);
		String serviceId = ProcessModifyOrderEbmHelper.getServiceId(request, oepOrderAction);
		String newserviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
		String uimServiceIdentifier = ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);

		String basePlan = null;
		String billingAccountId = null;
		String customerAccountId = null;
		OepOrder oepOrderDO = null;
		
		template = exchange.getContext().createProducerTemplate();
		
		//Query BRM for Account details
			
		CUSOPSEARCHOutputFlist searchResult =  getAccountDataFromBRM(template, serviceNo, uimServiceIdentifier, channelName, userName, oepOrderNumber, oepServiceType);	
			
		
		
		RESULTS result = getActiveServiceFromSearchResult(searchResult);
		
		if(result == null){
			
			throw new RuntimeException("No Matching Service is Found in BRM");
		}
		
		

		if (oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)) {
//			basePlan = result.getPLAN().getNAME();
			
			Iterator<RESULTS.PLAN> incompatibleAddonIterator= result.getPLAN().iterator();
			
			while (incompatibleAddonIterator.hasNext()) {
				RESULTS.PLAN allplanList = (RESULTS.PLAN)incompatibleAddonIterator.next();
				Byte planType = allplanList.getCUSFLDPLANTYPE();
				if(planType.equals((byte)1))
				{
				basePlan = allplanList.getNAME();
				}
			}
			
			
		}
		else
		{
			basePlan = result.getCUSFLDPLANNAME();
			
		}
		
		accountId = result.getACCOUNTNO();
		serviceId = result.getSERVICEID();
		billingAccountId = result.getCUSFLDBAACCOUNTNO();
		customerAccountId = result.getCUSFLDCAACCOUNTNO();
		
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" after BRM Query " +
				"basePlan :" + basePlan +",:" + "accountId :" + accountId + ",:" + "serviceId :" + serviceId + ",:"  + "billingAccountId :"
				+ billingAccountId + ",:" + "customerAccountId :" + customerAccountId);
				
		OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(basePlan, template, oepOrderNumber);
              
		productSpecMap.put(basePlan, planAttrbiutes);

		
		oepOrderDO = getOepOrderDO(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, null, productSpecMap, result, oepServiceType, channelName);
		oepOrderDO.setBaAccountNo(billingAccountId);
		
		ProcessModifyOrderEbmMapping addOrderEbmMapping = new ProcessModifyOrderEbmMapping();
		CreateOrder salesOrderEbm = addOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap, result,template);						
		
		log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
		
		if(salesOrderEbm!=null){
			
			oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

		}
		//Save Order Entiry to DB			
		saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
		
		log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
		
		sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber,oepOrderAction);
		
		log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
		
	}


	private void processSIMChange(OrderEntryRequest request, Exchange exchange, String oepOrderAction,
			String orderPayload, String oepOrderNumber, Map<String, OEPConfigPlanAttributesT> productSpecMap, final String oepServiceType) throws Exception {
		// TODO Auto-generated method stub
		try{

			if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)) {
				
				processSIMChangeForOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
			}
			
			else if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)) {
				
				processSIMChangeForOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
			}
			
			else {

			}
			
		}
		
		catch(Exception e){
			
			e.printStackTrace();
			log.info("OEP Order number "+oepOrderNumber+". Exception while processing Addon Order. "+e.getMessage());
			throw e;

		}

	}


	private void processSIMChangeForOrder(OrderEntryRequest request, Exchange exchange, String oepOrderAction,
			String orderPayload, String oepOrderNumber, Map<String, OEPConfigPlanAttributesT> productSpecMap, String oepServiceType) throws SOAPException {
		
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing ");
		log.info("OEP order number : "+oepOrderNumber+" .Order Payload :\n "+orderPayload);
		
		String serviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);		
		String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, oepOrderAction);
		String userName = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
		String accountId = ProcessModifyOrderEbmHelper.getAccountId(request, oepOrderAction);
		String serviceId = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
		String uimServiceIdentifier = ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);
		
		String basePlan = null;
		String billingAccountId = null;
		String customerAccountId = null;
		OepOrder oepOrderDO = null;
		
		
		template = exchange.getContext().createProducerTemplate();
		
		//Query BRM for Account details
		CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRM(template, serviceNo, uimServiceIdentifier, channelName, userName, oepOrderNumber, oepServiceType);	
		
		RESULTS result = getActiveServiceFromSearchResult(searchResult);
		
		if(result == null){
			
			throw new RuntimeException("No Active Service is Found in BRM");
		}
		basePlan = result.getCUSFLDPLANNAME();
		accountId = result.getACCOUNTNO();
		serviceId = result.getSERVICEID();
		billingAccountId = result.getCUSFLDBAACCOUNTNO();
		customerAccountId = result.getCUSFLDCAACCOUNTNO();
		
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" after BRM Query " +
				"basePlan :" + basePlan +",:" + "accountId :" + accountId + ",:" + "serviceId :" + serviceId + ",:"  + "billingAccountId :"
				+ billingAccountId + ",:" + "customerAccountId :" + customerAccountId);
				
		OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(basePlan, template, oepOrderNumber);
              
		productSpecMap.put(basePlan, planAttrbiutes);

		
		oepOrderDO = getOepOrderDO(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, null, productSpecMap, result, oepServiceType, channelName);		
		
		ProcessModifyOrderEbmMapping addOrderEbmMapping = new ProcessModifyOrderEbmMapping();
		CreateOrder salesOrderEbm = addOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap, result,template);						
		
		log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
		
		if(salesOrderEbm!=null){
			
			oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

		}
		//Save Order Entiry to DB			
		saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
		
		log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
		
		sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber,oepOrderAction);
		
		log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
	}
	
	/* 
	 * 
	 * This method processes the add order for all services incliding Add Mobile, BB, Short code and Bulk SMS
	 * 
	 */
	public void processChangePlanOrder(final OrderEntryRequest request,final Exchange exchange, final String oepOrderAction, 

			final String orderPayload, final String oepOrderNumber, Map<String, OEPConfigPlanAttributesT> productSpecMap, final String oepServiceType) throws Exception {
		
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing");
		log.info("OEP order number : "+oepOrderNumber+" with servicetype "+oepServiceType);
		
		try{
			
			List<ChangePlanActionPlanType> orderedPlans = null;
			List<ChangePlanActionPlanType> addOnPlans=null;
			ChangePlanActionPlanType basePlan = null;			
			OepOrder oepOrderDO = null;
			template = exchange.getContext().createProducerTemplate();			

			log.info("OEP order number : "+oepOrderNumber+". Before Fetching plans from order");
			
			orderedPlans = ProcessModifyOrderEbmHelper.getChangePlansFromOrder(request,oepOrderAction);
			
			log.info("OEP order number : "+oepOrderNumber+". Fetched plans from order");
			 
			for(ChangePlanActionPlanType plan : orderedPlans){
				
				String planType=null;
				
				//queryplan and initialize base plan and addOnPlan
				
				
				OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(plan.getPlanName(), template,oepOrderNumber);
				
				if(planAttrbiutes == null){
					
					throw new RuntimeException(plan.getPlanName()+" not found");
					
				}
				planType = planAttrbiutes.getPackageType();
				
				if(planAttrbiutes.getPs()==null){
					
					throw new RuntimeException("PS is not found for "+plan.getPlanName());
				}
				
				log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+planType+", product spec is "+planAttrbiutes.getPs()+", and COS is "+planAttrbiutes.getCos());
				
				if(planType.equalsIgnoreCase(OEPConstants.OEP_BASE_PLAN_TYPE)) {
					
					basePlan = plan;
					
				}
				else if(planType.equals(OEPConstants.OEP_ADDON_PLAN_TYPE)){				
					if(addOnPlans==null){
						
						addOnPlans = new ArrayList<ChangePlanActionPlanType>();
						
					}
					
					addOnPlans.add(plan);	
					
				}			
				productSpecMap.put(plan.getPlanName(),planAttrbiutes);		

			}
			
			basePlan.setPlanAction(OEPConstants.OSM_CHANGEPLAN_ACTION_CODE);
			
			//check any incompatible AddOn's
			
			
			
			String serviceNumber = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
			String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, oepOrderAction);
			String uimServiceIdentifer = ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);
	
			String userName = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
			log.info("Incompatible Addon's called,MSISDN--"+serviceNumber+"-- Plan Name--"+basePlan.getPlanName());
			
			CUSOPCUSTINCOMPATIBLEADDONOutputFlist incompatibleAddonResult = getIncompatibleAddonForMSISDN(template, serviceNumber, channelName, userName, oepOrderNumber,basePlan.getPlanName(),(byte) 1);	

			
			log.info("Incompatible Addon's called");

			
			if(incompatibleAddonResult != null && incompatibleAddonResult.getCUSFLDADDONPKG() != null && incompatibleAddonResult.getCUSFLDADDONPKG().size() > 0){
				
				if(addOnPlans==null){
					
					addOnPlans = new ArrayList<ChangePlanActionPlanType>();
					
				}
				
				Iterator<CUSFLDADDONPKG> incompatibleAddonIterator= incompatibleAddonResult.getCUSFLDADDONPKG().iterator();
				
				while (incompatibleAddonIterator.hasNext()) {
					CUSFLDADDONPKG incompatibleAddon=(CUSFLDADDONPKG)incompatibleAddonIterator.next();
					
					log.info("Incompatible Plan Name"+incompatibleAddon.getCUSFLDADDONPLANNAME());
					log.info("Incompatible Package Id"+incompatibleAddon.getPACKAGEID());
					
					ChangePlanActionPlanType removeaddonPlan=new ChangePlanActionPlanType();
					removeaddonPlan.setPlanName(incompatibleAddon.getCUSFLDADDONPLANNAME());
					removeaddonPlan.setPlanAction(OEPConstants.OSM_REMOVE_ACTION_CODE);
					OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(incompatibleAddon.getCUSFLDADDONPLANNAME(), template,oepOrderNumber);
					productSpecMap.put(incompatibleAddon.getCUSFLDADDONPLANNAME(),planAttrbiutes);
					addOnPlans.add(removeaddonPlan);
				}
				
				//log.info("Incompatible Addon Name"+incompatibleAddonResult.getCUSFLDADDONPKG().get);
				
			}
			
			CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRM(template, serviceNumber, uimServiceIdentifer, channelName, userName, oepOrderNumber, oepServiceType);
			
			RESULTS result = getActiveServiceFromSearchResult(searchResult);
			
			if(result == null){
				
				throw new RuntimeException("No Active Service is Found in BRM");
			}
			
			//generate Order and OrderLine DataObject's		
			oepOrderDO = getOepOrderDO(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, addOnPlans, productSpecMap, result, oepServiceType, channelName);
			oepOrderDO.setBaAccountNo(result.getCUSFLDBAACCOUNTNO());
			
			ProcessModifyOrderEbmMapping modifyOrderEbmMapping = new ProcessModifyOrderEbmMapping();
			CreateOrder salesOrderEbm = modifyOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap,result,template);						

			
			log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
			
			BulkBatchMaster master= new BulkBatchMaster();
			master.setBatchId(OEPConstants.OEP_BATCH_ID_DUMMY_VALUE);
			
			oepOrderDO.setBulkBatchMaster(master);	
			
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			
			//Save Order Entiry to DB			
			saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
			
			log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
			
			//send sales order to OSM queue			
			//Query BRM for Account details				
			
			log.info("Billing Account---"+searchResult.getRESULTS().get(0).getCUSFLDBAACCOUNTNO()+"Customer Account No --"+searchResult.getRESULTS().get(0).getCUSFLDCAACCOUNTNO());		
			
			sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber,oepOrderAction);			
			log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");			
		
		}
		catch (Exception e) {


			
			e.printStackTrace();
			log.info("OEP Order Number : "+oepOrderNumber+". Exception caught while processing the order.. "+e.getMessage());
			log.info(e);

			throw e;

		}
		
	}
	
	
	/* 
	 * 
	 * This method generate OepOrder entity bean and OepOrderLine entity bean associate OepOrderLine with OepOrder
	 * 
	 */
	private OepOrder getOepOrderDO(final OrderEntryRequest request, final String oepOrderAction, final String oepOrderNumber, final String orderPayload,
			final ChangePlanActionPlanType basePlan, List<ChangePlanActionPlanType> addOnPlans, final Map<String,OEPConfigPlanAttributesT> productSpecMap , final RESULTS searchResult, final String oepServiceType, final String channelName){

		
		log.info("OEP Order Number : "+oepOrderNumber+".Started Initializing Oep Order Entity Bean .Order payload is "+orderPayload);	

		GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
		
		String csrId = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
		Timestamp orderStartDate = ProcessModifyOrderEbmHelper.getOrderStartDate(request, oepOrderAction);
		String accountId= ProcessModifyOrderEbmHelper.getAccountId(request, oepOrderAction,searchResult);		
		String serviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
		if(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION.equals(oepOrderAction)){
			
			serviceNo = ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);
		}
		
		
		log.info("OEP Order Number : "+oepOrderNumber+". Before calling entityBeanGenerator.getOepOrderDO()");
		
		OepOrder oepOrderDO= entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);		
			
		entityBeanGenerator.getOepOrderDO(oepOrderDO, serviceNo, csrId,  orderStartDate, oepOrderAction, oepOrderNumber, orderPayload, null, channelName);	
		
		List<OepOrderLine> oepOrderLines = new ArrayList<OepOrderLine>();		
		
		log.info("OEP Order Number : "+oepOrderNumber+".Adding base plan,"+basePlan.getPlanName()+" to  Order Entity Bean ");
		
		OepOrderLine basePlanOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, basePlan.getPlanName(), 
				productSpecMap.get(basePlan.getPlanName()).getPs(), basePlan.getPlanAction(), accountId, oepServiceType, null, oepOrderNumber);
		
		basePlanOrderLine.setOepOrder(oepOrderDO);
		oepOrderLines.add(basePlanOrderLine);
		log.info("Before Addon Plans are set");
		
		if(addOnPlans != null){
			
			for (ChangePlanActionPlanType addOnPlan : addOnPlans){
				
				log.info("OEP Order Number : "+oepOrderNumber+".Adding Addon plan,"+addOnPlan.getPlanName()+" to  Order Entity Bean ");
				log.info("PS : "+productSpecMap.get(addOnPlan.getPlanName()).getPs()+".Adding Addon plan,"+addOnPlan.getPlanName()+" to Action "+ addOnPlan.getPlanAction());				


				OepOrderLine addOnOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, addOnPlan.getPlanName(),
						productSpecMap.get(addOnPlan.getPlanName()).getPs(), addOnPlan.getPlanAction(), accountId, oepServiceType,  basePlanOrderLine.getOrderLineId(), oepOrderNumber);
				
				addOnOrderLine.setOepOrder(oepOrderDO);
				
				oepOrderLines.add(addOnOrderLine);
				
			}
		}
		
		oepOrderDO.setOepOrderLines(oepOrderLines);

		log.info("Addon Plans are set");

		
		log.info("OEP Order Number : "+oepOrderNumber+".Oep Order Entity Bean is set");

		
		return oepOrderDO;


		
	}
	/**
	 *  
	 * This method performs the fulfillment for all AddOn either Add or Remove
	 * @param productSpecMap 
	 * @throws Exception 
	 * 
	 **/
	private void processAddOnOrder(final OrderEntryRequest request,final Exchange exchange, final String oepOrderAction, 
			final String orderPayload, final String oepOrderNumber, Map<String, OEPConfigPlanAttributesT> productSpecMap, final String oepServiceType) throws Exception{
		
		try{
			
			if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
				
				processDHIOAddOnOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
			}
			else if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN) 
					|| oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)
					|| oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)
					) {
			
				processAddRemovePlanOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, productSpecMap, oepServiceType);
			
			}
			
			else {
				
			}
			
		}
		
		catch(Exception e){
		
			e.printStackTrace();
			
			String errorMessage = null;
			if(e.getCause()!=null){
				
				errorMessage = e.getCause().getLocalizedMessage();
			}
			
			else{
				
				errorMessage = e.getLocalizedMessage();
			}
			log.error("OEP Order number "+oepOrderNumber+". Exception while processing Addon Order. "+errorMessage);
			throw e;
			
		}
	
	}

	private void processAddRemovePlanOrder(final OrderEntryRequest request, final Exchange exchange, final String oepOrderAction,
			final String orderPayload, final String oepOrderNumber, final Map<String, OEPConfigPlanAttributesT> productSpecMap, final String oepServiceType) {
		// TODO Auto-generated method stub
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing ");
		log.info("OEP order number : "+oepOrderNumber+" .Order Payload :\n "+orderPayload);
		
		String serviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
		List<ChangePlanActionPlanType> planList = ProcessModifyOrderEbmHelper.getPlansFromOrder(request, oepOrderAction);
		String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, oepOrderAction);
		String userName = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
		String uimServiceIdentifer = ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);
		
		String basePlan = null;
		String accountId = null;
		String serviceId = null;
		String billingAccountId = null;
		String customerAccountId = null;
		OepOrder oepOrderDO = null;
		String getPayType = null;
		template = exchange.getContext().createProducerTemplate();
		
		//Query BRM for Account details
		CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRM(template, serviceNo, uimServiceIdentifer, channelName, userName, oepOrderNumber, oepServiceType);	
		
		RESULTS result = getActiveServiceFromSearchResult(searchResult);
		
		if(result == null){
			
			throw new RuntimeException("No Active Service is Found in BRM");
		}
		
		
		if (oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE) || oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)) {
			
			Iterator<RESULTS.PLAN> incompatibleAddonIterator= result.getPLAN().iterator();
			
			while (incompatibleAddonIterator.hasNext()) {
				RESULTS.PLAN allplanList = (RESULTS.PLAN)incompatibleAddonIterator.next();
				Byte planType = allplanList.getCUSFLDPLANTYPE();
				if(planType.equals((byte)1))
				{
				basePlan = allplanList.getNAME();
				}
			}
			
		}
		else
		{
			basePlan = result.getCUSFLDPLANNAME();
			
		}
		
		accountId = result.getACCOUNTNO();
		serviceId = result.getSERVICEID();
		billingAccountId = result.getCUSFLDBAACCOUNTNO();
		customerAccountId = result.getCUSFLDCAACCOUNTNO();
	
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" after BRM Query " +
				"basePlan :" + basePlan +",:" + "accountId :" + accountId + ",:" + "serviceId :" + serviceId + ",:"  + "billingAccountId :"
				+ billingAccountId + ",:" + "customerAccountId :" + customerAccountId);
		
		
		GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
		
		oepOrderDO = entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);	
		oepOrderDO.setBaAccountNo(billingAccountId);

		
		Timestamp orderStartDate = ProcessModifyOrderEbmHelper.getOrderStartDate(request, oepOrderAction);
		
		log.info("OEP Order Number : "+oepOrderNumber+". Before calling entityBeanGenerator.getOepOrderDO()");
			
		entityBeanGenerator.getOepOrderDO(oepOrderDO, serviceNo, userName, orderStartDate, oepOrderAction, oepOrderNumber, orderPayload, null, channelName);
		productSpecMap.put(basePlan, queryBRMForPlanAttributes(basePlan, template, oepOrderNumber));
		
		log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(basePlan).getPackageType()+""
				+ ", product spec is "+productSpecMap.get(basePlan).getPs()+", and COS is "+productSpecMap.get(basePlan).getCos());
		
		List<OepOrderLine> oepOrderLines = new ArrayList<OepOrderLine>();
		
		log.info("OEP Order Number : "+oepOrderNumber+".Adding base plan,"+basePlan+" to  Order Entity Bean ");
		
		OepOrderLine basePlanOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, basePlan, 
				productSpecMap.get(basePlan).getPs(), ProcessModifyOrderEbmHelper.getPlanAction(request, productSpecMap.get(basePlan), oepOrderAction), accountId, oepServiceType, null, oepOrderNumber);
		
		basePlanOrderLine.setOepOrder(oepOrderDO);
		basePlanOrderLine.setCommercialPlanName(productSpecMap.get(basePlan).getCommercialPlanName());
		oepOrderLines.add(basePlanOrderLine);
		
		for(ChangePlanActionPlanType planType : planList) {
			
			String addOnPlan = planType.getPlanName();
			
			productSpecMap.put(addOnPlan, queryBRMForPlanAttributes(addOnPlan, template, oepOrderNumber));	
			
			if(productSpecMap.get(addOnPlan) == null){
				
				throw new RuntimeException("AddOn Plan "+addOnPlan+" not found");
			}
			else if(productSpecMap.get(addOnPlan).getPs()==null){
				
				throw new RuntimeException("PS is not found for "+addOnPlan);
			}
				
			log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(addOnPlan).getPackageType()+""
					+ ", product spec is "+productSpecMap.get(addOnPlan).getPs()+", and COS is "+productSpecMap.get(addOnPlan).getCos());
			
			log.info("OEP Order Number : "+oepOrderNumber+".Adding addOn plan,"+addOnPlan+" to  Order Entity Bean ");
			
			OepOrderLine addOnOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, addOnPlan,
					productSpecMap.get(addOnPlan).getPs(), planType.getPlanAction(), accountId, oepServiceType,  basePlanOrderLine.getOrderLineId(), oepOrderNumber);
			
			addOnOrderLine.setOepOrder(oepOrderDO);
			addOnOrderLine.setCommercialPlanName(productSpecMap.get(addOnPlan).getCommercialPlanName());
			oepOrderLines.add(addOnOrderLine);
			
		}
		
		/*
		 * This code is for checking Power/PowerPlus and if present the same will be sent to osm for removal
		 */
		for(ChangePlanActionPlanType planType : planList) {
			
			String addOnPlan = planType.getPlanName();
			String addOnPkgSubType = productSpecMap.get(addOnPlan).getAddonPkgSubType();
			String serviceNumber = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);

			if(addOnPkgSubType != null && !(planType.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE)) )
			{
			if(addOnPkgSubType.equalsIgnoreCase("POWER") || addOnPkgSubType.equalsIgnoreCase("POWERPLUS")   || addOnPkgSubType.equalsIgnoreCase("POWERPLUSTBB") || addOnPkgSubType.equalsIgnoreCase("POSTPAID_BOOSTER")
				|| addOnPkgSubType.equalsIgnoreCase("PREPAID_MINI")	
				|| "BB_LTE_PLAN".equalsIgnoreCase(addOnPkgSubType))
			{
				log.info("Incompatible Addon's called,MSISDN--"+serviceNumber+"-- Plan Name--"+addOnPlan);
				
				CUSOPCUSTINCOMPATIBLEADDONOutputFlist incompatibleAddonResult = getIncompatibleAddonForMSISDNPower(template, serviceNumber, channelName, userName, oepOrderNumber,addOnPlan,(byte) 2);	

				
				log.info("Incompatible Addon's called");

				
				if(incompatibleAddonResult != null && incompatibleAddonResult.getCUSFLDADDONPKG() != null && incompatibleAddonResult.getCUSFLDADDONPKG().size() > 0){
					
										
					Iterator<CUSFLDADDONPKG> incompatibleAddonIterator= incompatibleAddonResult.getCUSFLDADDONPKG().iterator();
					
					while (incompatibleAddonIterator.hasNext()) {
						CUSFLDADDONPKG incompatibleAddon=(CUSFLDADDONPKG)incompatibleAddonIterator.next();
						
						log.info("Incompatible Plan Name"+incompatibleAddon.getCUSFLDADDONPLANNAME());
						log.info("Incompatible Package Id"+incompatibleAddon.getPACKAGEID());
						String fetchedaddOnPlan = incompatibleAddon.getCUSFLDADDONPLANNAME();
						String removeActionAddOnPlan = OEPConstants.OSM_REMOVE_ACTION_CODE;
						
						OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(incompatibleAddon.getCUSFLDADDONPLANNAME(), template,oepOrderNumber);
						productSpecMap.put(incompatibleAddon.getCUSFLDADDONPLANNAME(),planAttrbiutes);
						
						if(productSpecMap.get(incompatibleAddon.getCUSFLDADDONPLANNAME()) == null){
							
							throw new RuntimeException("AddOn Plan "+incompatibleAddon.getCUSFLDADDONPLANNAME()+" not found");
						}
						log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(fetchedaddOnPlan).getPackageType()+""
								+ ", product spec is "+productSpecMap.get(fetchedaddOnPlan).getPs()+", and COS is "+productSpecMap.get(fetchedaddOnPlan).getCos());
						
						log.info("OEP Order Number : "+oepOrderNumber+".Adding addOn plan,"+fetchedaddOnPlan+" to  Order Entity Bean ");
						
						OepOrderLine addOnOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, fetchedaddOnPlan,
								productSpecMap.get(fetchedaddOnPlan).getPs(), removeActionAddOnPlan, accountId, oepServiceType,  basePlanOrderLine.getOrderLineId(), oepOrderNumber);
						
						addOnOrderLine.setOepOrder(oepOrderDO);
						
						oepOrderLines.add(addOnOrderLine);
					}
					
					//log.info("Incompatible Addon Name"+incompatibleAddonResult.getCUSFLDADDONPKG().get);
					
				}

				
			}
			}
			
		}
		
		
		
		
		
		
		
		
		
		
		oepOrderDO.setOepOrderLines(oepOrderLines);
		
		log.info("OEP Order Number : "+oepOrderNumber+".Oep Order Entity Bean is set");
		
		BulkBatchMaster master= new BulkBatchMaster();
		master.setBatchId(OEPConstants.OEP_BATCH_ID_DUMMY_VALUE);
		
		oepOrderDO.setBulkBatchMaster(master);
		
		ProcessModifyOrderEbmMapping modifyOrderEbmMapping = new ProcessModifyOrderEbmMapping();
		CreateOrder salesOrderEbm = modifyOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap, result,template);						
		
		if(salesOrderEbm!=null){
			
			oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

		}
		
		//Save Order Entiry to DB			
		saveOepOrderDO(oepOrderDO,template, oepOrderNumber);		

		log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
		
		
		log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");	
		
			
		try {
			
			sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber,oepOrderAction);
			
		} 
		catch (SOAPException e) {
			// TODO Auto-generated catch block
			log.info("Error sending OEP Order Number to OSM Queue : "+oepOrderNumber);
		}
		
		log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");		
			
		
	}
	
	private void processDHIOAddOnOrder(final OrderEntryRequest request,final Exchange exchange, 
			final String oepOrderAction, final String orderPayload, final String oepOrderNumber, final Map<String, OEPConfigPlanAttributesT> productSpecMap, final String oepServiceType) throws Exception{
		
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing ");
		log.info("OEP order number : "+oepOrderNumber+" .Order Payload :\n "+orderPayload);
		
		String serviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
		String serviceType = ProcessModifyOrderEbmHelper.getOrderServiceType(request, oepOrderAction);
		//String basePackage = ProcessModifyOrderEbmHelper.getBasePackageName(request, oepOrderAction);
		String addOnPackage = ProcessModifyOrderEbmHelper.getAddonPackageName(request, oepOrderAction);
		String depositReceiptNumner = ProcessModifyOrderEbmHelper.getDepositReceiptNumber(request, oepOrderAction);
		String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, oepOrderAction);
		String channelTrackingId = ProcessModifyOrderEbmHelper.getChannelTrackingId(request, oepOrderAction);
		String planAction = ProcessModifyOrderEbmHelper.getPlanAction(request, oepOrderAction);
		String userName = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
		String uimServiceIdentifier = request.getProcessAddOnManagment().getLoginId();
		String loginID = request.getProcessAddOnManagment().getLoginId();
		
		if(uimServiceIdentifier==null){
			
			ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);
		}
		
		
		
		String basePlan = null;
		String accountId = null;
		String serviceId = null;
		String billingAccountId = null;
		String customerAccountId = null;
		OepOrder oepOrderDO = null;
		RESULTS result = null;
		
		
		
		template = exchange.getContext().createProducerTemplate();
		
		try{
		//Query BRM for Account details
			CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRM(template, serviceNo, uimServiceIdentifier, channelName, userName, oepOrderNumber, oepServiceType);	
			
			result = getActiveServiceFromSearchResult(searchResult);
		}
		
		catch(Exception e){
			
			
			if(e.getCause()!=null){
				
				throw new Exception(e.getCause().getLocalizedMessage());
			}
			
			else {
				
				throw new Exception(e.getLocalizedMessage());
			}
			
		}
		
		
		/*if(loginID!=null){
			
			for (RESULTS service: searchResult.getRESULTS()){
				
				if(result.getLOGIN().equals(loginID)){
					
					 result = service;
				}
			} 
		}*/
		
		if(result == null){
			
			throw new RuntimeException("No Active Service is Found in BRM");
		}
		
		
			
		basePlan = result.getCUSFLDPLANNAME();		
		accountId = result.getACCOUNTNO();
		serviceId = result.getSERVICEID();
		billingAccountId = result.getCUSFLDBAACCOUNTNO();
		customerAccountId = result.getCUSFLDCAACCOUNTNO();
		
		//query BasePlan Details and Add to product spec
		productSpecMap.put(basePlan, queryBRMForPlanAttributes(basePlan, template, oepOrderNumber));
		
		
		if(productSpecMap.get(basePlan)==null){
			
			throw new RuntimeException("Base Plan "+basePlan+" not found");
			
		}
		else if(productSpecMap.get(basePlan).getPs()==null){
			throw new RuntimeException("PS is not found for "+basePlan);
			
		}
		log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(basePlan).getPackageType()+""
				+ ", product spec is "+productSpecMap.get(basePlan).getPs()+", and COS is "+productSpecMap.get(basePlan).getCos());

		
		//query Addon Attributes
		productSpecMap.put(addOnPackage, queryBRMForPlanAttributes(addOnPackage, template, oepOrderNumber));
		
		if(productSpecMap.get(addOnPackage)==null){
			
			throw new RuntimeException("Addon Plan "+addOnPackage+" not found");
			
		}
		else if(productSpecMap.get(addOnPackage).getPs()==null){
			throw new RuntimeException("PS is not found for "+addOnPackage);
			
		}
		
		log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(addOnPackage).getPackageType()+""
				+ ", product spec is "+productSpecMap.get(addOnPackage).getPs()+", and COS is "+productSpecMap.get(addOnPackage).getCos());
		
		
		 oepOrderDO = getOepOrderDO(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, addOnPackage, productSpecMap, result, oepServiceType, channelName);
		 oepOrderDO.setBaAccountNo(billingAccountId);
		 
		 ProcessModifyOrderEbmMapping modifyOrderEbmMapping = new ProcessModifyOrderEbmMapping();
			CreateOrder salesOrderEbm = modifyOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap, result,template);						
			
			log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
			
			BulkBatchMaster master= new BulkBatchMaster();
			master.setBatchId(OEPConstants.OEP_BATCH_ID_DUMMY_VALUE);
			
			oepOrderDO.setBulkBatchMaster(master);	
			
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			//Save Order Entiry to DB			
			saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
			
			log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
			String addOnPkgSubType= productSpecMap.get(addOnPackage).getAddonPkgSubType();
			
			log.info("OEP Order Number : "+oepOrderNumber+".AddOn plan : "+addOnPackage+". addOnPkgSubType : "+addOnPkgSubType);
			
						
			if((OEPConstants.MOBILE_SMS_ADDON_PACKAGE_TYPE.equalsIgnoreCase(productSpecMap.get(addOnPackage).getAddonPkgType()) 
					|| OEPConstants.MOBILE_VOICE_ADDON_PACKAGE_TYPE.equalsIgnoreCase(productSpecMap.get(addOnPackage).getAddonPkgType())) 
					&& !(channelName.equalsIgnoreCase(OEPConstants.OAP_CHANNEL_NAME))
					|| "DIP".equals(productSpecMap.get(addOnPackage).getAddonPkgSubType())) {
				
					
				if(planAction.equalsIgnoreCase(OEPConstants.OSM_ADD_ACTION_CODE)  && "DIP".equals(productSpecMap.get(addOnPackage).getAddonPkgSubType())){
					
					CUSOPCUSTUPDATESERVICEOutputFlist updateServiceResponse = sendDIPAddPlanRequest(oepOrderNumber, request, result, channelName, serviceNo, oepOrderAction,addOnPkgSubType);
					if(updateServiceResponse.getERRORCODE()!=null){
						
						 log.info("OEP order number : "+oepOrderNumber+". Error while Adding Plan : "+updateServiceResponse.getERRORDESCR());
						 
						 setOrderStatus(oepOrderDO,  OEPConstants.OEP_ORDER_STATUS_FAILED, updateServiceResponse.getERRORDESCR(),OEPConstants.OEP_ORDER_STATUS_FAILED, updateServiceResponse.getERRORDESCR(), oepOrderNumber);
						 
						 sendFailureNotification(oepOrderDO);
						 throw new RuntimeException(updateServiceResponse.getERRORDESCR()); 
						 
						 
					 }
					
					setOrderStatus(oepOrderDO,  OEPConstants.OEP_ORDER_STATUS_COMPLETE, OEPConstants.OEP_ORDER_STATUS_COMPLETE_DESCRIPTION, OEPConstants.OEP_ORDER_STATUS_FULFILL_BILLING, OEPConstants.OEP_ORDER_STATUS_FULFILL_BILLING, oepOrderNumber);
					 
				}

				else if(planAction.equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE)){
					
					CUSOPCUSTCANCELPLANOutputFlist cancelPlanResponse = sendCancelPlanRequestToBRM(oepOrderNumber, request, result, channelName, serviceNo, oepOrderAction);
					
					if(cancelPlanResponse.getERRORCODE()!=null){
						
						 log.info("OEP order number : "+oepOrderNumber+". Error while Cancelling Plan : "+cancelPlanResponse.getERRORDESCR());
						 
						 setOrderStatus(oepOrderDO,  OEPConstants.OEP_ORDER_STATUS_FAILED, cancelPlanResponse.getERRORDESCR(), OEPConstants.OEP_ORDER_STATUS_FAILED, cancelPlanResponse.getERRORDESCR(),oepOrderNumber);
						 
						 throw new RuntimeException(cancelPlanResponse.getERRORDESCR()); 
						 
						 
					 }
					
					setOrderStatus(oepOrderDO,  OEPConstants.OEP_ORDER_STATUS_COMPLETE, OEPConstants.OEP_ORDER_STATUS_COMPLETE_DESCRIPTION, OEPConstants.OEP_ORDER_STATUS_FULFILL_BILLING, OEPConstants.OEP_ORDER_STATUS_FULFILL_BILLING, oepOrderNumber);
				
				}
				
				else if(planAction.equalsIgnoreCase(OEPConstants.OSM_ADD_ACTION_CODE)){
				
					CUSOPCUSTADDPLANOutputFlist addPlanResponse = sendAddPlanRequestToBRM(oepOrderNumber, request, result, channelName, serviceNo, oepOrderAction);
					
					if(addPlanResponse.getERRORCODE()!=null){
						
						 log.info("OEP order number : "+oepOrderNumber+". Error while Adding Plan : "+addPlanResponse.getERRORDESCR());
						 
						 setOrderStatus(oepOrderDO,  OEPConstants.OEP_ORDER_STATUS_FAILED, addPlanResponse.getERRORDESCR(),OEPConstants.OEP_ORDER_STATUS_FAILED, addPlanResponse.getERRORDESCR(), oepOrderNumber);
						 
						 sendFailureNotification(oepOrderDO);
						 throw new RuntimeException(addPlanResponse.getERRORDESCR()); 
						 
						 
					 }
					
					setOrderStatus(oepOrderDO,  OEPConstants.OEP_ORDER_STATUS_COMPLETE, OEPConstants.OEP_ORDER_STATUS_COMPLETE_DESCRIPTION, OEPConstants.OEP_ORDER_STATUS_FULFILL_BILLING, OEPConstants.OEP_ORDER_STATUS_FULFILL_BILLING, oepOrderNumber);
					 
				}
				
				
				
				generatePlanManagementNotification(oepOrderDO, template);
			}			
			
			
			else {
				
				sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber,oepOrderAction);
				
				log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
			}
		
		
	}
	
	/**
	 * 
	 * @param orderLines
	 * @param statusCode
	 * @param statusDescription
	 */
	private void setOrderLineStatus(final OepOrder oepOrderDO, final String statusCode, final String statusDescription){
		
		log.info("Setting OrderLine status");
		for (OepOrderLine line: oepOrderDO.getOepOrderLines()){
			
			log.info("Setting OrderLine status"+line.getOrderLineRowid());
			line.setStatusCode(statusCode);
			line.setStatusDescription(statusDescription);
			line.setOepOrder(oepOrderDO);
		}
		
		
		
	}
	
	/**
	 * 
	 * @param oepOrderDO
	 * @param statusCode
	 * @param statusDescription
	 */
	private void setOrderStatus(OepOrder oepOrderDO, final String statusCode, final String statusDescription,final String orderlineStatus, final String orderLineStatusDescription,  final String oepOrderNumber){
		
		log.info(oepOrderDO.getOrderId() +": Setting Order status");
		
		GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
		
		oepOrderDO = entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);
		
		oepOrderDO.setStatusCode(statusCode);
		oepOrderDO.setStatusDescription(statusDescription);
		
		setOrderLineStatus(oepOrderDO, orderlineStatus, orderLineStatusDescription);
		
		saveOepOrderDO(oepOrderDO, template, oepOrderNumber);
		
		log.info(oepOrderDO.getOrderId() +": Order status is persisted to DB");
	}
	
	
	/**
	 * 
	 * @param oepOrderNumber
	 * @param template
	 * @param oepOrderRequest
	 * @param result
	 * @param channel
	 * @param serviceNo
	 * @param oepOrderAction
	 * @return
	 * @throws Exception 
	 */
	private CUSOPCUSTCANCELPLANOutputFlist sendCancelPlanRequestToBRM(final String oepOrderNumber, final OrderEntryRequest oepOrderRequest, final RESULTS result, 
			final String channel, final String serviceNo, final String oepOrderAction) throws Exception{
		
		log.info("OEP Order Number : "+oepOrderNumber+" Preparing to send Cancel Plan request to BRM");
		
		CUSOPCUSTCANCELPLANInputFlist cancelPlanRequest = new CUSOPCUSTCANCELPLANInputFlist();
		cancelPlanRequest.setPOID(OEPConstants.BRM_POID_GSM);
		cancelPlanRequest.setACCOUNTNO(result.getACCOUNTNO());
		cancelPlanRequest.setPROGRAMNAME(channel);
		cancelPlanRequest.setMSISDN(serviceNo);
		cancelPlanRequest.setSERVICEID(result.getLOGIN());
		cancelPlanRequest.setUSERNAME(ProcessModifyOrderEbmHelper.getCsrId(oepOrderRequest, oepOrderAction));
		
		List<PLAN> cancelPlanList = cancelPlanRequest.getPLAN();
		
		PLAN addOnPlan = new PLAN();
		String addOnPackageName = ProcessModifyOrderEbmHelper.getAddonPackageName(oepOrderRequest, oepOrderAction);
		
		addOnPlan.setNAME(addOnPackageName);
		addOnPlan.setPACKAGEID(ProcessModifyOrderEbmHelper.getAddOnPackageId(oepOrderRequest, result, addOnPackageName));		
		
		cancelPlanList.add(addOnPlan);
		
		log.info("OEP Order Number : "+oepOrderNumber+" OEP Order Action : "+oepOrderAction + " Plan Action : Remove" + " Plans are set");
		
				
		String inputXML = (String) template.requestBody("direct:convertToString", cancelPlanRequest);
		
		log.info("OEP Order Number : "+ oepOrderNumber + ", OEP Order Action : "+ oepOrderAction +", Plan Action : Remove. Addon Plan XML Payload is : \n "+inputXML);

		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_CANCEL_PLAN_OPCODE);
		opcode.setInputXml(inputXML);
		
		Exchange syncExchange = template.request("direct:sendCancelPlanRequestToBRM", new Processor() {
			
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
		
		CUSOPCUSTCANCELPLANOutputFlist cancelPlanResponse = (CUSOPCUSTCANCELPLANOutputFlist) syncExchange.getOut().getBody();
		
		log.info("OEP Order Number : "+ oepOrderNumber + ", OEP Order Action : "+ oepOrderAction +", Recieved Cancel Plan Response "+template.requestBody("direct:convertToString", cancelPlanResponse));
		
		return cancelPlanResponse;
	}
	
	/**
	 * 
	 * @param oepOrderNumber
	 * @param template
	 * @param oepOrderRequest
	 * @param result
	 * @param channel
	 * @param serviceNo
	 * @param oepOrderAction
	 * @return
	 * @throws Exception 
	 */
	private CUSOPCUSTADDPLANOutputFlist sendAddPlanRequestToBRM(final String oepOrderNumber, final OrderEntryRequest oepOrderRequest, final RESULTS result, 
			final String channel, final String serviceNo, final String oepOrderAction) throws Exception{
		
		log.info("OEP Order Number : "+oepOrderNumber+" Preparing to send add Plan request to BRM");
		
		CUSOPCUSTADDPLANInputFlist addPlanRequest = new CUSOPCUSTADDPLANInputFlist();
		addPlanRequest.setPOID(OEPConstants.BRM_POID_GSM);
		addPlanRequest.setACCOUNTNO(result.getACCOUNTNO());
		addPlanRequest.setCUSFLDBAACCOUNTNO(result.getCUSFLDBAACCOUNTNO());
		addPlanRequest.setCUSFLDCAACCOUNTNO(result.getCUSFLDCAACCOUNTNO());
		addPlanRequest.setPROGRAMNAME(channel);
		addPlanRequest.setMSISDN(serviceNo);
		addPlanRequest.setSERVICEID(result.getLOGIN());
		addPlanRequest.setUSERNAME(ProcessModifyOrderEbmHelper.getCsrId(oepOrderRequest, oepOrderAction));
		
		List<PLAN> addPlanList = addPlanRequest.getPLAN();
		
		PLAN addOnPlan = new PLAN();
		
		addOnPlan.setNAME(ProcessModifyOrderEbmHelper.getAddonPackageName(oepOrderRequest, oepOrderAction));
		addOnPlan.setCUSFLDPLANTYPE("2");
		addOnPlan.setSTARTT(OrderDataUtils.getXMLGregorianCalendarFromTimeStamp(new Date()));
		addOnPlan.setCUSFLDOVERRIDENPRICE(oepOrderRequest.getProcessAddOnManagment().getOverridenPrice());
		
		addPlanList.add(addOnPlan);
		
		log.info("OEP Order Number : "+oepOrderNumber+" OEP Order Action : "+oepOrderAction + " Plan Action : Add" + " Plans are set");
		
		if(oepOrderRequest.getProcessAddOnManagment().getDepositReceiptNo()!=null){
			
			List<DEPOSITS> deposistsList = addPlanRequest.getDEPOSITS();
			DEPOSITS deposit = new DEPOSITS();
			deposit.setAMOUNT(oepOrderRequest.getProcessAddOnManagment().getDepositAmount());
			deposit.setRECEIPTNO(oepOrderRequest.getProcessAddOnManagment().getDepositReceiptNo());
			deposit.setCUSFLDCOLLECTEDON(oepOrderRequest.getProcessAddOnManagment().getCollectedOn());
			deposit.setTYPE(oepOrderRequest.getProcessAddOnManagment().getDepositType());
			deposit.setCUSFLDSERVICETYPE(oepOrderRequest.getProcessAddOnManagment().getServiceType().value());
			deposit.setTYPESTR(oepOrderRequest.getProcessAddOnManagment().getAddOnPackageName());
			
			deposistsList.add(deposit);
			
			log.info("OEP Order Number : "+oepOrderNumber+" OEP Order Action : "+oepOrderAction + " Plan Action : Add" + " Deposits are set");
			
		}
		
		String inputXML = (String) template.requestBody("direct:convertToString", addPlanRequest);
		
		log.info("OEP Order Number : "+ oepOrderNumber + ", OEP Order Action : "+ oepOrderAction +", Plan Action : Add. Addon Plan XML Payload is : \n "+inputXML);

		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_ADD_PLAN_OPCODE);
		opcode.setInputXml(inputXML);
		
		Exchange syncExchange = template.request("direct:sendAddPlanRequestToBRM", new Processor() {
			
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
		
		CUSOPCUSTADDPLANOutputFlist addOnPlanResponse =(CUSOPCUSTADDPLANOutputFlist) syncExchange.getOut().getBody();
		
		
		log.info("OEP Order Number : "+ oepOrderNumber + ", OEP Order Action : "+ oepOrderAction +", Recieved Add Plan Response "+template.requestBody("direct:convertToString", addOnPlanResponse));
		
		return addOnPlanResponse;
	}
	
	/**
	 * 
	 * @param oepOrderNumber
	 * @param request
	 * @param result
	 * @param channelName
	 * @param serviceNo
	 * @param oepOrderAction
	 * @param addOnPkgSubType
	 * @return
	 * @throws Exception 
	 */
	private CUSOPCUSTUPDATESERVICEOutputFlist sendDIPAddPlanRequest(String oepOrderNumber, OrderEntryRequest oepOrderRequest,
			RESULTS result, String channel, String serviceNo, String oepOrderAction, String addOnPkgSubType) throws Exception {
		// TODO Auto-generated method stub
		
		CUSOPCUSTUPDATESERVICEInputFlist updateServiceRequest =  new CUSOPCUSTUPDATESERVICEInputFlist();
		updateServiceRequest.setPOID(OEPConstants.BRM_POID);
		updateServiceRequest.setACCOUNTNO(result.getACCOUNTNO());
		updateServiceRequest.setPROGRAMNAME(channel);
		updateServiceRequest.setMSISDN(serviceNo);
		updateServiceRequest.setSERVICEID(result.getLOGIN());
		updateServiceRequest.setUSERNAME(ProcessModifyOrderEbmHelper.getCsrId(oepOrderRequest, oepOrderAction));
		updateServiceRequest.setCUSFLDDEVICEMODEL(oepOrderRequest.getProcessAddOnManagment().getDeviceMake());
		updateServiceRequest.setACTION(OEPConstants.BRM_CHANGE_HANDSET);
		
		if(oepOrderRequest.getProcessAddOnManagment().getDeviceType()!=null)
			updateServiceRequest.setCUSFLDDEVICETYPE(Integer.parseInt(oepOrderRequest.getProcessAddOnManagment().getDeviceType()));
		
		if("1".equals(oepOrderRequest.getProcessAddOnManagment().getDeviceType()))
			updateServiceRequest.setREASONCODE("Rental");
		
		updateServiceRequest.setCUSFLDINSTALLMENTPLAN(1);
		
		List<PLAN> addPlanList = updateServiceRequest.getPLAN();
		
		PLAN addOnPlan = new PLAN();
		
		addOnPlan.setNAME(ProcessModifyOrderEbmHelper.getAddonPackageName(oepOrderRequest, oepOrderAction));
		addOnPlan.setCUSFLDPLANTYPE("2");
		
		addPlanList.add(addOnPlan);
		
		log.info("OEP Order Number : "+oepOrderNumber+" OEP Order Action : "+oepOrderAction + " Plan Action : Add" + " Plans are set");
		
		String inputXML = (String) template.requestBody("direct:convertToString", updateServiceRequest);
		
		log.info("OEP Order Number : "+ oepOrderNumber + ", OEP Order Action : "+ oepOrderAction +", Plan Action : Add. Addon Plan XML Payload is : \n "+inputXML);

		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_CUST_UPD_SERVICE_OPCODE);
		opcode.setInputXml(inputXML);
		
		//CUSOPCUSTUPDATESERVICEOutputFlist updateServiceResponse = (CUSOPCUSTUPDATESERVICEOutputFlist) template.requestBody("direct:sendUpdateHandsetRequestToBRM",opcode);
		
		 Exchange syncExchange = template.request("direct:sendUpdateHandsetRequestToBRM", new Processor() {
				
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
		
		CUSOPCUSTUPDATESERVICEOutputFlist updateServiceResponse = (CUSOPCUSTUPDATESERVICEOutputFlist) syncExchange.getOut().getBody();
		log.info("OEP Order Number : "+ oepOrderNumber + ", OEP Order Action : "+ oepOrderAction +", Recieved Update Service Response "+template.requestBody("direct:convertToString", updateServiceResponse));
		
		return updateServiceResponse;
		
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
		
		Vector<OEPConfigPlanAttributesT> planAttributesList = (Vector) template.requestBody("direct:queryPlanFromBRM", planMap);
		
		Iterator<OEPConfigPlanAttributesT> iterator = planAttributesList.listIterator();
		
		OEPConfigPlanAttributesT planAttributeObject = null;
		
		while(iterator.hasNext()){
			
			planAttributeObject = iterator.next();
			
		}
		
		log.info("OEP Order Number : "+oepOrderNumber+". Returning for Querying plan attributes");
		return planAttributeObject;
		
	}	
	
	private CUSOPSEARCHOutputFlist getAccountDataFromBRM (final ProducerTemplate template, 
			final String msisdn, final String uimServiceIdentifer, final String channelName, final String userName, final String oepOrderNumber, final String oepServiceType){
	

		if (oepServiceType.equalsIgnoreCase((ServiceTypeEnum.BULK_SMS).value().toString()) || oepServiceType.equalsIgnoreCase((ServiceTypeEnum.SC).toString())) 
			return getAccountDataFromBRMByFlag(template, uimServiceIdentifer, channelName, userName, oepOrderNumber, oepServiceType);
		else
			return getAccountDataFromBRMForMSISDN(template, msisdn, channelName, userName, oepOrderNumber, oepServiceType);
	}
	
	
	private CUSOPSEARCHOutputFlist getAccountDataFromBRMForMSISDN(final ProducerTemplate template, 
			final String msisdn, final String channelName, final String userName, final String oepOrderNumber, final String oepServiceType){
		
		
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
	
	private CUSOPSEARCHOutputFlist getAccountDataFromBRMByFlag(final ProducerTemplate template, 
			final String uimServiceIdentifer, final String channelName, final String userName, final String oepOrderNumber, final String serviceType){
		
		
		CUSOPSEARCHInputFlist opcodeSearch = new CUSOPSEARCHInputFlist();
		PARAMS param = new PARAMS();
		opcodeSearch.setPOID(OEPConstants.BRM_POID);
		opcodeSearch.setPROGRAMNAME(channelName);
		opcodeSearch.setUSERNAME(userName);
		opcodeSearch.setFLAGS(new BigInteger("201"));		
		param.setVALUE(uimServiceIdentifer);
		param.setElem(new BigInteger("0"));		
		opcodeSearch.getPARAMS().add(param);
		
		String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);

		return queryBRMForAccountData(template, inputXML, oepOrderNumber);
		
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
				 
				 log.info("OEP order number : "+oepOrderNumber+". Error while fetching Account data from BRM "+searchResult.getERRORDESCR());
				 throw new RuntimeException(searchResult.getERRORDESCR()); 
				 
				 
			 }
		 
		}
		catch (Exception e) {
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			 log.info("OEP order number : "+oepOrderNumber+". Exception while fetching Account data from BRM "+errorMessage);
			e.printStackTrace();
			
			throw new RuntimeException(e);
		}
		
		
		return searchResult;
		
	}
	
	private CUSOPCUSTINCOMPATIBLEADDONOutputFlist getIncompatibleAddonForMSISDN(final ProducerTemplate template, 
			final String msisdn, final String channelName, final String userName, final String oepOrderNumber,String planname, Byte planType){
		
		
		CUSOPCUSTINCOMPATIBLEADDONInputFlist opcodeSearch = new CUSOPCUSTINCOMPATIBLEADDONInputFlist();
		
		opcodeSearch.setPOID(OEPConstants.BRM_POID_GSM);
		opcodeSearch.setPROGRAMNAME(channelName);
		opcodeSearch.setUSERNAME(userName);
		opcodeSearch.setNAME(planname);
		opcodeSearch.setMSISDN(msisdn);
		opcodeSearch.setCUSFLDPLANTYPE(planType);
		
		String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);
		log.info("inputXML---->"+inputXML);

		return queryBRMForIncompatibleAddon(template, inputXML, oepOrderNumber);
		
	}
	
	private CUSOPCUSTINCOMPATIBLEADDONOutputFlist getIncompatibleAddonForMSISDNPower(final ProducerTemplate template, 
			final String msisdn, final String channelName, final String userName, final String oepOrderNumber,String planname,Byte planType){
		
		
		CUSOPCUSTINCOMPATIBLEADDONInputFlist opcodeSearch = new CUSOPCUSTINCOMPATIBLEADDONInputFlist();
		
		opcodeSearch.setPOID(OEPConstants.BRM_POID_GSM);
		opcodeSearch.setPROGRAMNAME(channelName);
		opcodeSearch.setUSERNAME(userName);
		opcodeSearch.setNAME(planname);
		opcodeSearch.setMSISDN(msisdn);
		opcodeSearch.setCUSFLDPLANTYPE(planType);
		
		String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);
		log.info("inputXML---->"+inputXML);

		return queryBRMForIncompatibleAddon(template, inputXML, oepOrderNumber);
		
	}
	
	private CUSOPCUSTINCOMPATIBLEADDONOutputFlist queryBRMForIncompatibleAddon(final ProducerTemplate template, final String inputXML, final String oepOrderNumber){
		
		CUSOPCUSTINCOMPATIBLEADDONOutputFlist searchResult = null;
		
		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_CUS_INCOMPATIBLEADDON_OPCODE);
		opcode.setInputXml(inputXML);		
		
		try{
		
		 log.info("OEP order number : "+oepOrderNumber+". Calling BRM webservice to fetch Incompatible Addon. Input xml : \n"+inputXML);
		 
		 Exchange syncExchange = template.request("direct:getIncompatibleAddonFromBRM", new Processor() {
				
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
			 
			searchResult = (CUSOPCUSTINCOMPATIBLEADDONOutputFlist) syncExchange.getOut().getBody();
		 
		
		 
		if(searchResult.getERRORCODE()!=null){
			 
			 log.info("OEP order number : "+oepOrderNumber+". Error while fetching Incompatibie Addon from BRM "+searchResult.getERRORDESCR());
			 throw new RuntimeException(searchResult.getERRORDESCR()); 
			 
			 
		 }
		 
		}
		catch (Exception e) {
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			log.info("OEP order number : "+oepOrderNumber+". Exception while fetching Account data from BRM "+errorMessage);
			e.printStackTrace();
			
			throw new RuntimeException(e);
		}
		
		
		return searchResult;
		
	}
	/* 
	 * 
	 * This method generate OepOrder entity bean and OepOrderLine entity bean associate OepOrderLine with OepOrder for Dhiraagu IO catalogue
	 * 
	 */
	private OepOrder getOepOrderDO(final OrderEntryRequest request, final String oepOrderAction, final String oepOrderNumber, final String orderPayload,
			final String basePlanName, final String addOnPlanName, final Map<String,OEPConfigPlanAttributesT> productSpecMap, final RESULTS searchResult, final String oepServiceType, final String channelName ){
		
		log.info("OEP Order Number : "+oepOrderNumber+".Started Initializing Oep Order Entity Bean .Order payload is "+orderPayload);	
		
		
		GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
		
		String csrId = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
		Timestamp orderStartDate = ProcessModifyOrderEbmHelper.getOrderStartDate(request, oepOrderAction);
		String orderActionFromRequest = ProcessModifyOrderEbmHelper.getOrderActionFromRequest(request, oepOrderAction);
		
		
		String accountId= searchResult.getACCOUNTNO();			
		String serviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
		
		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)
				|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)){
			
			serviceNo = ProcessModifyOrderEbmHelper.getOldServiceNo(request, oepOrderAction);
		}
		
		
		log.info("OEP Order Number : "+oepOrderNumber+". Before calling entityBeanGenerator.getOepOrderDO()");
		
		OepOrder oepOrderDO= entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);		
			
		entityBeanGenerator.getOepOrderDO(oepOrderDO, serviceNo, csrId, orderStartDate, oepOrderAction, oepOrderNumber, orderPayload, null, channelName);	
		
		
		List<OepOrderLine> oepOrderLines = new ArrayList<OepOrderLine>();		
		
		log.info("OEP Order Number : "+oepOrderNumber+".Adding base plan,"+basePlanName+" to  Order Entity Bean ");
		
		OepOrderLine basePlanOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, basePlanName, 
				productSpecMap.get(basePlanName).getPs(), ProcessModifyOrderEbmHelper.getPlanAction(request, productSpecMap.get(basePlanName), oepOrderAction), accountId, oepServiceType, null, oepOrderNumber);
		
		basePlanOrderLine.setOepOrder(oepOrderDO);
		basePlanOrderLine.setCommercialPlanName(productSpecMap.get(basePlanName).getCommercialPlanName());
		oepOrderLines.add(basePlanOrderLine);
		
		if(addOnPlanName != null){		
			
				
			log.info("OEP Order Number : "+oepOrderNumber+".Adding base plan,"+addOnPlanName+" to  Order Entity Bean ");
			String planAction = ProcessModifyOrderEbmHelper.getPlanAction(request, productSpecMap.get(addOnPlanName), oepOrderAction);
			
			OepOrderLine addOnOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, addOnPlanName,
					productSpecMap.get(addOnPlanName).getPs(), planAction, accountId, oepServiceType,  basePlanOrderLine.getOrderLineId(), oepOrderNumber);
			
			addOnOrderLine.setOepOrder(oepOrderDO);
			addOnOrderLine.setCommercialPlanName(productSpecMap.get(addOnPlanName).getCommercialPlanName());;
			oepOrderLines.add(addOnOrderLine);
			
			
			
			String addOnPkgSubType = productSpecMap.get(addOnPlanName).getAddonPkgSubType();

			if(addOnPkgSubType != null && !(planAction.equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE)) )
			{
			if(addOnPkgSubType.equalsIgnoreCase("POWER") || addOnPkgSubType.equalsIgnoreCase("POWERPLUS")   || addOnPkgSubType.equalsIgnoreCase("POWERPLUSTBB") || addOnPkgSubType.equalsIgnoreCase("POSTPAID_BOOSTER")
				|| addOnPkgSubType.equalsIgnoreCase("PREPAID_MINI")	
				|| "BB_LTE_PLAN".equalsIgnoreCase(addOnPkgSubType))
			{
				log.info("Incompatible Addon's called,MSISDN--"+serviceNo+"-- Plan Name--"+addOnPlanName);
				
				CUSOPCUSTINCOMPATIBLEADDONOutputFlist incompatibleAddonResult = getIncompatibleAddonForMSISDNPower(template, serviceNo, channelName, csrId, oepOrderNumber,addOnPlanName,(byte) 2);	

				
				log.info("Incompatible Addon's called");

				
				if(incompatibleAddonResult != null && incompatibleAddonResult.getCUSFLDADDONPKG() != null && incompatibleAddonResult.getCUSFLDADDONPKG().size() > 0){
					
										
					Iterator<CUSFLDADDONPKG> incompatibleAddonIterator= incompatibleAddonResult.getCUSFLDADDONPKG().iterator();
					
					while (incompatibleAddonIterator.hasNext()) {
						CUSFLDADDONPKG incompatibleAddon=(CUSFLDADDONPKG)incompatibleAddonIterator.next();
						
						log.info("Incompatible Plan Name"+incompatibleAddon.getCUSFLDADDONPLANNAME());
						log.info("Incompatible Package Id"+incompatibleAddon.getPACKAGEID());
						String fetchedaddOnPlan = incompatibleAddon.getCUSFLDADDONPLANNAME();
						String removeActionAddOnPlan = OEPConstants.OSM_REMOVE_ACTION_CODE;
						
						OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(incompatibleAddon.getCUSFLDADDONPLANNAME(), template,oepOrderNumber);
						productSpecMap.put(incompatibleAddon.getCUSFLDADDONPLANNAME(),planAttrbiutes);
						
						if(productSpecMap.get(incompatibleAddon.getCUSFLDADDONPLANNAME()) == null){
							
							throw new RuntimeException("AddOn Plan "+incompatibleAddon.getCUSFLDADDONPLANNAME()+" not found");
						}
						log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(fetchedaddOnPlan).getPackageType()+""
								+ ", product spec is "+productSpecMap.get(fetchedaddOnPlan).getPs()+", and COS is "+productSpecMap.get(fetchedaddOnPlan).getCos());
						
						log.info("OEP Order Number : "+oepOrderNumber+".Adding addOn plan,"+fetchedaddOnPlan+" to  Order Entity Bean ");
						
						OepOrderLine inCompatibleOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, fetchedaddOnPlan,
								productSpecMap.get(fetchedaddOnPlan).getPs(), removeActionAddOnPlan, accountId, oepServiceType,  basePlanOrderLine.getOrderLineId(), oepOrderNumber);
						
						inCompatibleOrderLine.setOepOrder(oepOrderDO);
						
						oepOrderLines.add(inCompatibleOrderLine);
					}
					
					//log.info("Incompatible Addon Name"+incompatibleAddonResult.getCUSFLDADDONPKG().get);
					
				}

				
			}
			}
				
			
		}
		
		oepOrderDO.setOepOrderLines(oepOrderLines);
		
		log.info("OEP Order Number : "+oepOrderNumber+".Oep Order Entity Bean is set");
		
		return oepOrderDO;
		
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
	private void sendSalesOrderEBM(final CreateOrder salesOrder,final ProducerTemplate template, final String oepOrderNumber,String orderAction) throws SOAPException{
		
		
		Document soapBodyDoc = (Document) template.requestBody("direct:convertToDocument", salesOrder);	
		SOAPMessage soapMessage = OrderDataUtils.createSOAPRequest(soapBodyDoc, oepOrderNumber, template);
		
		Map<String, Object> orderHeaders = new HashMap<String,Object>();
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_URI, OEPConstants.OSM_WEBSERVICE_URI);
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_WLS_CONTENT_TYPE, OEPConstants.OSM_WEBSERVICE_CONTETNT_TYPE);
		
		if(orderAction != null && orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
			
			template.sendBodyAndHeaders("direct:sendSalesOrderEBMToOSMWithPriority", soapMessage.getSOAPPart().getEnvelope(),
					orderHeaders);
			
		}else{
		
		template.sendBodyAndHeaders("direct:sendSalesOrderEBMToOSM", soapMessage.getSOAPPart().getEnvelope(),orderHeaders);
		}
		
		
	}
	
	/*
	 * 
	 * This is the helper method to create SOAP request from supplied SoapBodyDoc.
	 * 
	 * */
	 private SOAPMessage createSOAPRequest(Document soapBodyDoc,String oepOrderNumber) throws SOAPException{
       
		 MessageFactory messageFactory = null;
		 SOAPMessage soapMessage=null;
		
		
		messageFactory = MessageFactory.newInstance();
        soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();	

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ord", "http://xmlns.oracle.com/communications/ordermanagement");
        

        javax.xml.soap.SOAPBody soapBody = envelope.getBody();
        soapBody.addDocument(soapBodyDoc);
        
        log.info("OEP Order Number : "+oepOrderNumber+". SOAP Body is set. ");
        
        SOAPHeader header = envelope.getHeader(); 
        
        SOAPElement securityElement= header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        securityElement.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
        QName mustUnderstand = new QName("mustUnderstand");
        securityElement.addAttribute(mustUnderstand, "1");
        
        SOAPElement usernameToken = securityElement.addChildElement("UsernameToken", "wsse");
        usernameToken.addAttribute(new QName("wsu:Id"), "UsernameToken-8514627E9D7C3AD6E814914032601551");
        
        
        SOAPElement username = usernameToken.addChildElement("Username", "wsse");
        username.addTextNode("admin");
        
        SOAPElement password = usernameToken.addChildElement("Password","wsse");
        password.addTextNode("c1g2b3u4");
        
        password.addAttribute(new QName("Type"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        
        log.info("OEP Order Number : "+oepOrderNumber+". SOAP Headers are set. ");
        
        soapMessage.saveChanges();       
		

        return soapMessage;
    }
	
	 /**
		 * Send Managed CUG Services orders to OSM.
		 * @param request
		 * @param exchange
		 * @param oepOrderAction
		 * @param orderPayload
		 * @param oepOrderNumber
		 */
		public void processMobileServiceManagedCugServices(OrderEntryRequest request, Exchange exchange, String oepOrderAction,
				String orderPayload, String oepOrderNumber, String oepServiceType) {
			
			log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing ");
			log.info("OEP order number : "+oepOrderNumber+" .Order Payload :\n "+orderPayload);
			log.info("CUG Action : "+request.getProcessCugOrder().getCugAction()
					+" CUG Id : "+request.getProcessCugOrder().getCugId()
					+" Service Number : "+request.getProcessCugOrder().getServiceNumber()
					+" Short Number Action : "+request.getProcessCugOrder().getShortNumberAction()
					+" Short Number : "+request.getProcessCugOrder().getShortNumber());
			
			
			String serviceNumber = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
			String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, oepOrderAction);
			String userName = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
			String uimServiceIdentifier = ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);
			
			String basePlan = null;
			OepOrder oepOrderDO = null;
			
			template = exchange.getContext().createProducerTemplate();
			
			//Query BRM for Account details
			CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRM(template, serviceNumber, uimServiceIdentifier, channelName, userName, oepOrderNumber, oepServiceType);	
			
			RESULTS result = getActiveServiceFromSearchResult(searchResult);
			
			if(result == null){
				
				throw new RuntimeException("No Active Service is Found in BRM");
			}
			
			basePlan = result.getCUSFLDPLANNAME();
			
			Map<String,OEPConfigPlanAttributesT> productSpecMap = new HashMap<String,OEPConfigPlanAttributesT>();
			
			//query BasePlan Details and Add to product spec
			productSpecMap.put(basePlan, queryBRMForPlanAttributes(basePlan, template, oepOrderNumber));		
			log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(basePlan).getPackageType()+""
					+ ", product spec is "+productSpecMap.get(basePlan).getPs()+", and COS is "+productSpecMap.get(basePlan).getCos());
			
			oepOrderDO = getOepOrderDOForMobileCugServices(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, productSpecMap, result, oepServiceType, channelName, userName );
			oepOrderDO.setAction(OEPConstants.OEP_MOBILE_SERVICE_CUG + "-" +request.getProcessCugOrder().getCugAction());
			oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_N);
			oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_N);
			oepOrderDO.setBaAccountNo(result.getCUSFLDBAACCOUNTNO());

			ProcessModifyOrderEbmMapping addOrderEbmMapping = new ProcessModifyOrderEbmMapping();
			CreateOrder salesOrderEbm = addOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap, result,template);						
				
			log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
			
			BulkBatchMaster master= new BulkBatchMaster();
			master.setBatchId(OEPConstants.OEP_BATCH_ID_DUMMY_VALUE);
			oepOrderDO.setBulkBatchMaster(master);	
			
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			//Save Order Entity to DB			
			saveOepOrderDO(oepOrderDO, template, oepOrderNumber);
			log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
			
			try {
				sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber,oepOrderAction);
				log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
			} catch (SOAPException e) {
				log.error("Error send sales order to OSM.",e);
				e.printStackTrace();
			}
			
		}
		
		
		/**
		 * This method generate OepOrder entity bean and OepOrderLine entity bean associate OepOrderLine with OepOrder for Dhiraagu IO catalogue
		 * @param request
		 * @param oepOrderAction
		 * @param oepOrderNumber
		 * @param orderPayload
		 * @param basePlanName
		 * @param addOnPlanName
		 * @param productSpecMap
		 * @param searchResult
		 * @return
		 */
		private OepOrder getOepOrderDOForMobileCugServices(final OrderEntryRequest request, final String oepOrderAction, final String oepOrderNumber, final String orderPayload,
				final String basePlanName, final Map<String,OEPConfigPlanAttributesT> productSpecMap, final RESULTS searchResult , final String oepServiceType, final String channelName, String csrId){
			
			log.info("OEP Order Number : "+oepOrderNumber+".Started Initializing Oep Order Entity Bean .Order payload is "+orderPayload);	
			
			GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
			
			Timestamp orderStartDate = ProcessModifyOrderEbmHelper.getOrderStartDate(request, oepOrderAction);			
			
			String accountId= searchResult.getACCOUNTNO();					
			String serviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
			
			
			log.info("OEP Order Number : "+oepOrderNumber+". Before calling entityBeanGenerator.getOepOrderDO()");
			
			OepOrder oepOrderDO= entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);			
				
			entityBeanGenerator.getOepOrderDO(oepOrderDO, serviceNo, csrId, orderStartDate, oepOrderAction, oepOrderNumber, orderPayload, null, channelName);	
			
			List<OepOrderLine> oepOrderLines = new ArrayList<OepOrderLine>();		
			
			
			// Base Plan
			log.info("OEP Order Number : "+oepOrderNumber+".Adding base plan,"+basePlanName+" to  Order Entity Bean ");
			OepOrderLine basePlanOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, basePlanName, 
					productSpecMap.get(basePlanName).getPs(), request.getProcessCugOrder().getCugAction(), accountId, oepServiceType, null, oepOrderNumber);
			log.info("Base Plan Action ----------------------- "+basePlanOrderLine.getPlanAction());
			// Set Modify action as a default to Service Action Code.
			basePlanOrderLine.setPlanAction(OEPConstants.OEP_MOBILE_SERVICE_CUG_SERVICE_ACTION_CODE_MODIFY);
			basePlanOrderLine.setOepOrder(oepOrderDO);
			oepOrderLines.add(basePlanOrderLine);
			
			oepOrderDO.setOepOrderLines(oepOrderLines);
			
			log.info("OEP Order Number : "+oepOrderNumber+".Oep Order Entity Bean is set");
			
			return oepOrderDO;
			
		}

		/**
		 * Send FCA Mobile Service orders to OSM.
		 * @param request
		 * @param exchange
		 * @param oepOrderAction
		 * @param orderPayload
		 * @param oepOrderNumber
		 */
		public void processFcaMobileService(OrderEntryRequest request, Exchange exchange, String oepOrderAction,
				String orderPayload, String oepOrderNumber) {
			
			log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing ");
			log.info("OEP order number : "+oepOrderNumber+" .Order Payload :\n "+orderPayload);
			
			String serviceIdentifier = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
			String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, oepOrderAction);
			String userName = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
			String oepServiceType = ProcessModifyOrderEbmHelper.getOrderServiceType(request, oepOrderAction);
			
			String basePlan = null;
			OepOrder oepOrderDO = null;
			
			template = exchange.getContext().createProducerTemplate();
			
			//Query BRM for Account details
			CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRMForMSISDN(template, serviceIdentifier, channelName, userName, oepOrderNumber, oepServiceType);	
			
			RESULTS result = getActiveServiceFromSearchResult(searchResult);
			
			if(result == null){
				
				throw new RuntimeException("No Active Service is Found in BRM");
			}
			
			
			//RESULTS result = searchResult.getRESULTS().get(0);
			
			basePlan = result.getCUSFLDPLANNAME();
			
			Map<String,OEPConfigPlanAttributesT> productSpecMap = new HashMap<String,OEPConfigPlanAttributesT>();
			
			//query BasePlan Details and Add to product spec
			productSpecMap.put(basePlan, queryBRMForPlanAttributes(basePlan, template, oepOrderNumber));		
			log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(basePlan).getPackageType()+""
					+ ", product spec is "+productSpecMap.get(basePlan).getPs()+", and COS is "+productSpecMap.get(basePlan).getCos());
			
			oepOrderDO = getOepOrderDOForFcaMobileServices(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, productSpecMap, searchResult, channelName);
			oepOrderDO.setAction(OEPConstants.OEP_FCA_MOBILE_ACTION);
			oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_N);
			oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_N);
			oepOrderDO.setBaAccountNo(result.getCUSFLDBAACCOUNTNO());

			ProcessModifyOrderEbmMapping addOrderEbmMapping = new ProcessModifyOrderEbmMapping();
			CreateOrder salesOrderEbm = addOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap, result,template);						
				
			log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
			
			BulkBatchMaster master= new BulkBatchMaster();
			master.setBatchId(OEPConstants.OEP_BATCH_ID_DUMMY_VALUE);
			oepOrderDO.setBulkBatchMaster(master);	
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			//Save Order Entity to DB			
			saveOepOrderDO(oepOrderDO, template, oepOrderNumber);
			log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
			
			try {
				sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber,oepOrderAction);
				log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
			} catch (SOAPException e) {
				log.error("Error send sales order to OSM.",e);
				e.printStackTrace();
			}
			
		}
		
		
		/**
		 * This method generate OepOrder entity bean and OepOrderLine entity bean associate OepOrderLine with OepOrder for Dhiraagu IO catalogue
		 * @param request
		 * @param oepOrderAction
		 * @param oepOrderNumber
		 * @param orderPayload
		 * @param basePlanName
		 * @param addOnPlanName
		 * @param productSpecMap
		 * @param searchResult
		 * @return
		 */
		private OepOrder getOepOrderDOForFcaMobileServices(final OrderEntryRequest request, final String oepOrderAction, final String oepOrderNumber, final String orderPayload,
				final String basePlanName, final Map<String,OEPConfigPlanAttributesT> productSpecMap, final CUSOPSEARCHOutputFlist searchResult, final String channelName ){
			
			log.info("OEP Order Number : "+oepOrderNumber+".Started Initializing Oep Order Entity Bean .Order payload is "+orderPayload);	
			
			GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
			
			String csrId = "CSR";
			Timestamp orderStartDate = ProcessModifyOrderEbmHelper.getOrderStartDate(request, oepOrderAction);
			
			RESULTS result = searchResult.getRESULTS().get(0);
			String accountId= result.getACCOUNTNO();		
			String serviceType = ProcessModifyOrderEbmHelper.getOrderServiceType(request, oepOrderAction);		
			String serviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
			
			
			log.info("OEP Order Number : "+oepOrderNumber+". Before calling entityBeanGenerator.getOepOrderDO()");
			
			OepOrder oepOrderDO= entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);
				
			entityBeanGenerator.getOepOrderDO(oepOrderDO, serviceNo, csrId, orderStartDate, oepOrderAction, oepOrderNumber, orderPayload, null, channelName);	
			
			List<OepOrderLine> oepOrderLines = new ArrayList<OepOrderLine>();		
			
			
			// Base Plan
			log.info("OEP Order Number : "+oepOrderNumber+".Adding base plan,"+basePlanName+" to  Order Entity Bean ");
			OepOrderLine basePlanOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, basePlanName, 
					productSpecMap.get(basePlanName).getPs(), oepOrderAction, accountId, serviceType, null, oepOrderNumber);
			log.info("Base Plan Action ----------------------- "+basePlanOrderLine.getPlanAction());
			// Set Modify action as a default to Service Action Code.
			basePlanOrderLine.setPlanAction(OEPConstants.OSM_MODIFY_ACTION_CODE);
			basePlanOrderLine.setOepOrder(oepOrderDO);
			oepOrderLines.add(basePlanOrderLine);
			
			oepOrderDO.setOepOrderLines(oepOrderLines);
			
			log.info("OEP Order Number : "+oepOrderNumber+".Oep Order Entity Bean is set");
			
			return oepOrderDO;
			
		}
		
		/*
		 * This method is for changedetail in Bulk SMS
		 * 
		 * 
		 */
		
		private void processChangeDetailForBulkSMSOrder(OrderEntryRequest request, Exchange exchange, String oepOrderAction,
				String orderPayload, String oepOrderNumber, Map<String, OEPConfigPlanAttributesT> productSpecMap, String oepServiceType) throws SOAPException {
			
			log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing ");
			log.info("OEP order number : "+oepOrderNumber+" with servicetype "+oepServiceType);
			log.info("OEP order number : "+oepOrderNumber+" .Order Payload :\n "+orderPayload);
			
			String serviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);		
			String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, oepOrderAction);
			String userName = ProcessModifyOrderEbmHelper.getCsrId(request, oepOrderAction);
			String accountId = ProcessModifyOrderEbmHelper.getAccountId(request, oepOrderAction);
			String serviceId = ProcessModifyOrderEbmHelper.getServiceNo(request, oepOrderAction);
			String uimServiceIdentifier = ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);
			
			String basePlan = null;
			String billingAccountId = null;
			String customerAccountId = null;
			OepOrder oepOrderDO = null;
			
			
			template = exchange.getContext().createProducerTemplate();
			
			//Query BRM for Account details
			CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRM(template, serviceNo, uimServiceIdentifier, channelName, userName, oepOrderNumber, oepServiceType);	
			
			RESULTS result = getActiveServiceFromSearchResult(searchResult);
			
			if(result == null){
				
				throw new RuntimeException("No Active Service is Found in BRM");
			}
			
			
			
			if (oepOrderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)) {
//				basePlan = result.getPLAN().getNAME();
				
				Iterator<RESULTS.PLAN> incompatibleAddonIterator= result.getPLAN().iterator();
				
				while (incompatibleAddonIterator.hasNext()) {
					RESULTS.PLAN allplanList = (RESULTS.PLAN)incompatibleAddonIterator.next();
					Byte planType = allplanList.getCUSFLDPLANTYPE();
					if(planType.equals((byte)1))
					{
					basePlan = allplanList.getNAME();
					}
				}
			}
			else
			{
				basePlan = result.getCUSFLDPLANNAME();
				
			}
			

			accountId = result.getACCOUNTNO();
			serviceId = result.getSERVICEID();
			billingAccountId = result.getCUSFLDBAACCOUNTNO();
			customerAccountId = result.getCUSFLDCAACCOUNTNO();
			
			log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" after BRM Query " +
					"basePlan :" + basePlan +",:" + "accountId :" + accountId + ",:" + "serviceId :" + serviceId + ",:"  + "billingAccountId :"
					+ billingAccountId + ",:" + "customerAccountId :" + customerAccountId);
					
			OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(basePlan, template, oepOrderNumber);
	              
			productSpecMap.put(basePlan, planAttrbiutes);

			
			oepOrderDO = getOepOrderDO(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, null, productSpecMap, result, oepServiceType, channelName);	
			oepOrderDO.setBaAccountNo(billingAccountId);
			
			ProcessModifyOrderEbmMapping addOrderEbmMapping = new ProcessModifyOrderEbmMapping();
			CreateOrder salesOrderEbm = addOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap, result,template);						
			
			log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
			
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			//Save Order Entiry to DB			
			saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
			
			log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
			
			sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber,oepOrderAction);
			
			log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
		}
		
		
		/**
		 * This method is used to generate notification for Add plan and Cancel plan notifications.
		 * 
		 * @param oepOrderDO
		 * 
		 */
		private void generatePlanManagementNotification(final OepOrder oepOrderDO, final ProducerTemplate template) {
			
			List<OepOrderLine> orderLines  = oepOrderDO.getOepOrderLines();
			
			for (OepOrderLine orderLine : orderLines){
				
				if(orderLine.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_ADD_ACTION_CODE)){
					
					NotifAddOnSubscription addonEvent = new NotifAddOnSubscription();
					
					addonEvent.setAccountNo(oepOrderDO.getBaAccountNo());
					addonEvent.setAccountObj(OEPConstants.BRM_ACCOUNT_OBJ);
					addonEvent.setServiceNo(Long.parseLong(oepOrderDO.getServiceNo()));
					addonEvent.setEventDescr("Addon Subscription");
					addonEvent.setChannel(oepOrderDO.getChannelName());
					addonEvent.setMessage("You have successfully subscribed: "+orderLine.getCommercialPlanName());
					
					NotifAddOnSubscription.NotifAddOnSubscriptionEvents  susbscription = new NotifAddOnSubscription.NotifAddOnSubscriptionEvents();
					susbscription.setAddOnPlanName(orderLine.getCommercialPlanName());
					
					addonEvent.setNotifAddOnSubscriptionEvents(susbscription);
					
					
					String eventPayload = (String) template.requestBody("direct:convertToString",addonEvent);
					
					log.info("OEP Order "+ oepOrderDO.getOrderId()+". Generate payload : \n"+eventPayload);
					
					Map<String,Object> headers = new HashMap<String, Object>();
					headers.put("CHANNEL_NAME","NOTIF/SMS");		
					headers.put("JMSCorrelationID", "NotifAddOnSubscription");
					headers.put("NOTIFICATION_SOURCE", "OEP");
					
					template.sendBodyAndHeaders("direct:sendEventNotificationsToNotif", eventPayload, headers);
					
					log.info("OEP Order "+ oepOrderDO.getOrderId()+". Event sent to Notif Queue");
					
				}
				 
				else if(orderLine.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE)){
					
					NotifCancelAddOnSubscription cancelAddonEvent = new NotifCancelAddOnSubscription();
					cancelAddonEvent.setAccountNo(oepOrderDO.getBaAccountNo());
					cancelAddonEvent.setAccountObj(OEPConstants.BRM_ACCOUNT_OBJ);
					cancelAddonEvent.setServiceNo(Long.parseLong(oepOrderDO.getServiceNo()));
					cancelAddonEvent.setEventDescr("Addon Subscription Cancellation");
					cancelAddonEvent.setChannel(oepOrderDO.getChannelName());
					cancelAddonEvent.setMessage("You have successfully cancelled: "+orderLine.getCommercialPlanName());
					
					NotifCancelAddOnSubscription.NotifCancelAddOnSubscriptionEvents  susbscription = new NotifCancelAddOnSubscription.NotifCancelAddOnSubscriptionEvents();
					susbscription.setPlanName(orderLine.getCommercialPlanName());
					
					cancelAddonEvent.setNotifCancelAddOnSubscriptionEvents(susbscription);
					
					
					String eventPayload = (String) template.requestBody("direct:convertToString",cancelAddonEvent);
					
					log.info("OEP Order "+ oepOrderDO.getOrderId()+". Generate payload : \n"+eventPayload);
					
					Map<String,Object> headers = new HashMap<String, Object>();
					headers.put("CHANNEL_NAME","NOTIF/SMS");		
					headers.put("JMSCorrelationID", "NotifCancelAddOnSubscription");
					headers.put("NOTIFICATION_SOURCE", "OEP");
					
					template.sendBodyAndHeaders("direct:sendEventNotificationsToNotif", eventPayload, headers);
					
					log.info("OEP Order "+ oepOrderDO.getOrderId()+". Event sent to Notif Queue");
					
				}
			}
			
			
		}
		
		/**
		 * This method is used to generate Failure notification for Add plan and cancel plan failure for non OAP channels
		 * @param oepOrderDO 
		 * 
		 * @param oepOrderDO2
		 * @param template2
		 */
		private void sendFailureNotification(OepOrder oepOrderDO) {
			// TODO Auto-generated method stub
			
			List<OepOrderLine> orderLines  = oepOrderDO.getOepOrderLines();
			
			for (OepOrderLine orderLine : orderLines){
				
				 if(orderLine.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE)){
					
					NotifCancelAddOnSubscriptionFailure cancelAddonFailure = new NotifCancelAddOnSubscriptionFailure();
					cancelAddonFailure.setAccountNo(oepOrderDO.getBaAccountNo());
					cancelAddonFailure.setServiceNo(Long.parseLong(oepOrderDO.getServiceNo()));
					cancelAddonFailure.setEventDescr("Cancel Addon Subscription Failure");
					cancelAddonFailure.setChannel("OEP");
					
					cancelAddonFailure.setFailureCode("DEFAULT");
					
					
					String eventPayload =(String) template.requestBody("direct:convertToString",cancelAddonFailure);	

					
					log.info("OEP Order "+ oepOrderDO.getOrderId()+". Generate payload : \n"+eventPayload);
					
					Map<String,Object> headers = new HashMap<String, Object>();
					headers.put("CHANNEL_NAME","SMS");		
					headers.put("JMSCorrelationID", OEPConstants.NOTIF_CANCEL_ADDON_FAILURE);
					headers.put("NOTIFICATION_SOURCE", "OEP");
					
					template.sendBodyAndHeaders("direct:sendEventNotificationsToNotif", eventPayload, headers);
					
					log.info("OEP Order "+ oepOrderDO.getOrderId()+". Event sent to Notif Queue");
						
				}
					
				else if(orderLine.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_ADD_ACTION_CODE)){
					
					NotifAddOnSubscriptionFailure addAddonFailure = new NotifAddOnSubscriptionFailure();
					addAddonFailure.setAccountNo(oepOrderDO.getBaAccountNo());
					addAddonFailure.setServiceNo(Long.parseLong(oepOrderDO.getServiceNo()));
					addAddonFailure.setEventDescr("Addon Subscription Failure");
					addAddonFailure.setChannel("OEP");
					addAddonFailure.setFailureCode("DEFAULT");				
	
					String eventPayload =(String) template.requestBody("direct:convertToString",addAddonFailure);	

					
					log.info("OEP Order "+ oepOrderDO.getOrderId()+". Generate payload : \n"+eventPayload);
					
					Map<String,Object> headers = new HashMap<String, Object>();
					headers.put("CHANNEL_NAME","SMS");		
					headers.put("JMSCorrelationID", OEPConstants.NOTIF_ADD_ADDON_FAILURE);
					headers.put("NOTIFICATION_SOURCE", "OEP");
					
					template.sendBodyAndHeaders("direct:sendEventNotificationsToNotif", eventPayload, headers);
					
					log.info("OEP Order "+ oepOrderDO.getOrderId()+". Event sent to Notif Queue");
					
					
				}
				
				
			}
				
		}

}
