package com.oracle.oep.order.server.processors;

import java.math.BigInteger;
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
import com.oracle.oep.brm.opcodes.CUSOPCCUPDATESERVICEInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCCUPDATESERVICEOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.PARAMS;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.brm.opcodes.SERVICES;
import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.ebm.transform.ProcessSuspendResumeOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessSuspendResumeOrderEbmMapping;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ProcessStartCancelOrderType;
import com.oracle.oep.order.model.ProcessUpdateServiceSuspensionDateType;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.service.GenerateOrderEntityBean;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.CreateOrder;

public class ProcessStartCancelOrderFulfillmentProcessor implements Processor {
	
	public static final Logger log = LogManager.getLogger(ProcessStartCancelOrderFulfillmentProcessor.class); 
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		Message in = exchange.getIn();
		OrderEntryRequest request = in.getBody(OrderEntryRequest.class);
		ProducerTemplate template = exchange.getContext().createProducerTemplate();
		Map<String,OEPConfigPlanAttributesT> productSpecMap = new HashMap<String, OEPConfigPlanAttributesT>();
		
		ProcessStartCancelOrderType startCancelOrder =  request.getProcessStartCancelOrder();	
		
		String oepOrderNumber = startCancelOrder.getOrderId();
		String orderActionFromRequest = startCancelOrder.getOrderAction();
		String serviceNumber = startCancelOrder.getServiceIdentifier();
		OepOrder oepOrderDO = null;
		
		log.info("OEP Order Number : "+oepOrderNumber+".Received Start Cancel Order "+oepOrderNumber+". Order Action : "+orderActionFromRequest);
		
		try{				
			
			oepOrderDO = getOepOrderFromRespository(oepOrderNumber, template, exchange);
			List<OepOrderLine> oepOrderLines = oepOrderDO.getOepOrderLines();
			
			for (OepOrderLine oepOrderLine : oepOrderLines){
				
				String planName = oepOrderLine.getPlanName();
				OEPConfigPlanAttributesT configPlanAttributes = queryBRMForPlanAttributes(planName, template, oepOrderNumber);
				productSpecMap.put(planName, configPlanAttributes);
				
			}	
			
			if(orderActionFromRequest.equals(OEPConstants.OEP_START_ACTION)){
				
				oepOrderDO.setIsFutureDate(OEPConstants.OEP_CONSTANT_N);
				oepOrderDO.setIsPark(OEPConstants.OEP_CONSTANT_N);	
				
				sendOrderToFulfillment(oepOrderNumber, oepOrderDO, productSpecMap, template, orderActionFromRequest);
				
			}
			
			else if(orderActionFromRequest.equals(OEPConstants.OEP_CANCEL_ACTION)){
				
				oepOrderDO.setIsFutureDate(OEPConstants.OEP_CONSTANT_N);
				oepOrderDO.setIsPark(OEPConstants.OEP_CONSTANT_N);
				oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_CANCELLED);
				
				
			}
			
			//Save Order Entiry to DB			
			saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
			
			log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");
			
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			errorMessage = errorMessage!=null?(errorMessage.length()>200?errorMessage.substring(0, 200):errorMessage) : null;
			
			log.error("OEP Order number "+oepOrderNumber+". Exception while processing Order. "+errorMessage);
			
			GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
			oepOrderDO = entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);
			
			oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_FAILED);
			oepOrderDO.setStatusDescription(errorMessage);
			
			saveOepOrderDO(oepOrderDO, template, oepOrderNumber);	
			
			throw e;
			
			
		}
		
	}
	
	
	
	public CUSOPCCUPDATESERVICEOutputFlist processUpdateSuspensionDate(ProducerTemplate template, OepOrder oepOrderDO, OrderEntryRequest request){
		
		ProcessUpdateServiceSuspensionDateType updateServiceOrder = request.getProcessUpdateServiceSuspensionDate();
		String channelName = null;
		String userName= null;
		String oepOrderNumber = oepOrderDO.getOrderId();
		String orderActionFromRequest = updateServiceOrder.getOrderAction();
		String msisdn = updateServiceOrder.getServiceIdentifier();
		String suspensionDuration = updateServiceOrder.getSuspensionDuration();
		String dealName = updateServiceOrder.getDealName();
		String oepOrderSuspension = updateServiceOrder.getOrderId();
		
		CUSOPCCUPDATESERVICEOutputFlist updateResult = null;
		log.info("OEP Order Number : "+oepOrderNumber+".Received Suspension Order Number "+oepOrderSuspension);
		
		log.info("OEP Order Number : "+oepOrderNumber+".Received upadate Order "+oepOrderNumber+". Order Action : "+orderActionFromRequest);
		
		if(updateServiceOrder.getChannelName()!=null){
			
			channelName = updateServiceOrder.getChannelName();
		}
		else {
			
			channelName = OEPConstants.OAP_CHANNEL_NAME;
			
		}
		
		
		if(updateServiceOrder.getOrderUserId()!=null){
			
			userName = updateServiceOrder.getOrderUserId();
		}
		else {
			
			userName = OEPConstants.BRM_CUS_SEARCH_USER;
		}
		
		
		
		try{
			
			log.info("OEP Order Number : "+oepOrderNumber+". Searching Service Identifier from BRM");			
			
			CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRMForMSISDN(template, msisdn, channelName, userName, oepOrderNumber);
			
			log.info("OEP Order Number : "+oepOrderNumber+". Sending udate request to BRM to update suspense duration to "+suspensionDuration);
			
			updateResult = updateBRMServiceDetails(template, msisdn, searchResult, suspensionDuration, channelName, userName, oepOrderNumber, dealName,oepOrderSuspension);		
			
			
		}
		catch(Exception e){
			
			log.info("OEP order number : "+oepOrderNumber+ ". Exception while processing order : "+e.getMessage());
			throw new RuntimeException("Exception while processing update service request. "+e.getMessage());
		}
		
		return updateResult;
		
		
	}
	
	/**
	 * 
	 * 
	 **/
	private CUSOPCCUPDATESERVICEOutputFlist updateBRMServiceDetails(final ProducerTemplate template,final String msisdn, final CUSOPSEARCHOutputFlist searchResult,
			final String suspensionDuration, final String channelName, final String userName, final String oepOrderNumber, final String dealName, final String oepOrderSuspension){
		
		
		CUSOPCCUPDATESERVICEInputFlist updateServiceOpcode = new CUSOPCCUPDATESERVICEInputFlist();
		
		updateServiceOpcode.setPOID(OEPConstants.BRM_POID);
		updateServiceOpcode.setPROGRAMNAME(channelName);
		updateServiceOpcode.setACCOUNTNO(searchResult.getRESULTS().get(0).getCUSFLDBAACCOUNTNO());
		updateServiceOpcode.setUSERNAME(userName);
		updateServiceOpcode.setCUSFLDBARRINGACTION(new BigInteger(OEPConstants.BRM_BARRING_ACTION));
		updateServiceOpcode.setCUSFLDBARRINGTYPE(new BigInteger(OEPConstants.BRM_BARRING_DUNNING));
		updateServiceOpcode.setCUSFLDBARRINGLEVEL(new BigInteger(OEPConstants.BRM_BARRING_VOLUNT_SUSPND));
				
		
		List<SERVICES> services = updateServiceOpcode.getSERVICES();
		SERVICES service = new SERVICES();
		service.setSERVICETYPE(searchResult.getRESULTS().get(0).getSERVICEID());
		service.setMSISDN(msisdn);
		service.setLOGIN(searchResult.getRESULTS().get(0).getLOGIN());
		service.setACTION(new BigInteger(OEPConstants.BRM_BARRING_LOST_SIM));
		service.setCUSFLDSUSPENSEDURATION(new BigInteger(suspensionDuration));
		service.setORDERID(oepOrderSuspension);
		service.setDEALNAME(dealName);
		
		services.add(service);
		
		String inputXML = (String) template.requestBody("direct:convertToString", updateServiceOpcode);
		
		return sendUpdateServiceRequestToBRM(template, inputXML, oepOrderNumber);
		
	}
	
	/**
	 * 
	 * 
	 **/
	private CUSOPCCUPDATESERVICEOutputFlist sendUpdateServiceRequestToBRM(final ProducerTemplate template, final String inputXML, final String oepOrderNumber){
		
		log.info("OEP Order Number : "+oepOrderNumber+".Sending update service request to BRM");
		
				
		CUSOPCCUPDATESERVICEOutputFlist updateResult = null;
		
		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_UPDATE_SERVICE_OPCODE);
		opcode.setInputXml(inputXML);		
		
		
		log.info("OEP order number : "+oepOrderNumber+". Calling BRM webservice to Update Service Input xml : \n"+inputXML);
		 
		updateResult = (CUSOPCCUPDATESERVICEOutputFlist) template.requestBody("direct:sendUpdateServiceRequestToBRM", opcode);		 
				
		return updateResult;
		
	}
	
	private OepOrder getOepOrderFromRespository(final String oepOrderNumber,final ProducerTemplate template, final Exchange exchange){
		
		log.info("OEP Order Number : "+oepOrderNumber+".Querying OEP ORDER from OEP DB for "+oepOrderNumber);
		
		Map<String, String> planMap = new HashMap<String, String>();
		
		planMap.put("param", oepOrderNumber);
		
		Vector<OepOrder> resultSet = (Vector) template.requestBody("direct:queryOrderFromOEP", planMap);
		
		Iterator<OepOrder> iterator = resultSet.iterator();
		
		OepOrder oepOrderDO = null;
		
		while(iterator.hasNext()){
			
			oepOrderDO = iterator.next();
			
		}
		
		if(oepOrderDO==null){
			
			log.info("Order with Order number : "+oepOrderNumber+" is not found.");
			
			throw new RuntimeException("Order with Order number : "+oepOrderNumber+" is not found.");
			
			
			
		}
		
		log.info("OEP Order Number : "+oepOrderNumber+". Returning after Querying");
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
		
		Vector<OEPConfigPlanAttributesT> planAttributesList = (Vector) template.requestBody("direct:queryPlanFromBRM", planMap);
		
		Iterator<OEPConfigPlanAttributesT> iterator = planAttributesList.listIterator();
		
		OEPConfigPlanAttributesT planAttributeObject = null;
		
		while(iterator.hasNext()){
			
			planAttributeObject = iterator.next();
			
		}
		
		log.info("OEP Order Number : "+oepOrderNumber+". Returning for Querying plan attributes");
		return planAttributeObject;
		
	}	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * **/
	private void sendOrderToFulfillment(final String oepOrderNumber, final OepOrder oepOrderDO, final Map<String,OEPConfigPlanAttributesT> productSpecMap,
			final ProducerTemplate template, final String orderActionFromRequest) throws SOAPException{
		
		String originalOrderAction = oepOrderDO.getAction();
		String originalOrderRequestPayload = oepOrderDO.getPayloadXml();
		
		OrderEntryRequest originalOrderRequest = (OrderEntryRequest) template.requestBody("direct:convertToOrderEntryRequest", originalOrderRequestPayload);
		
		log.info("OEP Order Number : "+oepOrderNumber+". Order Action : "+orderActionFromRequest+". Original Order Action : "+originalOrderAction);
		
				
		if(originalOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
				|| originalOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)){
			
			String channelName = ProcessSuspendResumeOrderEbmHelper.getChannelName(originalOrderRequest, originalOrderAction);
			String serviceNumber = oepOrderDO.getServiceNo();
			String userName = oepOrderDO.getCsrid();		
			
			
			CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRMForMSISDN(template, serviceNumber, channelName, userName, oepOrderNumber);	
			
			RESULTS result = getActiveServiceFromSearchResult(searchResult);
			
			if(result == null){
				
				throw new RuntimeException("No Active Service is Found in BRM");
			}

			ProcessSuspendResumeOrderEbmMapping suspendResumeEbmMapping = new ProcessSuspendResumeOrderEbmMapping();
			CreateOrder salesOrderEbm = suspendResumeEbmMapping.createOSMOrderEBM(originalOrderRequest, oepOrderDO, originalOrderAction, oepOrderNumber, productSpecMap, result);
			
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			
			//Save Order Entiry to DB			
			saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
			
			sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber);
			log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
			 
		}
		
	}
	
	
	
	/**
	 * 
	 * 
	 * **/
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
	
	/**
	 * 
	 * @param searchResult
	 * @return
	 */
	private RESULTS getActiveServiceFromSearchResult(CUSOPSEARCHOutputFlist searchResult){
		
		
		for (RESULTS result: searchResult.getRESULTS()){
			
			if((result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_ACTIVE_STATUS_CODE)
				||(result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_SUSPENDED_STATUS_CODE)){
				
				return result;
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * 
	 * **/
	
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
