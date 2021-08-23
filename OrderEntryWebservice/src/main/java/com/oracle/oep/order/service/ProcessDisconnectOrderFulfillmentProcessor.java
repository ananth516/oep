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
import com.oracle.oep.brm.opcodes.CUSOPSEARCHInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.PARAMS;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.ebm.transform.ProcessDisconnectOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessDisconnectOrderEbmMapping;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ServiceTypeEnum;
import com.oracle.oep.order.presistence.model.BulkBatchMaster;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.CreateOrder;


public class ProcessDisconnectOrderFulfillmentProcessor {
	
	public static final Logger log = LogManager.getLogger(ProcessDisconnectOrderFulfillmentProcessor.class);
	ProducerTemplate template = null;
	OepOrder oepOrderDO = null;
	
	
	public void processDisconnectOrder(final OrderEntryRequest request,final Exchange exchange, final String oepOrderAction, final String orderPayload, final String oepOrderNumber, final String oepServiceType) throws Exception {
		
		try{
			
			Map<String,OEPConfigPlanAttributesT> productSpecMap = new HashMap<String,OEPConfigPlanAttributesT>();
			
	
			log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing ");
			log.info("OEP order number : "+oepOrderNumber+" .Order Payload :\n "+orderPayload);
			
			String serviceNo = ProcessDisconnectOrderEbmHelper.getServiceNo(request, oepOrderAction);
			String uimServiceIdentifier = ProcessDisconnectOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);
			String channelName = ProcessDisconnectOrderEbmHelper.getChannelName(request, oepOrderAction);
			String channelTrackingId = ProcessDisconnectOrderEbmHelper.getChannelTrackingId(request, oepOrderAction);
			String planAction = ProcessDisconnectOrderEbmHelper.getPlanAction(request, oepOrderAction);
			String userName = ProcessDisconnectOrderEbmHelper.getCsrId(request, oepOrderAction);	
			
			
			
			String basePlan = null;
			String accountId = null;
			String serviceId = null;
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
			
			if (oepServiceType.equalsIgnoreCase((ServiceTypeEnum.BULK_SMS).value().toString()) 
					|| oepServiceType.equalsIgnoreCase((ServiceTypeEnum.SC).toString())) {
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
				
			} else 	{
				basePlan = result.getCUSFLDPLANNAME();
			}
			
			//basePlan = result.getCUSFLDPLANNAME();
			accountId = result.getACCOUNTNO();
			serviceId = result.getSERVICEID();
			billingAccountId = result.getCUSFLDBAACCOUNTNO();
			customerAccountId = result.getCUSFLDCAACCOUNTNO();
			
			//query BasePlan Details and Add to product spec
			productSpecMap.put(basePlan, queryBRMForPlanAttributes(basePlan, template, oepOrderNumber));		
			log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(basePlan).getPackageType()+""
					+ ", product spec is "+productSpecMap.get(basePlan).getPs()+", and COS is "+productSpecMap.get(basePlan).getCos());		
				
			
			oepOrderDO = getOepOrderDO(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, null, productSpecMap, searchResult, oepServiceType,channelName);
			oepOrderDO.setBaAccountNo(billingAccountId); 
			
			ProcessDisconnectOrderEbmMapping disconnectEbmMapping = new ProcessDisconnectOrderEbmMapping();
			CreateOrder salesOrderEbm = disconnectEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap, result);						
			
			log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
			
			BulkBatchMaster master= new BulkBatchMaster();
			master.setBatchId(OEPConstants.OEP_BATCH_ID_DUMMY_VALUE);
			
			oepOrderDO.setBulkBatchMaster(master);
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			
			if(oepServiceType.equalsIgnoreCase(ServiceTypeEnum.MOBILE.toString()))
			{
				if((request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getOrderName().equals(OEPConstants.BRM_PREPAID_LIFECYCLE_TERMINATE_SERVICE_ACTION)
						||OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION.equals(oepOrderAction))&& request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder()
						.getMobileService().getServiceDetails()
						.getOrderStartDate().toGregorianCalendar().getTime()
						.after(new Date())) {
					
						log.info("Order parked to execute on future date");
						oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_Y);
						oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_Y);
						//Save Order Entiry to DB			
						saveOepOrderDO(oepOrderDO,template, oepOrderNumber);			
						log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
							
						
					} 
				else {
						oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_N);
						oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_N);
						//Save Order Entiry to DB			
						saveOepOrderDO(oepOrderDO,template, oepOrderNumber);			
						log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
							
						sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber);
						log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
						
					}
							
			}
			else
			{
				oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_N);
				oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_N);
				//Save Order Entiry to DB			
				saveOepOrderDO(oepOrderDO,template, oepOrderNumber);			
				log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
					
				sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber);
				log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
				
			}
			
						
		}
		catch(Exception e){
			
			e.printStackTrace();
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			errorMessage = errorMessage!=null?(errorMessage.length()>200?errorMessage.substring(0, 200):errorMessage) : null;
			log.error("OEP Order number "+oepOrderNumber+". Exception while processing Disconnect Order. "+errorMessage);
			
			GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
			oepOrderDO = entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);
			
			oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_FAILED);
			oepOrderDO.setStatusDescription(errorMessage);
			
			saveOepOrderDO(oepOrderDO, template, oepOrderNumber);	
			
			throw e;
			
		}
				
		
	}
	
	
	public void processRemoveServiceOrder(final OrderEntryRequest request,final Exchange exchange, final String oepOrderAction, final String orderPayload, final String oepOrderNumber, final String oepServiceType) throws Exception {
		
		try{
			
			Map<String,OEPConfigPlanAttributesT> productSpecMap = new HashMap<String,OEPConfigPlanAttributesT>();
			
	
			log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing ");
			log.info("OEP order number : "+oepOrderNumber+" .Order Payload :\n "+orderPayload);
			
			String serviceNo = ProcessDisconnectOrderEbmHelper.getServiceNo(request, oepOrderAction);
			String uimServiceIdentifier = ProcessDisconnectOrderEbmHelper.getUIMServiceIdentifier(request, oepOrderAction);
			String channelName = ProcessDisconnectOrderEbmHelper.getChannelName(request, oepOrderAction);
			String channelTrackingId = ProcessDisconnectOrderEbmHelper.getChannelTrackingId(request, oepOrderAction);
			String planAction = ProcessDisconnectOrderEbmHelper.getPlanAction(request, oepOrderAction);
			String userName = ProcessDisconnectOrderEbmHelper.getCsrId(request, oepOrderAction);	
			String basePlan = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getBasePackageName();
			String accountId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getAccountNo();
			String serviceId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getServiceIdentifier();
			OepOrder oepOrderDO = null;
			
						
			
			template = exchange.getContext().createProducerTemplate();
			
			log.info("OEP order number : "+oepOrderNumber+". package name is "+basePlan);		
				
			//query BasePlan Details and Add to product spec
			productSpecMap.put(basePlan, queryBRMForPlanAttributes(basePlan, template, oepOrderNumber));		
			log.info("OEP order number : "+oepOrderNumber+". Fetched package type is "+productSpecMap.get(basePlan).getPackageType()+""
					+ ", product spec is "+productSpecMap.get(basePlan).getPs()+", and COS is "+productSpecMap.get(basePlan).getCos());	
			oepOrderDO = getOepOrderDO(request, oepOrderAction, oepOrderNumber, orderPayload, basePlan, null, productSpecMap, null, oepServiceType, channelName);
			 
			ProcessDisconnectOrderEbmMapping disconnectEbmMapping = new ProcessDisconnectOrderEbmMapping();
			CreateOrder salesOrderEbm = disconnectEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap, null);						
			
			log.info("OEP Order Number : "+oepOrderNumber+".Generated OepOrder Entity Bean. Generating EBM");
			
			BulkBatchMaster master= new BulkBatchMaster();
			master.setBatchId(OEPConstants.OEP_BATCH_ID_DUMMY_VALUE);
			
			oepOrderDO.setBulkBatchMaster(master);
			
			if(oepServiceType.equalsIgnoreCase(ServiceTypeEnum.MOBILE.toString()))
			{
				if(request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getOrderName().equals(OEPConstants.BRM_PREPAID_LIFECYCLE_TERMINATE_SERVICE_ACTION)
						&& request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder()
						.getMobileService().getServiceDetails()
						.getOrderStartDate().toGregorianCalendar().getTime()
						.after(new Date())) {
					
						log.info("Order parked to execute on future date");
						oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_Y);
						oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_Y);
						oepOrderDO.setStatusDescription(OEPConstants.OEP_ORDER_CREATED_STATUS_DESCRIPTION);
						//Save Order Entiry to DB			
						saveOepOrderDO(oepOrderDO,template, oepOrderNumber);			
						log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
										
					} 
				else {
						oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_N);
						oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_N);
						//Save Order Entiry to DB			
						saveOepOrderDO(oepOrderDO,template, oepOrderNumber);			
						log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted"+(String) template.requestBody("direct:convertToString", salesOrderEbm));
							
						sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber);
						log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
						
					}
			}
			else
			{
				oepOrderDO.setIsPark(OEPConstants.OEP_IS_PARK_N);
				oepOrderDO.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_N);
				//Save Order Entiry to DB			
				saveOepOrderDO(oepOrderDO,template, oepOrderNumber);				
				log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted"+(String) template.requestBody("direct:convertToString", salesOrderEbm));
								
				sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber);
				log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
			}			
		}
		catch(Exception e){
			
			e.printStackTrace();
			log.info("OEP Order number "+oepOrderNumber+". Exception while processing Disconnect Order. "+e.getMessage());
			
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
	
	/* 
	 * 
	 * This method generate OepOrder entity bean and OepOrderLine entity bean associate OepOrderLine with OepOrder for Dhiraagu IO catalogue
	 * 
	 */
	private OepOrder getOepOrderDO(final OrderEntryRequest request, final String oepOrderAction, final String oepOrderNumber, final String orderPayload,
			final String basePlanName, final String addOnPlanName, final Map<String,OEPConfigPlanAttributesT> productSpecMap, final CUSOPSEARCHOutputFlist searchResult, final String oepServiceType, final String channelName ){
		
		log.info("OEP Order Number : "+oepOrderNumber+".Started Initializing Oep Order Entity Bean .Order payload is "+orderPayload);	
		
		
		GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
		String accountId = null; 
		String csrId = ProcessDisconnectOrderEbmHelper.getCsrId(request, oepOrderAction);
		Timestamp orderStartDate = ProcessDisconnectOrderEbmHelper.getOrderStartDate(request, oepOrderAction);
		String orderActionFromRequest = ProcessDisconnectOrderEbmHelper.getOrderActionFromRequest(request, oepOrderAction);
		if (oepOrderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION)) {
			accountId = ProcessDisconnectOrderEbmHelper.getAccountName(request, oepOrderAction, null);
		} else {
		RESULTS result = searchResult.getRESULTS().get(0);
		accountId= result.getACCOUNTNO();		
		}
		String serviceNo = ProcessDisconnectOrderEbmHelper.getServiceNo(request, oepOrderAction);
		
		
		log.info("OEP Order Number : "+oepOrderNumber+". Before calling entityBeanGenerator.getOepOrderDO()");
		
		oepOrderDO= entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);	
			
		entityBeanGenerator.getOepOrderDO(oepOrderDO, serviceNo, csrId, orderStartDate, oepOrderAction, oepOrderNumber, orderPayload, null, channelName);	
		
		
		List<OepOrderLine> oepOrderLines = new ArrayList<OepOrderLine>();		
		
		log.info("OEP Order Number : "+oepOrderNumber+".Adding base plan,"+basePlanName+" to  Order Entity Bean ");
		
		OepOrderLine basePlanOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction, basePlanName, 
				productSpecMap.get(basePlanName).getPs(), ProcessDisconnectOrderEbmHelper.getPlanAction(request, oepOrderAction), accountId, oepServiceType, null, oepOrderNumber);
		
		basePlanOrderLine.setOepOrder(oepOrderDO);
		oepOrderLines.add(basePlanOrderLine);		
		
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
	private void sendSalesOrderEBM(final CreateOrder salesOrder,final ProducerTemplate template, final String oepOrderNumber) throws SOAPException{
		
		
		Document soapBodyDoc = (Document) template.requestBody("direct:convertToDocument", salesOrder);	
		SOAPMessage soapMessage = OrderDataUtils.createSOAPRequest(soapBodyDoc, oepOrderNumber, template);
		
		Map<String, Object> orderHeaders = new HashMap<String,Object>();
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_URI, OEPConstants.OSM_WEBSERVICE_URI);
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_WLS_CONTENT_TYPE, OEPConstants.OSM_WEBSERVICE_CONTETNT_TYPE);		
		
		template.sendBodyAndHeaders("direct:sendSalesOrderEBMToOSM", soapMessage.getSOAPPart().getEnvelope(),orderHeaders);
		
		
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
	

}
