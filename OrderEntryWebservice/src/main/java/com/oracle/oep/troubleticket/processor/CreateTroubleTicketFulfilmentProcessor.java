package com.oracle.oep.troubleticket.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.stringtemplate.v4.ST;

import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.notif.event.xsd.EmailNotification;
import com.oracle.oep.notif.event.xsd.NotifEvent;
import com.oracle.oep.notif.event.xsd.SMSNotification;
import com.oracle.oep.notif.xsd.NotifAddOnSubscriptionFailure;
import com.oracle.oep.notif.xsd.NotifCancelAddOnSubscriptionFailure;
import com.oracle.oep.notif.xsd.NotifChangeRatePlanFailure;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.utils.BRMHelperUtils;
import com.oracle.oep.troubleticket.ebm.xsd.CreateTroubleTicketEBM;
import com.oracle.oep.troubleticket.ebm.xsd.CreateTroubleTicketEBM.DataArea.TroubleTicketEBM;
import com.oracle.oep.troubleticket.xsd.CreateTroubleTicketRequest;
import com.oracle.oep.troubleticket.xsd.CreateTroubleTicketResponse;

public class CreateTroubleTicketFulfilmentProcessor implements Processor {

	public static final Logger log = LogManager.getLogger(CreateTroubleTicketFulfilmentProcessor.class);
	
	@Autowired
	private ProducerTemplate template;	
	
	public ProducerTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ProducerTemplate template) {
		this.template = template;
	}
	
	private void inIt(){
		
		 
		
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {

		//template = exchange.getContext().createProducerTemplate();
		Message in = exchange.getIn();
		Message out = exchange.getOut();
		
		
		CreateTroubleTicketEBM createTtRequestEbm = in.getBody(CreateTroubleTicketEBM.class);
		TroubleTicketEBM ttEbmpayload = createTtRequestEbm.getDataArea().getTroubleTicketEBM();
		String createTTEbmStr = in.getBody(String.class);
		String orderAction = ttEbmpayload.getOrderName();
		String orderId = ttEbmpayload.getOrderID();
		String serviceType= getServiceType(ttEbmpayload.getServiceType());
		String serviceNo= null;
		String callCategory = ttEbmpayload.getCallCategory();
		
		
		
		log.info("Received Trouble Ticket Request from OSM : \n"+createTTEbmStr + ". Order Id : "+orderId +". Order Action : "+orderAction);
		
		OepOrder oepOrderDO = queryOrderFromOEP(ttEbmpayload);
		
		sendEmailAndSMSNotification(callCategory,orderId,oepOrderDO.getServiceNo());
		
		
		if(orderAction.equalsIgnoreCase(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)
			||orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)
			||orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)
			||orderAction.equalsIgnoreCase(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)){
			
			sendFailureEventToNotif(ttEbmpayload,oepOrderDO);
		}		
		
		if(OEPConstants.OEP_BULK_SMS.equalsIgnoreCase(serviceType)||OEPConstants.OEP_SC.equalsIgnoreCase(serviceType)){
			
			if(!(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION.equalsIgnoreCase(orderAction)
				||OEPConstants.OEP_ADD_BULK_SMS_ACTION.equalsIgnoreCase(orderAction))){
				
				log.info("OrderId : "+orderId+". ServiveType : "+serviceType+". Querying BRM for Account data ");
				CUSOPSEARCHOutputFlist searchResult=BRMHelperUtils.getAccountDataFromBRM(template, null, oepOrderDO.getServiceNo(), "OEP", OEPConstants.BRM_CUS_SEARCH_USER, oepOrderDO.getOrderId());
				if(searchResult==null||searchResult.getERRORDESCR()!=null){
					
					if(searchResult!=null) {
						String errorMessage = searchResult.getERRORDESCR()!=null?searchResult.getERRORDESCR():"Account not found in BRM";
						log.info("OrderId : {}. ServiveType : {}. "+errorMessage,orderId,errorMessage);
					}
					throw new Exception("Account not found.");
				}
				
				serviceNo = Long.toString(searchResult.getRESULTS().get(0).getDEVICEID());
			}
			
			else			
				serviceNo=oepOrderDO.getServiceNo();
		}
		else{
			
			serviceNo=oepOrderDO.getServiceNo();

		}
		CreateTroubleTicketRequest ttWSRequest = new CreateTroubleTicketRequest();
		
		
		
		ttWSRequest.setOrderId(ttEbmpayload.getOrderID());
		ttWSRequest.setAction(ttEbmpayload.getAction());
		ttWSRequest.setChannel(ttEbmpayload.getChannel());
		ttWSRequest.setCallType(OEPConstants.OEP_TT_CALL_ACTION_TYPE);
		ttWSRequest.setCallArea(ttEbmpayload.getCallArea());
		ttWSRequest.setCallCategory(ttEbmpayload.getCallCategory());
		ttWSRequest.setCallSubCategory(ttEbmpayload.getCallSubCategory());
		ttWSRequest.setCallActionType(ttEbmpayload.getCallActionType());
		//ttWSRequest.setServiceAccount(oepOrderDO.getServiceNo());
		ttWSRequest.setBillingAccount(ttEbmpayload.getAccountNumber());
		//ttWSRequest.setCustomerAccount(ttEbmpayload.getCustomerAccountId());
		ttWSRequest.setServiceType(serviceType);
		ttWSRequest.setPriority(getPriority(ttEbmpayload.getPriority()));
		ttWSRequest.setCallBack(getCallBack(ttEbmpayload.getCallBack()));
		ttWSRequest.setRemarks(ttEbmpayload.getRemarks());
		ttWSRequest.setIssueDateTime(ttEbmpayload.getContactDateTime());
		ttWSRequest.setOrderId(ttEbmpayload.getOrderID());
		ttWSRequest.setFailureCode(ttEbmpayload.getFailureCode());
		ttWSRequest.setFailureDescription(ttEbmpayload.getFailureDescription());
		ttWSRequest.setServiceNo(serviceNo);
		
		
		log.info("Order Id : "+orderId +".  tt WS request : "+template.requestBody("direct:convertToString",ttWSRequest));
		
		
		try{
			
			Object result = template.requestBody("direct:callTroubleTicketService", ttWSRequest);
			
			
			
			if(result instanceof CreateTroubleTicketResponse){
				
				CreateTroubleTicketResponse ttResponse = (CreateTroubleTicketResponse) result;
				
				log.info("Order Id : "+orderId +". CreateTroubleTicketResponse : \n"+template.requestBody("direct:convertToString", ttResponse));
				
				ttResponse.setOrderId(orderId);
				if(ttResponse.getResponseCode().equals("0")){
				
					long ttId = ttResponse.getTicketId();
					log.info("Order Id : "+orderId +". Trouble ticket created successfully. Ticket Id : "+ttId);
						
				}
				
				else {
					
					log.error("Order Id : "+orderId+". Failed to create trouble ticket."+ttResponse.getResponseDesscription());
					throw new Exception(ttResponse.getResponseCode()+" - "+ttResponse.getResponseDesscription());
					
				}
			}
			
		}
		catch(CamelExecutionException e){
			
			log.error("Order Id : "+orderId+". "+e.getCause().getLocalizedMessage());
			
		}
		catch(Exception e){
			
			log.error("Order Id : "+orderId+". "+e.getMessage());
			
		}
		
	}
	
	
	private OepOrder queryOrderFromOEP (TroubleTicketEBM ttEbmpayload) throws Exception{
		
		String orderAction = ttEbmpayload.getOrderName();
		String orderId = ttEbmpayload.getOrderID();
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("param", orderId);
		
		Vector<OepOrder> vector = (Vector<OepOrder>) template.requestBody("direct:queryOrderFromOEP",params);
		
		if(!(vector.size()>0)){
			
			log.error("Order not found wiht Id "+orderId);
			throw new Exception("Order not found wiht Id "+orderId);
		}
		
		return vector.get(0);
		
	}

	
	private void sendFailureEventToNotif(TroubleTicketEBM ttEbmpayload,OepOrder oepOrderDO) throws Exception {


		String orderAction = ttEbmpayload.getOrderName();
		String orderId = ttEbmpayload.getOrderID();
		
		/*Map<String,Object> params = new HashMap<String,Object>();
		params.put("param", orderId);
		
		Vector<OepOrder> vector = (Vector<OepOrder>) template.requestBody("direct:queryOrderFromOEP",params);
		
		if(!(vector.size()>0)){
			
			log.error("Order not found wiht Id "+orderId);
			throw new Exception("Order not found wiht Id "+orderId);
		}
		
		OepOrder oepOrderDO = vector.get(0);*/
		
		String msisdn = oepOrderDO.getServiceNo();
		List<OepOrderLine> orderLines =  oepOrderDO.getOepOrderLines();
		
		OepOrderLine parentLine = null;
		List<OepOrderLine> childLines = new ArrayList<OepOrderLine>();
		
		for(OepOrderLine line:orderLines){
			
			if(line.getOrderLineId().equals(line.getParentOrderLineId())){
				
				parentLine=line;
				
			}
			else {
				
				childLines.add(line);
			}
			
		}
		
		
		String eventpayload = null;
		String eventName = null;
		
		if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION) ){
			
			NotifChangeRatePlanFailure changePlanFailureEvent = new NotifChangeRatePlanFailure();
			changePlanFailureEvent.setAccountNo(ttEbmpayload.getAccountNumber());
			changePlanFailureEvent.setServiceNo(Long.parseLong(oepOrderDO.getServiceNo()));
			changePlanFailureEvent.setEventDescr("Addon Subscription Failure");
			changePlanFailureEvent.setChannel("OEP");
			
			if(ttEbmpayload.getCallCategory().equalsIgnoreCase("BRM") && ttEbmpayload.getFailureCode().equalsIgnoreCase("10724")){
				
				changePlanFailureEvent.setFailureCode(ttEbmpayload.getFailureCode());
			}
			
			else{
				changePlanFailureEvent.setFailureCode("DEFAULT");
			}
			
			eventName =  OEPConstants.NOTIF_CHANGE_RATEPLAN_FAILURE;			
			eventpayload =(String) template.requestBody("direct:convertToString",changePlanFailureEvent);			
			
		}
		
		else if(childLines.size()>0 && childLines.get(0).getPlanAction().equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE)){
			
			NotifCancelAddOnSubscriptionFailure cancelAddonFailure = new NotifCancelAddOnSubscriptionFailure();
			cancelAddonFailure.setAccountNo(ttEbmpayload.getAccountNumber());
			cancelAddonFailure.setServiceNo(Long.parseLong(oepOrderDO.getServiceNo()));
			cancelAddonFailure.setEventDescr("Cancel Addon Subscription Failure");
			cancelAddonFailure.setChannel("OEP");
			
			if(ttEbmpayload.getCallCategory().equalsIgnoreCase("BRM") && ttEbmpayload.getFailureCode().equalsIgnoreCase("10724")){
				
				cancelAddonFailure.setFailureCode(ttEbmpayload.getFailureCode());
			}
			
			else{
				cancelAddonFailure.setFailureCode("DEFAULT");
			}
			
			eventName = OEPConstants.NOTIF_CANCEL_ADDON_FAILURE;			
			eventpayload =(String) template.requestBody("direct:convertToString",cancelAddonFailure);			
				
		}
			
		else if(childLines.size()>0 && childLines.get(0).getPlanAction().equalsIgnoreCase(OEPConstants.OSM_ADD_ACTION_CODE)){
			
			NotifAddOnSubscriptionFailure addAddonFailure = new NotifAddOnSubscriptionFailure();
			addAddonFailure.setAccountNo(ttEbmpayload.getAccountNumber());
			addAddonFailure.setServiceNo(Long.parseLong(oepOrderDO.getServiceNo()));
			addAddonFailure.setEventDescr("Addon Subscription Failure");
			addAddonFailure.setChannel("OEP");
			
			if(ttEbmpayload.getCallCategory().equalsIgnoreCase("BRM") && ttEbmpayload.getFailureCode().equalsIgnoreCase("10724")){
				
				addAddonFailure.setFailureCode(ttEbmpayload.getFailureCode());
			}
			
			else{
				addAddonFailure.setFailureCode("DEFAULT");
			}
			
			eventName = OEPConstants.NOTIF_ADD_ADDON_FAILURE;			
			eventpayload =(String) template.requestBody("direct:convertToString",addAddonFailure);	
			
			
		}
		
		log.info("OrderId : "+orderId+". Event Payload : \n"+eventpayload);
		
		Map<String, Object> messageHeaders = new HashMap<String,Object>();
		messageHeaders.put("CHANNEL_NAME","SMS");		
		messageHeaders.put("JMSCorrelationID", eventName);
		
		template.sendBodyAndHeaders("direct:sendFailureEventToNotif", eventpayload, messageHeaders);
		
		log.info("OrderId : "+orderId+". "+messageHeaders.get("JMSCorrelationID")+" event sent to OEP_EVENTS_NOTIFICATTION_QUEUE.");
		
		
	}

	private String getCallBack(String callBack) {

		log.info("CallBackRequired : " + callBack);
		
		if(callBack==null){
			
			return "1";
			
		}
		else if(callBack.equalsIgnoreCase("NO"))
			return "1";
		
		else if(callBack.equalsIgnoreCase("YES"))
			return "0";
		
		return "1";
	}

	private String getPriority(String priority) {
		
		log.info("Prioeity : " + priority);
		
		if(priority == null){
			
			return "NORMAL";
		}
		else if(priority.equalsIgnoreCase("NORMAL"))			
			return "NORMAL";
		
		else if(priority.equalsIgnoreCase("HIGH"))
			return "ESCALATION";
		
		return "NORMAL";
	}
	
	private String getServiceType(String serviceType){
		
		
		if(serviceType.equalsIgnoreCase("Mobile Broadband"))
			return "BB";
		else if(serviceType.equalsIgnoreCase("Short Code"))
			return "SC";
		else
			return serviceType;
		
	}
	
	/**
	 * 
	 * @param callCategory
	 * @param orderId
	 * @param string
	 */
	private void sendEmailAndSMSNotification(String callCategory, String orderId, String serviceNumber) {
		// TODO Auto-generated method stub
		
		String brmMailId = template.requestBody("direct:getProperty","provisioning.brm.mail").toString();
		String brmSMS = template.requestBody("direct:getProperty","provisioning.brm.msisdn").toString();
		String ossMailId = template.requestBody("direct:getProperty","provisioning.oss.mail").toString();
		String ossSMS = template.requestBody("direct:getProperty","provisioning.oss.msisdn").toString();
		
		ST messageTemplate= new ST(OEPConstants.OEP_PROVISIONING_FAILURE_BODY_TEMPLATE,'$','$');
		
		messageTemplate.add("serviceNumber", serviceNumber);
		messageTemplate.add("orderId", orderId);
		NotifEvent event = new NotifEvent();
		
				
		SMSNotification smsNotification = new SMSNotification();
		
		smsNotification.setBody(messageTemplate.render());
		
		EmailNotification emailNotification = new EmailNotification();		
		
		emailNotification.setSubject(OEPConstants.OEP_PROVISIONING_FAILURE_SUBJECT_TEMPLATE);
		emailNotification.setBody(messageTemplate.render());
		
		if("BRM".equals(callCategory)){
			
			smsNotification.setToNumber(Long.parseLong(brmSMS));
			emailNotification.setTo(brmMailId);
		}
		else {
			smsNotification.setToNumber(Long.parseLong(ossSMS));
			emailNotification.setTo(ossMailId);
			
		}
		
		event.setEmailNotification(emailNotification);
		event.setSMSNotification(smsNotification);
		
		String payload =  template.requestBody("direct:convertToString",event).toString();
		
		Map<String,Object> notifEventHeaders = new HashMap<String,Object>();
		notifEventHeaders.put(OEPConstants.JMS_CORRELATION_ID,"SMS/EMAIL");
		notifEventHeaders.put(OEPConstants.EVENT_NAME,OEPConstants.OEP_PROVISIONING_FAILURE_EVENT);
		notifEventHeaders.put(OEPConstants.NOTIFICATION_SOURCE,"OEP");
		
		log.info("Event Name : {}, Target Channel Name : SMS/EMAIL. Request {}",OEPConstants.OEP_PROVISIONING_FAILURE_EVENT ,payload);
				
		template.sendBodyAndHeaders("weblogicJMS:OEP_EVENTS_NOTIFICATION_TOPIC", event, notifEventHeaders);
	}

	

}
