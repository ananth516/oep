package com.oracle.oep.order.utils;


import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;


import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
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
import com.oracle.oep.brm.opcodes.CUSOPCUSTVALIDATECHANGEPLANOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.PARAMS;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.service.ProcessModifyOrderFulfillmentProcessor;


public class OrderDataUtils {
	
	public static final Logger log = LogManager.getLogger(OrderDataUtils.class);
	
	//This method returns Account Id
	public static String getAccountId(final OrderEntryRequest request,final String orderAction){
		
		String accountId=null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || (orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION))){
			accountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAccountName();
		}			
			
		return accountId;
	}
	
		
	public static String getChannelTrackingId(final OrderEntryRequest request,final String orderAction, final int orderIndex){
		
		String channelTrackingId = null;
					
		if(orderAction.equalsIgnoreCase(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			channelTrackingId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChannelTrackingId();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)){
			
			channelTrackingId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChannelTrackingId();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || (orderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE))){
			
			channelTrackingId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getChannelTrackingId();
		}

		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)
				||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
				||orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
				||orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) ||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)
				||orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION)
				||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)){
			
			channelTrackingId = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(orderIndex).getMobileService().getServiceDetails().getChannelTrackingId();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
			
			channelTrackingId = request.getProcessAddOnManagment().getChannelTrackingId();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_NUMBER_RELEASE_MOBILE_SERVICE)){
			
			channelTrackingId = request.getProcessNumberReleaseOrder().getChannelTrackingId();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)){
			
			channelTrackingId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getChannelTrackingId();
		}
		
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)){
			
			channelTrackingId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getChannelTrackingId();
		}
		
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)){
			
			channelTrackingId = request.getProcessChangePlan().getChannelTrackingId();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
			
			channelTrackingId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getChannelTrackingId();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)){
			
			channelTrackingId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getChannelTrackingId();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MOBILE_SERVICE_CUG)) {
			
			channelTrackingId = request.getProcessCugOrder().getChannelTrackingId();
		}
		else if(orderAction.equals(OEPConstants.BRM_ORDER_SPENDINGCAP_ACTION) || orderAction.equals(OEPConstants.BRM_ORDER_COLLECTION_ACTION_BARRING)) {
			
			channelTrackingId = request.getProcessCollectionBarring().getChannelTrackingId();
		}
		 else if ( orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
			channelTrackingId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getFca().getChannelTrackingId();
		} else if(orderAction.equals(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
			channelTrackingId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getServiceDetails().getChannelTrackingId();
		}
		return channelTrackingId;
		
	}

	
	public static String getChannelTrackingIdPortIn(final OrderEntryRequest request,final String orderAction){
		
		String channelTrackingId = null;
		
		if(request.getProcessManagePortInOrder()!=null){
			
			channelTrackingId = request.getProcessManagePortInOrder().getChannelTrackingId();
		}
		
		else if(request.getProcessStartCancelOrder()!=null){
			
			channelTrackingId = request.getProcessStartCancelOrder().getChannelTrackingId();
		}
		else if(request.getProcessUpdateServiceSuspensionDate()!=null){
			
			channelTrackingId = request.getProcessUpdateServiceSuspensionDate().getChannelTrackingId();
		}
		
		return channelTrackingId;
		
	}
	
	
	public static Timestamp getTimeStampFromCalender(XMLGregorianCalendar date){
		
		return new Timestamp(date.toGregorianCalendar().getTimeInMillis());
	}
	
	
	//get Date Time in xml format
	public static XMLGregorianCalendar getXMLGregorianCalendarFromTimeStamp(Date timestamp) throws DatatypeConfigurationException{			
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(timestamp);
		
		XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);  
		
	        
		return cal;
		
		
	}	
	
	public static String getPermittedTypeCode(final String productSpec){
		
		if(productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BASEPLAN_PS)
				||productSpec.equalsIgnoreCase(OEPConstants.MOBILE_ADDON_PS)
				||productSpec.equalsIgnoreCase(OEPConstants.MOBILE_DATA_PS)
				||productSpec.equalsIgnoreCase(OEPConstants.MOBILE_VAS_PS)){
		     
			return OEPConstants.MOBILE_GSM_PERMITTED_TYPE_CODE;
		}
		else if(productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BB_PS)||productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BB_ADDON_PS)) {
			
			return OEPConstants.MOBILE_BB_PERMITTED_TYPE_CODE;
		}
		else if(productSpec.equalsIgnoreCase(OEPConstants.OEP_MOBILE_SERVICE_CUG)) {
			
			return OEPConstants.MOBILE_GSM_PERMITTED_TYPE_CODE;
		}
		else if(productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BULK_SMS_PS)) {
			
			return OEPConstants.MOBILE_BULK_SMS_PERMITTED_CODE;
		} else if(productSpec.equalsIgnoreCase(OEPConstants.SHORT_CODE_PS)
				|| productSpec.equalsIgnoreCase(OEPConstants.SHORT_CODE_ADDON_PS)) {
			return OEPConstants.MOBILE_SHORT_CODE_PERMITTED_TYPE_CODE;
		}
		
		else 
			return null;
		
		}
	
	
	public static CUSOPSEARCHOutputFlist getAccountDataFromBRMForMSISDN(final ProducerTemplate template, 
			final String msisdn, final String channelName, final String userName, final String channelTrackingId) throws Exception{
		
		
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

		return queryBRMForAccountData(template, inputXML, channelTrackingId);
		
	} 
	
	public static CUSOPSEARCHOutputFlist queryBRMForAccountData(final ProducerTemplate template, final String inputXML, final String channelTrackingId) throws Exception{
		
		CUSOPSEARCHOutputFlist searchResult = null;
		
		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_CUS_SEARCH_OPCODE);
		opcode.setInputXml(inputXML);		
		
		try{
		
		 log.info("Channel Tracking ID : "+channelTrackingId+". Calling BRM webservice to fetch Account data. Input xml : \n"+inputXML);
		 
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
			 
			 log.info("Channel Tracking ID : "+channelTrackingId+". Error while fetching Account data from BRM "+searchResult.getERRORDESCR());
			 throw new RuntimeException("ErrorCode="+searchResult.getERRORCODE()+" and ErrorDescription="+searchResult.getERRORDESCR()); 
			 
			 
		 }
		 
		}
		catch (Exception e) {
			
			 log.info("Channel Tracking ID : "+channelTrackingId+". Exception while fetching Account data from BRM "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		
		return searchResult;
		
	}
	
	
	public static boolean validateChangePlanRequest(final ProducerTemplate template, final String inputXML, final String channelTrackingId){
		
		CUSOPCUSTVALIDATECHANGEPLANOutputFlist validateResponse = null;
		
		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_VALIDATE_CHANGE_PLAN_OPCODE);
		opcode.setInputXml(inputXML);		
		
		try{
		
			log.info("Channel Tracking ID : "+channelTrackingId+". Calling BRM webservice to validate change plan. Input xml : \n"+inputXML);
			
			Exchange syncExchange = template.request("direct:validateRequestChangePlanRequest", new Processor() {
				
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
			 
			validateResponse = (CUSOPCUSTVALIDATECHANGEPLANOutputFlist) syncExchange.getOut().getBody();	
			 
			//validateResponse = (CUSOPCUSTVALIDATECHANGEPLANOutputFlist) template.requestBody("direct:validateRequestChangePlanRequest", opcode);
			 
			if(validateResponse.getSTATUS().toString().equals("1")){
				 
				 log.info("Channel Tracking ID : "+channelTrackingId+". Validation failed "+validateResponse.getERRORDESCR());
				 throw new RuntimeException("ErrorCode="+validateResponse.getERRORCODE()+" and ErrorDescription="+validateResponse.getERRORDESCR()); 
				 
				 
			 }
			
			else {
				
				log.info("Channel Tracking ID : "+channelTrackingId+". Validation Successful.");
				return true;
			}
		 
		}
		catch (Exception e) {
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			 log.info("OEP order number : "+channelTrackingId+". Exception while fetching Account data from BRM "+errorMessage);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}	
	
	
	/*
	 * 
	 * This is the helper method to create SOAP request from supplied SoapBodyDoc.
	 * 
	 */
	/**
	 * 
	 * @param soapBodyDoc
	 * @param oepOrderNumber
	 * @return
	 * @throws SOAPException
	 */
	 public static SOAPMessage createSOAPRequest(Document soapBodyDoc,String oepOrderNumber, ProducerTemplate template) throws SOAPException{
       
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
        username.addTextNode(template.requestBody("direct:getProperty","osm.username").toString());
        
        SOAPElement password = usernameToken.addChildElement("Password","wsse");
        password.addTextNode(template.requestBody("direct:getProperty","osm.password").toString());
        
        password.addAttribute(new QName("Type"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        
        log.info("OEP Order Number : "+oepOrderNumber+". SOAP Headers are set. ");
        
        soapMessage.saveChanges();       
		

        return soapMessage;
    }
	 
}
