package com.oracle.oep.order.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmMapping;
import com.oracle.oep.order.ebm.transform.ProcessNumberRelaseOrderEbmMapping;
import com.oracle.oep.order.model.AddActionPlanType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.presistence.model.BulkBatchMaster;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.CreateOrder;

public class ProcessNumberReleaseOrderFulfillmentProcessor {

	
	public static final Logger log = LogManager.getLogger(ProcessNumberReleaseOrderFulfillmentProcessor.class); 	
	OepOrder oepOrderDO = null;
	ProducerTemplate template = null;
	/* 
	 * 
	 * This method processes the add order for all services incliding Add Mobile, BB, Short code and Bulk SMS
	 * 
	 */
	public void processNumberReleaseOrders(final OrderEntryRequest request,final Exchange exchange, final String oepOrderAction, final String orderPayload, final String oepOrderNumber, final String oepServiceType) throws Exception {
		
		log.info("OEP order number : "+oepOrderNumber+" with action "+oepOrderAction+" started processing");
		
		try{
			
			
			template = exchange.getContext().createProducerTemplate();
			
			

			//generate Order DataObject's		
			oepOrderDO = getOepOrderDO(request, exchange, oepOrderAction, oepOrderNumber, orderPayload, oepServiceType, null);
			
			ProcessNumberRelaseOrderEbmMapping numberReleaseOrderEbmMapping = new ProcessNumberRelaseOrderEbmMapping();
			CreateOrder salesOrderEbm = numberReleaseOrderEbmMapping.createOSMOrderEBM(request,oepOrderDO,  oepOrderAction, oepOrderNumber);	
			
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
			
		
								
			sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber);			
			log.info("OEP Order Number : "+oepOrderNumber+".Order sent to OSM queue");
		
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
			log.info("OEP Order Number : "+oepOrderNumber+". Exception caught while processing the order.. "+e.getMessage());
			log.info(e);
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
	private OepOrder getOepOrderDO(final OrderEntryRequest request,final Exchange exchange,final String oepOrderAction, final String oepOrderNumber, final String orderPayload , final String oepServiceType, final String channelName){
		
		log.info("OEP Order Number : "+oepOrderNumber+".Started Initializing Oep Order Entity Bean .Order payload is "+orderPayload);	
		
		
		GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
		
		String csrId = "";
		Timestamp orderStartDate = ProcessAddOrderEbmHelper.getOrderStartDate(request, oepOrderAction);
		String serviceNo = ProcessAddOrderEbmHelper.getServiceNo(request, oepOrderAction);
		
		log.info("OEP Order Number : "+oepOrderNumber+". Before calling entityBeanGenerator.getOepOrderDO()");
		ProducerTemplate template = exchange.getContext().createProducerTemplate();

		OepOrder oepOrderDO= entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);			
			
		entityBeanGenerator.getOepOrderDO(oepOrderDO, serviceNo, csrId,  orderStartDate, oepOrderAction, oepOrderNumber, orderPayload, null, channelName);	

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
		String inputXML = (String) template.requestBody("direct:convertToString", soapMessage.getSOAPPart().getEnvelope());
		
		log.info("OEP Order Number : "+oepOrderNumber + inputXML);

		
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
