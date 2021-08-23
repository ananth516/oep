package com.oracle.oep.order.ebm.transform;

import java.sql.Timestamp;
import java.util.Date;

import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.utils.OrderDataUtils;

public class ProcessDisconnectOrderEbmHelper {
	
	 public static String getCsrId(final OrderEntryRequest request, final String oepOrderAction){
			
			String csrId = null;
			
			if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
					||oepOrderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION))
				csrId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getOrderUserId();
			else if(oepOrderAction.equals(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)){
				csrId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getOrderUserId();
				}
			else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE))
				csrId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getServiceDetails().getOrderUserId();
			else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION))
				csrId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getServiceDetails().getOrderUserId();
			else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				csrId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getOrderUserId();
			}else if (oepOrderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION)) {
				csrId = OEPConstants.BRM_CHANNEL_NAME;
			}
			else
				csrId = OEPConstants.OAP_CHANNEL_NAME;
			return csrId;
		}
	 
	  
	 public static String getOrderActionFromRequest(final OrderEntryRequest request, final String oepOrderAction){
			
			String orderActionFromRequest = null;
			
			if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION)  || oepOrderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
					||oepOrderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
				
				orderActionFromRequest = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getOrderAction();
			}else if(oepOrderAction.equals(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)){
				orderActionFromRequest = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getOrderAction();
				}
			else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
				orderActionFromRequest = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getServiceDetails().getOrderAction();
				}			
			else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)){
				orderActionFromRequest = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getServiceDetails().getOrderAction();
				}
			else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				orderActionFromRequest = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getOrderAction();
			}
				
			return orderActionFromRequest;
		}
	 
	 public static String getEarlyTerminationFlag(final OrderEntryRequest request, final String oepOrderAction){
			
			String earlyTerminationFlag = null;
			
			if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION)  || oepOrderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE) 
					|| oepOrderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION)
					|| oepOrderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
				
				earlyTerminationFlag = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getEarlyTerminationFlag();
			} else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
				
				earlyTerminationFlag = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getEarlyTerminationFlag();
			} else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				earlyTerminationFlag = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getEarlyTerminationFlag();
			}  else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)){
				earlyTerminationFlag = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getEarlyTerminationFlag();
			}
				
			return earlyTerminationFlag;
		}
	 
	 public static Timestamp getOrderStartDate(final OrderEntryRequest request, final String oepOrderAction){
			
			Timestamp orderStartDate=null; 
			if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
					||oepOrderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
				
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getOrderStartDate());
				
			}else if(oepOrderAction.equals(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)){
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getOrderStartDate());
				}
			else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getServiceDetails().getOrderStartDate());
				}			
			else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)){
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getServiceDetails().getOrderStartDate());
				}
			else if(oepOrderAction.equals(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getOrderStartDate());
			}
			return orderStartDate;
			
		}
	 
	 public static String getServiceNo(final OrderEntryRequest request,final String orderAction){
			
			String serviceNo=null;		
					
			if(orderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION)  || orderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
					||orderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
				serviceNo = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getServiceIdentifier();
			}	
			else if(orderAction.equals(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)){
				serviceNo = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getServiceIdentifier();
			}	
			else if(orderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
				serviceNo = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getServiceDetails().getServiceIdentifier();
			}
			else if(orderAction.equals(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				serviceNo = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getServiceIdentifier();
			}
			else if(orderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION)) {
				serviceNo = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getServiceNo();
			}
			
			return serviceNo;
		}
	 
	 
	 public static String getUIMServiceIdentifier(final OrderEntryRequest request,final String orderAction){
			
			String serviceIdentifer = null;
			
			if(orderAction.equals(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)){
				
				serviceIdentifer = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getServiceDetails().getServiceIdentifier();
			}
			else if(orderAction.equals(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				serviceIdentifer = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getServiceIdentifier();
			}
			else if(orderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION)) {
				serviceIdentifer = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getServiceIdentifier();
			}
			return serviceIdentifer;
		} 
	 
	public static String getChannelName(final OrderEntryRequest request,final String orderAction ){
		 
		 String channelName = null;
		 
		if (orderAction.equals(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)) {
			channelName = OEPConstants.NPMS_CHANNEL_NAME;
		} else if (orderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION)) {
			channelName = OEPConstants.BRM_CHANNEL_NAME;
		}
		else if(orderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION)){
			
			if(request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getOrderName().equals(OEPConstants.BRM_PREPAID_LIFECYCLE_TERMINATE_SERVICE_ACTION))
			{
				channelName = OEPConstants.BRM_CHANNEL_NAME;
			}
			
			else 
				channelName = OEPConstants.OAP_CHANNEL_NAME;
		}
		else {
			
			channelName = OEPConstants.OAP_CHANNEL_NAME;
		}
		 
		 
		 return channelName;
		 
	}
	
	public static String getChannelTrackingId(final OrderEntryRequest request,final String orderAction ){
		 
		 String channelTrackingId = null;
		 
		if (orderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
				||orderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)) {

			channelTrackingId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService()
					.getServiceDetails().getChannelTrackingId();

		} else if (orderAction.equals(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)) {
			channelTrackingId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService()
					.getServiceDetails().getChannelTrackingId();
		} else if (orderAction.equals(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)) {
			channelTrackingId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder()
					.getMobileBBService().getServiceDetails().getChannelTrackingId();
		} else if (orderAction.equals(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)) {
			channelTrackingId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getServiceDetails().getChannelTrackingId();
		} else if(orderAction.equals(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
			channelTrackingId = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getChannelTrackingId();
		}
		 
		 
		 return channelTrackingId;
		 
	}
	
	public static String getPlanAction(final OrderEntryRequest request, final String orderAction ){
			
			String planAction=null;		
					
			if(orderAction.equalsIgnoreCase(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)){
				
				planAction=OEPConstants.OSM_PORTOUT_ACTION_CODE;
			} else if (orderAction.equalsIgnoreCase(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)) {
				planAction = OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE;
			} else if (orderAction.equalsIgnoreCase(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)) {
				planAction = OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE;
			}				
			else {
				
				planAction=OEPConstants.OSM_DISCONNECT_ACTION_CODE;
			}	
			return planAction;
		}	 
	
	 public static String getAccountName(final OrderEntryRequest request,final String orderAction, final CUSOPSEARCHOutputFlist searchResult ){
			
			String accountName=null;		
					
			if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
					||orderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
				
				accountName=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getAccountNo();
			}else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)){
				
				accountName=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getAccountNo();
			}else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				
				accountName=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getAccountNo();
			}else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
				
				accountName=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getServiceDetails().getAccountNo();
			} else if (orderAction.equalsIgnoreCase(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION)) {
				accountName = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getAccountNo();
			}
				
			return accountName;
		}
	 
	 public static String getWaveOff(final OrderEntryRequest request,final String orderAction){
			
			String waveOff=null;		
					
			if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
					||orderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
				
				waveOff=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getWaveOff();
			} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
				
				waveOff=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getWaveOff();
			} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				
				waveOff=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getWaveOff();
			}  else if (orderAction.equals(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)) {
				waveOff=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getWaveOff();
			}
			return waveOff;
		}
	 
	 public static String getWaveOffReason(OrderEntryRequest request, String orderAction) {
			
		 	String waveOffReason=null;		
			
			if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
					||orderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
				
				waveOffReason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getWaveOffReason();
			} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
				
				waveOffReason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getWaveOffReason();
			} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				
				waveOffReason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getWaveOffReason();
			} else if (orderAction.equals(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)) {
				waveOffReason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getWaveOffReason();
			
			}
				
			return waveOffReason;
		}
	 
	 public static String getBillingAccountId(final String orderAction, final CUSOPSEARCHOutputFlist searchResult ){			
						
			return searchResult.getRESULTS().get(0).getCUSFLDBAACCOUNTNO();
		}
	 
	 public static String getCustomerAccountId(final String orderAction, final CUSOPSEARCHOutputFlist searchResult ){			
							
			return searchResult.getRESULTS().get(0).getCUSFLDCAACCOUNTNO();
		}


	public static String getTerminationCharge(OrderEntryRequest request, String orderAction) {

		String terminationCharge=null;		
		
		if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
				||orderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
			
			terminationCharge=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getTerminationCharge();
		}else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
			
			terminationCharge=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getTerminationCharge();
		}else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
			
			terminationCharge=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getTerminationCharge();
		}else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)){
			
			terminationCharge=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getTerminationCharge();
		}
			
		return terminationCharge;
		
	}
	
	public static String getTerminationReason(OrderEntryRequest request, String orderAction) {

		String terminationReason=null;		
		
		if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
				|orderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
			
			terminationReason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getReason();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || orderAction.equalsIgnoreCase(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
			
			terminationReason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getServiceDetails().getReason();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)){
			
			terminationReason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getServiceDetails().getReason();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
			
			terminationReason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getReason();
		}
			
		return terminationReason;
		
	}
	
	public static String getPortType(OrderEntryRequest request, String orderAction) {

		String portType=null;		
		
		if(orderAction.equalsIgnoreCase(OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION)){
			
			portType=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getPortType();
		}
		else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION)){
			
			portType=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getPortType();
		}
			
		return portType;
		
	}
	
	 public static String getReason(final OrderEntryRequest request, final String oepOrderAction){
			
			String reason = null;
			
			if(oepOrderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)) {
				reason = request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getReason();
			} else if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				reason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getReason();
			} else if(oepOrderAction.equals(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)) {
				reason=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getServiceDetails().getReason();
			}
			
			return reason;
		}
	 
	 public static String getTerminationNotes(final OrderEntryRequest request, final String oepOrderAction){
		 
		 String terminationNotes = null;
		 
		 if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_CHANGE_OWNERSHIP_TERMINATE)
				 ||oepOrderAction.equals(OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION)){
				
			 terminationNotes=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getServiceDetails().getTerminationNotes();
			}
			else if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION) || oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE)){
				
				terminationNotes=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getServiceDetails().getTerminationNotes();
			}
			else if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION)){
				
				terminationNotes=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileShortCodeService().getServiceDetails().getTerminationNotes();
			}
			else if(oepOrderAction.equalsIgnoreCase(OEPConstants.OEP_DISCONNECT_BULK_SMS_ACTION)){
				
				terminationNotes=request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getServiceDetails().getTerminationNotes();
			}
		 
		 return terminationNotes;
		 
	 }
	 

}
