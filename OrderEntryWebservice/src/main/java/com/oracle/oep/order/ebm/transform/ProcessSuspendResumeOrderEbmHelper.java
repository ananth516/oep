package com.oracle.oep.order.ebm.transform;

import java.sql.Timestamp;
import java.util.Date;

import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.SuspendOrderServiceType;
import com.oracle.oep.order.utils.OrderDataUtils;

public class ProcessSuspendResumeOrderEbmHelper {
	
	 public static String getCsrId(final OrderEntryRequest request, final String oepOrderAction, final int index){
			
			String csrId = null;
			
			if(oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)||oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
					||oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) || oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
					|| oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) || oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)) {
				csrId = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getMobileService().getServiceDetails().getOrderUserId();
					} else if (oepOrderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)) {
			csrId = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index)
					.getBulkSMSService().getServiceDetails().getOrderUserId();
		} else if (oepOrderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)) {
			csrId = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index)
					.getMobileShortCodeService().getServiceDetails().getOrderUserId();
		} else if (oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)
				|| oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)
				) {
			csrId = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index)
					.getMobileBBService().getServiceDetails().getOrderUserId();
		}
			return csrId;
		}
	 
	 
	 public static String getOrderActionFromRequest(final OrderEntryRequest request, final String oepOrderAction, final int index){
			
			String orderActionFromRequest = null;
			
			if(oepOrderAction.equals(OEPConstants.OEP_SUSPEND_RESUME_ORDER)){
				
				orderActionFromRequest = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getMobileService().getServiceDetails().getOrderAction();
			}else if (oepOrderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)) {
			orderActionFromRequest = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()
					.get(index).getBulkSMSService().getServiceDetails().getOrderAction();
		} else if (oepOrderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)) {
			orderActionFromRequest = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()
					.get(index).getMobileShortCodeService().getServiceDetails().getOrderAction();
		}
				
			return orderActionFromRequest;
		}
	 
	
	 public static Timestamp getOrderStartDate(final OrderEntryRequest request, final String oepOrderAction, final int index){
			
			Timestamp orderStartDate=null; 
			if(oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)||oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
					||oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) || oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
					|| oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) || oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
				
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getMobileService().getServiceDetails().getOrderStartDate());
				
			} else if (oepOrderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)) {

			orderStartDate = OrderDataUtils
					.getTimeStampFromCalender(request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()
							.get(index).getBulkSMSService().getServiceDetails().getOrderStartDate());

		} else if (oepOrderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)) {

			orderStartDate = OrderDataUtils
					.getTimeStampFromCalender(request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()
							.get(index).getMobileShortCodeService().getServiceDetails().getOrderStartDate());

		} else if(oepOrderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)||oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
					||oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION) || oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
					|| oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION) || oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
				
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getMobileBBService().getServiceDetails().getOrderStartDate());
				
			}
			
			return orderStartDate;
			
		}
	 
	 public static String getServiceNo(final OrderEntryRequest request,final String orderAction, int index){
			
			String serviceNo=null;		
					
			if(orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
					||orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
					||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
				serviceNo = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getMobileService().getServiceDetails().getServiceIdentifier();
			}	
			
			else if (orderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)) {

			serviceNo = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index)
					.getBulkSMSService().getServiceDetails().getServiceIdentifier();
		} else if (orderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)) {

			serviceNo = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index)
					.getMobileShortCodeService().getServiceDetails().getServiceIdentifier();
		} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
					||orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
					||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
				serviceNo = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getMobileBBService().getServiceDetails().getServiceIdentifier();
			}		
			
			
			return serviceNo;
		}
	 
	
	 public static String getUIMServiceIdentifier(final OrderEntryRequest request,final String orderAction, final int index){
			
			String serviceIdentifer = null;
			
			if(orderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)){
				
				serviceIdentifer = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getMobileShortCodeService().getServiceDetails().getServiceIdentifier();
			}
			else if (orderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)) {

				serviceIdentifer = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getBulkSMSService().getServiceDetails().getServiceIdentifier();
			} 
			
			return serviceIdentifer;
		}
	 
	 
	
	public static String getChannelName(final OrderEntryRequest request,final String orderAction ){
		 
		 String channelName = null;
		 
		 if(orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
				 ||orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)|| 
				 orderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)
				 || orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
			 channelName = OEPConstants.OAP_CHANNEL_NAME;
			} 
		 else if (orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				 ||orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION) || 
				 orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION) ||
				 orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)) {
				channelName = OEPConstants.OAP_CHANNEL_NAME;
			}
		 
		 
		 return channelName;
		 
	}
	
	public static String getChannelTrackingId(final OrderEntryRequest request,final String orderAction, final int index ){
		 
		 String channelTrackingId = null;
		 
		 if(orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION) ||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
				 ||orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
				 || orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
			 
			 channelTrackingId = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getMobileService().getServiceDetails().getChannelTrackingId();
			
		 }else if (orderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION)) {

			channelTrackingId = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index)
					.getBulkSMSService().getServiceDetails().getChannelTrackingId();

		} else if (orderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)) {

			channelTrackingId = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index)
					.getMobileShortCodeService().getServiceDetails().getChannelTrackingId();

		} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				 ||orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
				 || orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)) {
			 
			 channelTrackingId = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(index).getMobileBBService().getServiceDetails().getChannelTrackingId();
		 
		 }
		 
		 
		 return channelTrackingId;
		 
	}
	
	public static String getPlanAction(final OrderEntryRequest request, final String orderAction, final int index ){
			
			String planAction=null;		
					
			if(orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION) ||orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)|| orderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)
				|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
				
				planAction=OEPConstants.OSM_SUSPEND_ACTION_CODE;
			}		
			
			else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)|| orderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) 
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
				
				planAction=OEPConstants.OSM_RESUME_ACTION_CODE;
			}
			return planAction;
		}	 
	

	 public static String getBillingAccountId(final OrderEntryRequest request,final String orderAction, final CUSOPSEARCHOutputFlist searchResult ){
			
			String billingAccountId=null;		
			
			billingAccountId = searchResult.getRESULTS().get(0).getCUSFLDBAACCOUNTNO();
			
				
			return billingAccountId;
		}
	 
	
	 public static String getCustomerAccountId(final OrderEntryRequest request, final String orderAction, final CUSOPSEARCHOutputFlist searchResult ){
			
			String customerAccountId=null;		
			
			customerAccountId = searchResult.getRESULTS().get(0).getCUSFLDCAACCOUNTNO();
					
				
			return customerAccountId;
		}
	 
	 
	 public static String getSuspendReason(final OrderEntryRequest request, final String orderAction){
			
			String suspendReason=null;		
			if(orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)	){
				
				for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
					
					 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileService().getServiceDetails().getOrderAction())){
						 suspendReason = suspendResumeOrder.getMobileService().getServiceDetails().getReason();
						 
						 break;
					 }
				}
				
			}else if (orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION)) {

			for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder()
					.getListOfOEPOrder().getOEPOrder()) {

				if (orderAction
						.equalsIgnoreCase(suspendResumeOrder.getBulkSMSService().getServiceDetails().getOrderAction())) {
					suspendReason = suspendResumeOrder.getBulkSMSService().getServiceDetails().getReason();

					break;
				}
			}

		} else if (orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)) {

			for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder()
					.getListOfOEPOrder().getOEPOrder()) {

				if (orderAction
						.equalsIgnoreCase(suspendResumeOrder.getMobileShortCodeService().getServiceDetails().getOrderAction())) {
					suspendReason = suspendResumeOrder.getMobileShortCodeService().getServiceDetails().getReason();

					break;
				}
			}

		} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION) ||
				orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
			
			for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
				
				 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileBBService().getServiceDetails().getOrderAction())){
					 suspendReason = suspendResumeOrder.getMobileBBService().getServiceDetails().getReason();
					 
					 break;
				 }
			}
			
		}
			return suspendReason;
		}
	 
	 public static int getIndexFromOrder(final OrderEntryRequest request, final String orderAction){
		 
		 int orderIndex = 0;
		 
		 if(orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)
			|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
			|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
			|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
			|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) 
			|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
		 
			 for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
					
				 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileService().getServiceDetails().getOrderAction())){
					
					 orderIndex = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().indexOf(suspendResumeOrder);
					 
					 break;
				 }
			}
		 }else if (orderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION) ) {

			for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder()
					.getListOfOEPOrder().getOEPOrder()) {

				if (orderAction
						.equalsIgnoreCase(suspendResumeOrder.getBulkSMSService().getServiceDetails().getOrderAction())) {

					orderIndex = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()
							.indexOf(suspendResumeOrder);

					break;
				}
			}
		} else if (orderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)) {

			for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder()
					.getListOfOEPOrder().getOEPOrder()) {

				if (orderAction
						.equalsIgnoreCase(suspendResumeOrder.getMobileShortCodeService().getServiceDetails().getOrderAction())) {

					orderIndex = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()
							.indexOf(suspendResumeOrder);

					break;
				}
			}
		} else  if(orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)
				|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION) 
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
			 
				 for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
						
					 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileBBService().getServiceDetails().getOrderAction())){
						
						 orderIndex = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().indexOf(suspendResumeOrder);
						 
						 break;
					 }
				}
				 
		}
		 return orderIndex;
	 }
	 
	 public static String getSuspensionDuration(final OrderEntryRequest request, final String orderAction){
		 
		 String suspensionDuration = null;
		 
		 if(orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)
					|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
					|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) 
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
		 
			 for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
					
				 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileService().getServiceDetails().getOrderAction())){
					
					 suspensionDuration = suspendResumeOrder.getMobileService().getServiceDetails().getSuspensionDuration();
					 
					 break;
				 }
			}
		 } else if (orderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION) ) {

			for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder()
					.getListOfOEPOrder().getOEPOrder()) {

				if (orderAction
						.equalsIgnoreCase(suspendResumeOrder.getBulkSMSService().getServiceDetails().getOrderAction())) {

					suspensionDuration = suspendResumeOrder.getBulkSMSService().getServiceDetails()
							.getSuspensionDuration();

					break;
				}
			}
		} else if (orderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)) {

			for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder()
					.getListOfOEPOrder().getOEPOrder()) {

				if (orderAction
						.equalsIgnoreCase(suspendResumeOrder.getMobileShortCodeService().getServiceDetails().getOrderAction())) {

					suspensionDuration = suspendResumeOrder.getMobileShortCodeService().getServiceDetails()
							.getSuspensionDuration();

					break;
				}
			}
		} else if (orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)
				|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION) 
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
	 
			 for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
					
				 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileBBService().getServiceDetails().getOrderAction())){
					
					 suspensionDuration = suspendResumeOrder.getMobileBBService().getServiceDetails().getSuspensionDuration();
					 
					 break;
				 }
			}
		}
		 return suspensionDuration;
	 }
	 
 public static String getServiceSuspensionDescription(final OrderEntryRequest request, final String orderAction){
		 
		 String suspensionDescription = null;
		 
		 if(orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)
					|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
					|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) 
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
		 
			 for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
					
				 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileService().getServiceDetails().getOrderAction())){
					
					 suspensionDescription = suspendResumeOrder.getMobileService().getServiceDetails().getServiceSuspensionDescription();
					 
					 break;
				 }
			}
		 } else if (orderAction.equals(OEPConstants.OEP_SUSPEND_BULKSMS_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_RESUME_BULKSMS_SERVICE_ACTION) ) {

			for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder()
					.getListOfOEPOrder().getOEPOrder()) {

				if (orderAction
						.equalsIgnoreCase(suspendResumeOrder.getBulkSMSService().getServiceDetails().getOrderAction())) {

					suspensionDescription = suspendResumeOrder.getBulkSMSService().getServiceDetails()
							.getServiceSuspensionDescription();

					break;
				}
			}
		} else if (orderAction.equals(OEPConstants.OEP_SUSPEND_SHORTCODE_SERVICE_ACTION)) {

			for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder()
					.getListOfOEPOrder().getOEPOrder()) {

				if (orderAction
						.equalsIgnoreCase(suspendResumeOrder.getMobileShortCodeService().getServiceDetails().getOrderAction())) {

					suspensionDescription = suspendResumeOrder.getMobileShortCodeService().getServiceDetails()
							.getServiceSuspensionDescription();

					break;
				}
			}
		} else if(orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)
				|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION) 
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
	 
			 for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
					
				 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileBBService().getServiceDetails().getOrderAction())){
					
					 suspensionDescription = suspendResumeOrder.getMobileBBService().getServiceDetails().getServiceSuspensionDescription();
					 
					 break;
				 }
			}
		}
		 return suspensionDescription;
	 }
 
 public static String getSuspensionDealName(final OrderEntryRequest request, final String orderAction){
	 
	 String suspensionDealName = null;
	 
	 if(orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION)
				|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
				|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION) 
				|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
	 
		 for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
				
			 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileService().getServiceDetails().getOrderAction())){
				
				 suspensionDealName = suspendResumeOrder.getMobileService().getServiceDetails().getDealName();
				 
				 break;
			 }
		}
	 }  else if(orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION)
			|| orderAction.equals(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
			|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_ACTION)
			|| orderAction.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
			|| orderAction.equalsIgnoreCase(OEPConstants.OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION) 
			|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION)){
 
		 for (SuspendOrderServiceType suspendResumeOrder : request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder()){
				
			 if(orderAction.equalsIgnoreCase(suspendResumeOrder.getMobileBBService().getServiceDetails().getOrderAction())){
				
				 suspensionDealName = suspendResumeOrder.getMobileBBService().getServiceDetails().getDealName();
				 
				 break;
			 }
		}
	}
	 return suspensionDealName;
 }
}
