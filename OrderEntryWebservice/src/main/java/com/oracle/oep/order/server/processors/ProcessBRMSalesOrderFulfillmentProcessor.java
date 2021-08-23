package com.oracle.oep.order.server.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brmorder.model.CancelPlan;
import com.oracle.oep.brmorder.model.CollectionsActionBarring;
import com.oracle.oep.brmorder.model.FCA;
import com.oracle.oep.brmorder.model.NotifRemoveServiceNotification;
import com.oracle.oep.brmorder.model.PrepaidLifecycleBarring;
import com.oracle.oep.brmorder.model.SpendingCapAction;
import com.oracle.oep.brmorder.model.TerminateService;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.AddonActionEnum;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.OrderEntryResponse;
import com.oracle.oep.order.model.ProcessAddOnManagmentType;
import com.oracle.oep.order.model.ServiceTypeEnum;
import com.oracle.oep.order.service.ProcessCollectionBarringProcessor;
import com.oracle.oep.order.service.ProcessFcaOrderProcessor;
import com.oracle.oep.order.service.ProcessModifyOrderFulfillmentProcessor;
import com.oracle.oep.order.service.ProcessNotifRemoveServiceProcessor;
import com.oracle.oep.order.service.ProcessPrepaidLifecycleBarringProcessor;
import com.oracle.oep.order.service.ProcessSpendingCapProcessor;
import com.oracle.oep.order.service.ProcessTerminateServiceProcessor;

public class ProcessBRMSalesOrderFulfillmentProcessor implements Processor{
	

	public static final Logger log = LogManager.getLogger(ProcessBRMSalesOrderFulfillmentProcessor.class); 	
	

	@Override
	public void process(Exchange exchange) throws Exception {

		Message in = exchange.getIn();
		String brmOrderAction = in.getHeader(OEPConstants.BRM_ORDER_JMSCORRELATION_ID, String.class);
		String brmJmsTimeStamp = in.getHeader(OEPConstants.BRM_ORDER_JMSTIMESTAMP, String.class);
		String orderPayload = in.getBody(String.class);
		
		log.info("Received BRM Notificatoin Order with order action "+brmOrderAction);
		log.info("BRM Order Payload Received : \n"+orderPayload);	
		
		
		
		if(brmOrderAction.equals(OEPConstants.BRM_ORDER_SPENDINGCAP_ACTION)){
			
			log.info("BRM OrderAction is correct : "+brmOrderAction);	
			
			SpendingCapAction request = in.getBody(SpendingCapAction.class);
					
			ProcessSpendingCapProcessor spendingCapsProcessor = new ProcessSpendingCapProcessor();
					
			spendingCapsProcessor.processSpendingCapOrder(request, exchange, brmOrderAction, orderPayload, brmJmsTimeStamp);
			
			
			
		}
		
		else if (brmOrderAction.equals(OEPConstants.BRM_ORDER_COLLECTION_ACTION_BARRING)) {
			
			log.info("BRM OrderAction is correct : "+brmOrderAction);	
			
			CollectionsActionBarring request = in.getBody(CollectionsActionBarring.class);
			
			ProcessCollectionBarringProcessor collectionBarringProcessor = new ProcessCollectionBarringProcessor();
					
			collectionBarringProcessor.processCollectionBarringOrder(request, exchange, brmOrderAction, orderPayload, brmJmsTimeStamp);
			
			
			
			
		} else if(brmOrderAction.equals(OEPConstants.BRM_FCA_MOBILE_ACTION)) {
			
			log.info("BRM OrderAction is correct : "+brmOrderAction);	
			
			FCA request = in.getBody(FCA.class);
			
			ProcessFcaOrderProcessor fcaProcessor = new ProcessFcaOrderProcessor();
			
			fcaProcessor.processFcaOrder(request, exchange, brmOrderAction, orderPayload, brmJmsTimeStamp);
			
		}  else if(brmOrderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_BARRING_ACTION)) {
			
			log.info("BRM OrderAction is correct : "+brmOrderAction);	
			
			PrepaidLifecycleBarring request = in.getBody(PrepaidLifecycleBarring.class);
			
			ProcessPrepaidLifecycleBarringProcessor prepaidLifecycleBarringProcessor = new ProcessPrepaidLifecycleBarringProcessor();
			
			prepaidLifecycleBarringProcessor.processPrepaidLifecycleBarringOrder(request, exchange, brmOrderAction, orderPayload, brmJmsTimeStamp);
			
		}  else if(brmOrderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION)) {
			
			log.info("BRM OrderAction is correct : "+brmOrderAction);	
			
			NotifRemoveServiceNotification request = in.getBody(NotifRemoveServiceNotification.class);
			
			ProcessNotifRemoveServiceProcessor processor = new ProcessNotifRemoveServiceProcessor();
			
			processor.processNotifRemoveService(request, exchange, brmOrderAction, orderPayload, brmJmsTimeStamp);
			
		} else if(brmOrderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_TERMINATE_SERVICE_ACTION)) {
			
			log.info("BRM OrderAction is correct : "+brmOrderAction);	
			
			TerminateService request = in.getBody(TerminateService.class);
			
			ProcessTerminateServiceProcessor processor = new ProcessTerminateServiceProcessor();
			
			processor.processNotifRemoveService(request, exchange, brmOrderAction, orderPayload, brmJmsTimeStamp);
			
		}
		
		else if(brmOrderAction.equals(OEPConstants.BRM_ORDER_CANCEL_PLAN)) {
			
			log.info("BRM OrderAction is correct : "+brmOrderAction);	
			
			CancelPlan cancelPlanRequest = in.getBody(CancelPlan.class);
			ProducerTemplate template = exchange.getContext().createProducerTemplate();
			
			OrderEntryRequest orderEntryRequest = new OrderEntryRequest();
			ProcessAddOnManagmentType addOnOrderType = new ProcessAddOnManagmentType();			
			addOnOrderType.setServiceIdentifier(cancelPlanRequest.getMSISDN());
			addOnOrderType.setLoginId(cancelPlanRequest.getServiceIdentifier());
			addOnOrderType.setServiceType(ServiceTypeEnum.fromValue(cancelPlanRequest.getServiceType()));
			addOnOrderType.setBasePackageName(cancelPlanRequest.getBasePlanName());
			addOnOrderType.setAddOnPackageName(cancelPlanRequest.getCancelPlanName());
			addOnOrderType.setAction(AddonActionEnum.REMOVE);
			addOnOrderType.setBillingIndicator(cancelPlanRequest.getBillableItem()+"");
			addOnOrderType.setChannelName(OEPConstants.BRM_CHANNEL_NAME);
			addOnOrderType.setChannelTrackingId((String)in.getHeader("breadcrumbId"));
			
			orderEntryRequest.setProcessAddOnManagment(addOnOrderType);
			
			log.info("Order Entry Request: " + (String) template.requestBody("direct:convertxmlToString", orderEntryRequest));
			
			OrderEntryResponse orderEntryresponse = (OrderEntryResponse) template.requestBody("direct:callOEPOrderEntryWebservice", orderEntryRequest);

			log.info("ABM Response Order sumitted" + (String) template.requestBody("direct:convertxmlToString", orderEntryresponse));
			
		}
		
		else {
			
			log.info("BRM OrderAction is Incorrect : "+brmOrderAction);	
		}
		
	}

}
