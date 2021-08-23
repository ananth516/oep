package com.oracle.oep.order.server.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessSuspendResumeOrderEbmHelper;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.service.ProcessAddOrderFulfillmentProcessor;
import com.oracle.oep.order.service.ProcessBarringOrderFulfillmentProcessor;
import com.oracle.oep.order.service.ProcessCallDivertOrderFulfillmentProcessor;
import com.oracle.oep.order.service.ProcessDisconnectOrderFulfillmentProcessor;
import com.oracle.oep.order.service.ProcessModifyOrderFulfillmentProcessor;
import com.oracle.oep.order.service.ProcessNumberReleaseOrderFulfillmentProcessor;
import com.oracle.oep.order.service.ProcessSuspendResumeOrderFulfillmentProcessor;

public class ProcessSalesOrderFulfillmentProcessor implements Processor {

	public static final Logger log = LogManager.getLogger(ProcessSalesOrderFulfillmentProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		Message in = exchange.getIn();
		String oepOrderAction = in.getHeader(OEPConstants.ORDER_ACTION_HEADER, String.class);
		String oepOrderNumber = in.getHeader(OEPConstants.OEP_ORDER_NUMBER_HEADER, String.class);
		String oepServiceType = in.getHeader(OEPConstants.SERVICE_TYPE_HEADER, String.class);
		String orderPayload = in.getBody(String.class);

		
		log.info("Received OEP Order : "+oepOrderNumber+" with order action "+oepOrderAction);	

				
		OrderEntryRequest request = in.getBody(OrderEntryRequest.class);	
		String portType = ProcessAddOrderEbmHelper.getPortType(request, oepOrderAction);

		
		if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) 
			|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)
			|| oepOrderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)
			|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
			|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)
			|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
			|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)
			|| (oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)  &&  portType.equals(OEPConstants.OEP_PORTIN))){

			log.info("OEPOrderAction is correct : "+oepOrderAction);	
			
			ProcessAddOrderFulfillmentProcessor addProcessor = new ProcessAddOrderFulfillmentProcessor();
			addProcessor.processAddOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, "Mobile");
		
		}
		
		else if(oepOrderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION) 
				|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)
				|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)
				|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)
				|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)
				|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)
				|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)
     			|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)
     			|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)
				|| oepOrderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)
				|| oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)
				|| oepOrderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
				
			
			ProcessModifyOrderFulfillmentProcessor modifyProcessor = new ProcessModifyOrderFulfillmentProcessor();
			modifyProcessor.processModifyOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, oepServiceType);
			
		}
		
		else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
				||oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)
				|| oepOrderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
			
			ProcessDisconnectOrderFulfillmentProcessor disconnectProcessor = new ProcessDisconnectOrderFulfillmentProcessor();
			disconnectProcessor.processDisconnectOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, oepServiceType);
			
		}
		
		else if(oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)

				||oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)
				||oepOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)){
			
			ProcessSuspendResumeOrderFulfillmentProcessor suspendResumeProcessor = new ProcessSuspendResumeOrderFulfillmentProcessor();
			suspendResumeProcessor.processSuspendResumeOrders(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, oepServiceType);
			
		}

		else if(oepOrderAction.equals(OEPConstants.OEP_NUMBER_RELEASE_MOBILE_SERVICE)){
			
			ProcessNumberReleaseOrderFulfillmentProcessor numberReleaseProcessor = new ProcessNumberReleaseOrderFulfillmentProcessor();
			numberReleaseProcessor.processNumberReleaseOrders(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, oepServiceType);
			
		}
		
		else if(oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)  &&  !portType.equals(OEPConstants.OEP_PORTIN)){
			log.info("OEP Port Type is not correct : "+portType);	
			
		}
		
		else if(oepOrderAction.equals(OEPConstants.OEP_ACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION) || oepOrderAction.equals(OEPConstants.OEP_DEACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION) ){
			
			ProcessCallDivertOrderFulfillmentProcessor callDivertProcessor = new ProcessCallDivertOrderFulfillmentProcessor();
			callDivertProcessor.processCallDivertOrders(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, oepServiceType);
			
		}
		
		else if(oepOrderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)){
			
			ProcessAddOrderFulfillmentProcessor bulkSMSProcessor = new ProcessAddOrderFulfillmentProcessor();
			bulkSMSProcessor.processAddOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, oepServiceType);
			
		}
		
		else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
			
			ProcessDisconnectOrderFulfillmentProcessor disconnectProcessor = new ProcessDisconnectOrderFulfillmentProcessor();
			disconnectProcessor.processDisconnectOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, oepServiceType);
			
		}

		else if(oepOrderAction.equals(OEPConstants.BRM_ORDER_SPENDINGCAP_ACTION) || oepOrderAction.equals(OEPConstants.BRM_ORDER_COLLECTION_ACTION_BARRING)
				|| oepOrderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_BARRING_ACTION)){
			log.info("OEPOrderAction is correct : "+oepOrderAction);	
		
			ProcessBarringOrderFulfillmentProcessor barringOrder = new ProcessBarringOrderFulfillmentProcessor();
			barringOrder.processBarringOrders(request, exchange, oepOrderAction, orderPayload, oepOrderNumber);
		}
		
		else if(oepOrderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION)){
			
			ProcessDisconnectOrderFulfillmentProcessor disconnectProcessor = new ProcessDisconnectOrderFulfillmentProcessor();
			disconnectProcessor.processRemoveServiceOrder(request, exchange, oepOrderAction, orderPayload, oepOrderNumber, oepServiceType);
			
		}
		
		else{
			log.info("OEPOrderAction is incorrect : "+oepOrderAction);
			
		}	
	
	
	}

}
