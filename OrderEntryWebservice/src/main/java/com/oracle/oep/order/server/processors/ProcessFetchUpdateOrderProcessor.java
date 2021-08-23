package com.oracle.oep.order.server.processors;

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
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmMapping;
import com.oracle.oep.order.ebm.transform.ProcessDisconnectOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessDisconnectOrderEbmMapping;
import com.oracle.oep.order.ebm.transform.ProcessSuspendResumeOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessSuspendResumeOrderEbmMapping;
import com.oracle.oep.order.model.AddActionPlanType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.service.GenerateOrderEntityBean;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.CreateOrder;

public class ProcessFetchUpdateOrderProcessor  implements Processor {
	
	public static final Logger log = LogManager.getLogger(ProcessFetchUpdateOrderProcessor.class);
	
	ProducerTemplate template = null;
	OrderEntryRequest request  = null;	
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		log.info("ProcessFetchUpdateOrderProcessor class executing");

		Message in=exchange.getIn();	
		String oepOrderNumber = in.getHeader(OEPConstants.OEP_ORDER_NUMBER_HEADER, String.class);

		request = in.getBody(OrderEntryRequest.class);
		template = exchange.getContext().createProducerTemplate();
		OepOrder oepOrderDO = queryOEPForOrder(oepOrderNumber, template);

		String oepOrderAction = oepOrderDO.getAction();
		
		ProcessAddOrderEbmMapping addOrderEbmMapping = new ProcessAddOrderEbmMapping();
		Map<String,OEPConfigPlanAttributesT> productSpecMap = new HashMap<String,OEPConfigPlanAttributesT>();
		List<AddActionPlanType> orderedPlans = null;
		List<AddActionPlanType> addOnPlans=null;
		AddActionPlanType basePlan = null;
		oepOrderDO.setOrderStartDate(new Timestamp(new Date().getTime()));

		try{
		
		if(oepOrderDO.getAction().equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
				|| oepOrderDO.getAction().equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION.equals(oepOrderDO.getAction())
				|| OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION.equals(oepOrderDO.getAction()))
		{
			
			List<OepOrderLine> oepOrderLines = oepOrderDO.getOepOrderLines();

			for (OepOrderLine oepOrderLine : oepOrderLines){
				
				String planName = oepOrderLine.getPlanName();
				OEPConfigPlanAttributesT configPlanAttributes = queryBRMForPlanAttributes(planName, template, oepOrderNumber);
				productSpecMap.put(planName, configPlanAttributes);
				
			}	
			
				
	//			oepOrderDO.setIsFutureDate(OEPConstants.OEP_CONSTANT_N);
	//			oepOrderDO.setIsPark(OEPConstants.OEP_CONSTANT_N);	
				
				sendOrderToFulfillment(oepOrderNumber, oepOrderDO, productSpecMap, template, "Start");
				
			
			
	/*		else if(orderActionFromRequest.equals(OEPConstants.OEP_CANCEL_ACTION)){
				
				oepOrderDO.setIsFutureDate(OEPConstants.OEP_CONSTANT_N);
				oepOrderDO.setIsPark(OEPConstants.OEP_CONSTANT_N);
				oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_CANCELLED);
				
				
			}
	*/		
			//Save Order Entiry to DB			
			saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
			
			log.info("OEP Order Number : "+oepOrderNumber+".OepOrder Entity Bean is persisted");	
			
		
		}
		else
		{
			orderedPlans = ProcessAddOrderEbmHelper.getPlansFromOrder(request,oepOrderAction);

			for(AddActionPlanType plan : orderedPlans){
				
				String planType=null;
				
				//queryplan and initialize base plan and addOnPlan
				
				
				OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(plan.getPlanName(), template,oepOrderNumber);
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
			
			CreateOrder salesOrderEbm = addOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber, productSpecMap,null,template);	
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			
			//Save Order Entiry to DB			
			saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
			
			sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber);			
			log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");

		}
		}
		catch(Exception e){
			
			e.printStackTrace();
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			errorMessage = errorMessage!=null?(errorMessage.length()>200?errorMessage.substring(0, 200):errorMessage) : null;
			
			log.error("OEP Order number "+oepOrderNumber+". Exception while processing Suspend Order. "+errorMessage);
			
			oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_FAILED);
			oepOrderDO.setStatusDescription(errorMessage);
			
			saveOepOrderDO(oepOrderDO, template, oepOrderNumber);	
			
			throw e;
		}
		
		
}
	
	private OepOrder queryOEPForOrder(final String orderId,final ProducerTemplate template){
		
		log.info("OEP Order Number : "+orderId+".Querying OEP ORDER from OEP DB for "+orderId);
		
		Map<String, String> planMap = new HashMap<String, String>();
		
		planMap.put("param", orderId);
		
		Vector<OepOrder> oepOrder = (Vector) template.requestBody("direct:queryOrderFromOEP", planMap);
		
		Iterator<OepOrder> iterator = oepOrder.listIterator();
		
		OepOrder oepOrderObject = null;
		
		while(iterator.hasNext()){
			
			oepOrderObject = iterator.next();
			
		}
		
		if(oepOrderObject==null){
			
			log.info("Order with Order number : "+orderId+" is not found.");
			
			throw new RuntimeException("Order with Order number : "+orderId+" is not found.");
			
		}
		
		log.info("OEP Order Number : "+orderId+". Returning for Querying");
		return oepOrderObject;
		
	}
	
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
	private void sendSalesOrderEBM(final CreateOrder salesOrder,final ProducerTemplate template, final String oepOrderNumber) throws SOAPException{
		
		
		Document soapBodyDoc = (Document) template.requestBody("direct:convertToDocument", salesOrder);	
		SOAPMessage soapMessage = OrderDataUtils.createSOAPRequest(soapBodyDoc, oepOrderNumber, template);
		
		Map<String, Object> orderHeaders = new HashMap<String,Object>();
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_URI, OEPConstants.OSM_WEBSERVICE_URI);
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_WLS_CONTENT_TYPE, OEPConstants.OSM_WEBSERVICE_CONTETNT_TYPE);		
		
		template.sendBodyAndHeaders("direct:sendSalesOrderEBMToOSM", soapMessage.getSOAPPart().getEnvelope(),orderHeaders);
		log.info("OEP Order Number : "+oepOrderNumber+" Order sent to osm");

		
	}
	
	
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
		
		else if(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION.equals(originalOrderAction)
				||OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION.equals(originalOrderAction)){
			
			
			String channelName = ProcessDisconnectOrderEbmHelper.getChannelName(originalOrderRequest, originalOrderAction);
			String serviceNumber = oepOrderDO.getServiceNo();
			String userName = oepOrderDO.getCsrid();		
			
			
			CUSOPSEARCHOutputFlist searchResult = getAccountDataFromBRMForMSISDN(template, serviceNumber, channelName, userName, oepOrderNumber);	

			RESULTS result = getActiveServiceFromSearchResult(searchResult);
			
			if(result == null){
				
				throw new RuntimeException("No Active Service is Found in BRM");
			}
			
			ProcessDisconnectOrderEbmMapping disconnectEbmMapping = new ProcessDisconnectOrderEbmMapping();
			CreateOrder salesOrderEbm = disconnectEbmMapping.createOSMOrderEBM(originalOrderRequest, oepOrderDO, originalOrderAction, oepOrderNumber, productSpecMap, result);
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			
			//Save Order Entiry to DB			
			saveOepOrderDO(oepOrderDO,template, oepOrderNumber);
			sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber);
			log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
			
		}
		
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
			 
			 log.info("OEP order number : "+oepOrderNumber+". Error while fetching Account data from BRM "+searchResult.getERRORDESCR());
			 throw new RuntimeException(searchResult.getERRORDESCR()); 
			 
			 
		 }
		 
		}
		catch (Exception e) {
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			log.info("OEP order number : "+oepOrderNumber+". Exception while fetching Account data from BRM "+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return searchResult;
		
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
	 
	 /* 
	 * 
	 * This is the helper method to persist OepOrder enity bean to OEP DB
	 * 
	 */
	
	private void saveOepOrderDO(final OepOrder oepOrder,final ProducerTemplate template, final String oepOrderNumber){
		
		template.sendBody("direct:saveOrderToRespository", oepOrder);		
		
		
	}

}