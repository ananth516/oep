package com.oracle.oep.order.ebm.transform;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.oracle.oep.brm.opcodes.CUSFLDADDONPKG;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.AddActionPlanType;
import com.oracle.oep.order.model.AddonActionEnum;
import com.oracle.oep.order.model.ChangePlanActionPlanType;
import com.oracle.oep.order.model.DepositPaymentType;
import com.oracle.oep.order.model.OptionsEnumType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ProcessChangePlanReqType;
import com.oracle.oep.order.model.SIMDetailsType;
import com.oracle.oep.order.utils.OrderDataUtils;

public class ProcessModifyOrderEbmHelper {
	
	 public static String getCsrId(final OrderEntryRequest request, final String oepOrderAction){
			
			String csrId = null;
			
			if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getOrderUserId();
			} 
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAddRemoveAddon().getOrderUserId();
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeSim().getOrderUserId();
				
			} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeSim().getOrderUserId();

			}  else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getOrderUserId();
				
			} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getOrderUserId();
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getOrderUserId();
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getOrderUserId();
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getOrderUserId();
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAddRemoveAddon().getOrderUserId();
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getOrderUserId();
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangePlan().getOrderUserId();
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getOrderUserId();
			}
			
			else if(oepOrderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)){
				
				csrId =  request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getOrderUserId();
			}
			
			else if(oepOrderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
				
				csrId = request.getProcessAddOnManagment().getChannelName();
			}
			
			else if(oepOrderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)){
				
				csrId = request.getProcessChangePlan().getChannelName();
			}
			
			else if(oepOrderAction.equals(OEPConstants.OEP_ACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION) 
					|| oepOrderAction.equals(OEPConstants.OEP_DEACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION)){
				
				csrId = request.getProcessCallDivertManagment().getChannelName();
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)){
				
				csrId = request.getProcessCugOrder().getChannelName();
			}		
			else if(oepOrderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
				
				csrId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getFca().getChannelName();
			}
			
			else {
				
				csrId = OEPConstants.BRM_CUS_SEARCH_USER;
			}
						
			
			return csrId;
		}
		
	 public static String getOrderActionFromRequest(final OrderEntryRequest request, final  String oepOrderAction){
			
			String orderActionFromRequest = null;
			
			if(oepOrderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
				
				orderActionFromRequest = OEPConstants.OEP_ADDON_MANAGEMENT_ACTION;
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)) {
				
				orderActionFromRequest = OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN;
			}
			
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
				
				orderActionFromRequest = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getOrderAction();
			}
				
			return orderActionFromRequest;
		}
	 
	 public static Timestamp getOrderStartDate(final OrderEntryRequest request, final String oepOrderAction){
			
			Timestamp orderStartDate=null; 
			
			if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
				
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getOrderStartDate());
			}
			
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)){
				
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAddRemoveAddon().getOrderStartDate());
			}
			
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)){
				
				orderStartDate =  OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getOrderStartDate());
			}
			else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)) {
				
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeSim().getOrderStartDate());
				
			} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)) {
				
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeSim().getOrderStartDate());


			}  else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)) {
				
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getOrderStartDate());
				
			} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)) {

			orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getOrderStartDate());
			
			} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)) {
				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getOrderStartDate());

			} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)) {

				orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getOrderStartDate());
			} else if(oepOrderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)){
				
				orderStartDate =  OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getOrderStartDate());
			} else if(oepOrderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)){
				
				orderStartDate =  OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangePlan().getOrderStartDate());
			} else if(oepOrderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)){
				
				orderStartDate =  OrderDataUtils.getTimeStampFromCalender(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getOrderStartDate());
			}
			
			
			else {
				
				orderStartDate = new Timestamp( new Date().getTime());
			}
			
			return orderStartDate;
			
		}
	 
	 public static String getServiceNo(final OrderEntryRequest request,final String orderAction){
			
			String serviceNo=null;		
					
			if(orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
				serviceNo = request.getProcessAddOnManagment().getServiceIdentifier();
			}
			
			else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)){
				
				serviceNo = request.getProcessChangePlan().getServiceIdentifier();
				
			}
			
			else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)){
				
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getMsIsdn();
				
			}
			
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getMsIsdn();
			}
			
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAddRemoveAddon().getMsIsdn();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeSim().getMsIsdn();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeSim().getMsIsdn();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getMsIsdn();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getMsIsdn();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getMsIsdn();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getServiceIdentifier();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAddRemoveAddon().getServiceIdentifier();
			}
			else if(orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getServiceIdentifier();
			}
			else if(orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangePlan().getMsIsdn();
			}
			else if(orderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getServiceIdentifier();
			}

			else if(orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)) {
				serviceNo = request.getProcessCugOrder().getServiceNumber();
			}
			else if(orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
				serviceNo = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getFca().getMsIsdn();
			}
			
			return serviceNo;
		}
	
	public static String getUIMServiceIdentifier(final OrderEntryRequest request,final String orderAction){
		
		String serviceIdentifer=null;
		
		if(orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)){
			
			serviceIdentifer = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getServiceIdentifier();
		} else 	if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)){
			
			serviceIdentifer = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getServiceIdentifier();
		} else 	if(orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)){
			
			serviceIdentifer = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangePlan().getServiceIdentifier();
		} else 	if(orderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)){
			
			serviceIdentifer = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getServiceIdentifier();
		} else 	if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)){
			
			serviceIdentifer = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAddRemoveAddon().getServiceIdentifier();
		}
		
		return serviceIdentifer;
	} 
	
	public static String getServiceId(final OrderEntryRequest request, final String orderAction) {
		String serviceId = null;

		if (orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)) {
			serviceId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService()
					.getChangeSim().getServiceIdentifier();
		} else if (orderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)) {
			serviceId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService()
					.getChangeSim().getServiceIdentifier();
		} else if (orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)) {
			serviceId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService()
					.getChangeMsIsdn().getServiceIdentifier();
		} else if (orderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)) {
			serviceId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService()
					.getChangeMsIsdn().getServiceIdentifier();
		} else if (orderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)) {
			serviceId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService()
					.getChangeShortCodeNumber().getServiceIdentifier();
		}

		return serviceId;
	}
		
	 public static String getOrderServiceType(final OrderEntryRequest request,final String orderAction ){
		 
		 String serviceType = null;
		 
		if (orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)) {
			
			if(request.getProcessAddOnManagment().getServiceType()!=null)
				serviceType = request.getProcessAddOnManagment().getServiceType().value();
		} 
		else if (orderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)) {
			
			serviceType = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService()
					.getAddRemoveAddon().getOrderServiceType();
		}
		else if (orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)) {
			serviceType = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService()
					.getChangeSim().getOrderServiceType();
		} 
		else if (orderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)) {
			serviceType = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService()
					.getChangeSim().getOrderServiceType();
		} 
		else if (orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)) {
			serviceType = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService()
					.getChangeMsIsdn().getOrderServiceType();
		} 
		else if (orderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)) {
			serviceType = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService()
					.getChangeMsIsdn().getOrderServiceType();
		}
		
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
			
			serviceType = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getOrderServiceType();				
			
		} else if(orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)) {
			serviceType = OEPConstants.OEP_MOBILE_SERVICE_TYPE;
			
		} else if(orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
			serviceType = OEPConstants.OEP_MOBILE_SERVICE_TYPE;
		}
		 
		 
		 return serviceType;
		 
	 }
	 
	 public static String getBasePackageName(final OrderEntryRequest request,final RESULTS searchResult, final String orderAction ){
		 
		 String basePackageName = null;
		 
		 if(orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)){
			 
			 basePackageName = request.getProcessChangePlan().getNewPackageName();
			
		 }
		 
		 else {		 
							
			 basePackageName = searchResult.getCUSFLDPLANNAME();				
			 
			 
		 }	 
		 
		 return basePackageName;
		 
	 }

	public static String getAddonPackageName(final OrderEntryRequest request,final String orderAction ){
	 
	 String addonPackageName = null;
	 
	 if(orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
		 
		 addonPackageName = request.getProcessAddOnManagment().getAddOnPackageName();
		
	 }
	 
	 
	 return addonPackageName;
	 
	}
	
	public static String getDepositReceiptNumber(final OrderEntryRequest request,final String orderAction ){
		 
		 String depositReceiptNumber = null;
		 
		 if(orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
			 
			 depositReceiptNumber = request.getProcessAddOnManagment().getDepositReceiptNo();
			
		 }
		 
		 
		 return depositReceiptNumber;
		 
	}
	
	public static List<DepositPaymentType> getDepositsFromOrder(final OrderEntryRequest request,final String orderAction){
		
		List<DepositPaymentType> deposits = null;		
			
		if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)) {
			
			deposits = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAddRemoveAddon().getDepositPayment();
		}
		
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)) {
			
			deposits = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getDepositPayment();
		}
		
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)) {
			
			deposits = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getDepositPayment();
		}
		
		else if(orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)) {
			
			deposits = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangePlan().getDepositPayment();
		}
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)) {
			
			deposits = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAddRemoveAddon().getDepositPayment();
		}		
		
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)) {
			
			deposits = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getDepositPayment();
		
		}
		else if(orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)){
			
			ProcessChangePlanReqType changePlan = request.getProcessChangePlan();

			DepositPaymentType depositPayment = new DepositPaymentType();
			
			if(changePlan.getDepositType()!=null || changePlan.getDepositReceiptNo()!=null || changePlan.getCollectedOn()!=null || changePlan.getDepositAmount() != null)
			{	
			
			if(changePlan.getDepositAmount()!=null)
			{
			depositPayment.setDepositAmount(changePlan.getDepositAmount());
			}
			if(changePlan.getDepositReceiptNo()!=null)
			{
			depositPayment.setDepositReceiptNo(changePlan.getDepositReceiptNo());
			}
			if(changePlan.getDepositType()!=null)
			{
			depositPayment.setDepositType(changePlan.getDepositType());
			}
			if(changePlan.getNewPackageName()!=null)
			{
			depositPayment.setPlanName(changePlan.getNewPackageName());
			}
			if(changePlan.getCollectedOn()!=null)
			{
			depositPayment.setCollectedOn(changePlan.getCollectedOn());
			}
			if(changePlan.getServiceType()!=null)
			{
			depositPayment.setServiceType(changePlan.getServiceType());
			}
			
			deposits = new ArrayList<DepositPaymentType>();			
			deposits.add(depositPayment);
			
			}
			
		}
		
		return deposits;
	}
	
	public static String getChannelName(final OrderEntryRequest request,final String orderAction ){
		 
		 String channelName = null;
		 
		 if(orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
			 
			 channelName = request.getProcessAddOnManagment().getChannelName();
			
		 }		 
		 
		 else if(orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)){
			 
			 channelName = request.getProcessChangePlan().getChannelName();
			
		 }		 
		 
		 else if(orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)) {
			 
				channelName = request.getProcessCugOrder().getChannelName();
				
		 }
		 
		 else if(orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)) {
			 
				channelName = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getFca().getChannelName();
				
		 }
		 
		 else if(orderAction.equals(OEPConstants.OEP_ACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION) 
					|| orderAction.equals(OEPConstants.OEP_DEACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION)){
				
			 channelName = request.getProcessCallDivertManagment().getChannelName();
			}
		 
		 else {
			 
			 channelName = OEPConstants.OAP_CHANNEL_NAME;
		 }
		 		 
		 return channelName;
		 
	}
	
	public static String getChannelTrackingId(final OrderEntryRequest request,final String orderAction ){
		 
		 String channelTrackingId = null;
		 
		if (orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)) {
			
			channelTrackingId = request.getProcessAddOnManagment().getChannelTrackingId();
		} 
		else if (orderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)) {
			
			channelTrackingId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAddRemoveAddon().getChannelTrackingId();
		}
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)) {
			channelTrackingId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService()
					.getChangeSim().getChannelTrackingId();
		} 
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)) {

			channelTrackingId = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService()
					.getChangeSim().getChannelTrackingId();
		}
		else if(orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)) {
			channelTrackingId = request.getProcessCugOrder().getChannelTrackingId();
		}
		 
		 
		 return channelTrackingId;
		 
	}
	
	 public static String getPlanAction(final OrderEntryRequest request, final OEPConfigPlanAttributesT planAttributes, final String orderAction ){
			
			String planAction=null;		
					
			if(orderAction.equalsIgnoreCase(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)
					|| orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)){
				
				if(planAttributes.getPackageType().equalsIgnoreCase(OEPConstants.OEP_BASE_PLAN_TYPE)) {
					
					planAction=OEPConstants.OSM_MODIFY_ACTION_CODE;
					  
				}
				
				else {
						 planAction = getPlanAction(request, orderAction);
				}
			}		
				
			return planAction;
		}
	 
	 public static String getPlanAction(final OrderEntryRequest request, final String orderAction ){
			
			String planAction=null;		
					
			if(orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
				
				planAction=request.getProcessAddOnManagment().getAction().toString();
				
				if(planAction.equalsIgnoreCase(AddonActionEnum.ADD.toString())){
					
					planAction=OEPConstants.OSM_ADD_ACTION_CODE;
				}
				
				else if(planAction.equalsIgnoreCase(AddonActionEnum.REMOVE.toString())){
					
					planAction=OEPConstants.OSM_REMOVE_ACTION_CODE;
				}
				
				
			}		
				
			return planAction;
		}
		
	 public static BigDecimal getOverridenPrice(final OrderEntryRequest request,final String orderAction,final String planName){
			
			BigDecimal overridenPrice  = null;	

			List<ChangePlanActionPlanType> plans = null;
			

			if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION) ) {
				

				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getPlan();
				
			}else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)) {
				

				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getPlan();
				
			}
			else if(orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)) {
				

				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getPlan();
				
			}
			
			else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
				
				return request.getProcessAddOnManagment().getOverridenPrice();
			}
			
			
			if(plans!=null){
				
				for (ChangePlanActionPlanType plan : plans){
					
					if(plan.getPlanName().equals(planName)){
						overridenPrice= plan.getOverridenPrice();
						break;
					}
				}
			
			}
		
			return overridenPrice;
		} 
	 
	 public static List<ChangePlanActionPlanType> getPlanList(final OrderEntryRequest request, final String orderAction) {
		 List<ChangePlanActionPlanType> planList = null;
		 
		 if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)){
			 
			 planList = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAddRemoveAddon().getPlan();

		 }
		 
		 return planList;
	 }
	 
	 public static XMLGregorianCalendar getPlanStartDate(final OrderEntryRequest request,final String orderAction,final String planName){

			
			XMLGregorianCalendar planStartDate = null;	
			List<ChangePlanActionPlanType> plans = null;
				
			if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)) {
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getPlan();
				
			}
			else if(orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)) {
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getPlan();
				
			}
			else if(orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)) {
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangePlan().getPlan();
				
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getPlan();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)){
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getPlan();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)){
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAddRemoveAddon().getPlan();
			}
			
			if(plans!=null) {
				
				for (ChangePlanActionPlanType plan : plans){
	
					
					if(plan.getPlanName().equals(planName)){
						planStartDate= plan.getPlanStartDate();
						break;
					}
				}
			
			}
			
			return planStartDate;
		}
		
		
		public static XMLGregorianCalendar getPlanEndDate(final OrderEntryRequest request,final String orderAction,final String planName){
			
			XMLGregorianCalendar planEndDate = null;
			List<ChangePlanActionPlanType> plans = null;
			
			if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)) {
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getPlan();			

			}
			else if(orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)) {
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getPlan();			

			}
			else if(orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)) {
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangePlan().getPlan();
				
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getPlan();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)){
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getPlan();
			}
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)){
				
				plans=request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAddRemoveAddon().getPlan();
			}			
			if(plans!=null) {
			
				for (ChangePlanActionPlanType plan : plans){
					
					if(plan.getPlanName().equals(planName)){
						planEndDate= plan.getPlanEndDate();
						break;
					}
				}
				
			}
			
			return planEndDate;

		}

	public static String getAccountId(final OrderEntryRequest request,final String orderAction){
			
			String accountID=null;	
			
			if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getAccountNo();
				
			} 
			
			else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)) {
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAddRemoveAddon().getAccountNo();
			}
			
			else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)){
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getAccountNo();
			
			}
			else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)) {
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeSim().getAccountNo();
				
			} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)) {
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeSim().getAccountNo();
				
			} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)) {
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getAccountNo();

			} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)) {
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getAccountNo();

			} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)) {
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getAccountNo();

			} else if(orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)) {
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getAccountNo();
			} else if(orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)){
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getAccountNo();
			
			} else if(orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)){
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangePlan().getAccountNo();
			
			} else if(orderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)){
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getAccountNo();
			
			} 
			else if(orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)) {
				
				accountID = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getFca().getAccountNo();
			}
				
			return accountID;
		}
	
	 public static String getAccountId(final OrderEntryRequest request,final String orderAction, final RESULTS searchResult ){
			
			String accountId=null;		
			
			if(searchResult!=null){				
						
				accountId = searchResult.getACCOUNTNO();				
				
			}
			
			else {
				
				accountId = getAccountId(request, orderAction);
			}
				
				
			return accountId;
		}
	 
	 public static String getBillingAccountId(final OrderEntryRequest request,final String orderAction, final RESULTS searchResult ){
			
			String billingAccountId=null;		
			
			if(orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)
					|| orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)
					|| orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)
					|| orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)
					|| orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
				
			
						
				billingAccountId = searchResult.getCUSFLDBAACCOUNTNO();
					
			}
				
			return billingAccountId;
		}
	 
	 public static String getCustomerAccountId(final OrderEntryRequest request,final String orderAction, final RESULTS searchResult ){
			
			String customerAccountId=null;		
			if(orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)
					|| orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)
					|| orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)
					|| orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)
					|| orderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)
					|| orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
				
										
				customerAccountId = searchResult.getCUSFLDCAACCOUNTNO();
				
			}		
				
			return customerAccountId;
	}
	
	 public static String getAddOnPackageId(final OrderEntryRequest request,final RESULTS searchResult, final String planName ){
			
			String packageId=null;				
			
			for	(CUSFLDADDONPKG addOnPackage : searchResult.getCUSFLDADDONPKG() ){
				
				if(addOnPackage.getCUSFLDADDONPLANNAME().equalsIgnoreCase(planName)){
					packageId = addOnPackage.getPACKAGEID();
					break;
				}
			}
				
			return packageId;
	}
	 
	 public static String getAddOnPackageId(final OrderEntryRequest request,final RESULTS searchResult, final String planName, List<String> planPackage){
			
			String packageId=null;				
			
			for (String planPack : planPackage){
				String[] words=planPack.split("\\^");
	            if(planName.equalsIgnoreCase(words[0])){
	            	packageId = words[1];   
	            	planPackage.remove(planPack);          	   
	                   break;
	            }
			}
			
				
			return packageId;
	} 

	public static List<ChangePlanActionPlanType> getPlansFromOrder( final OrderEntryRequest request, final String oepOrderAction) {
		// TODO Auto-generated method stub
		List<ChangePlanActionPlanType> addOnPlans = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)){
			
			addOnPlans = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getPlan();
		} 
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)) {
			
			addOnPlans = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAddRemoveAddon().getPlan();
		}		
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)) {
			
			addOnPlans = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAddRemoveAddon().getPlan();
		}		
		return addOnPlans;
	}



	public static String getOldSim(OrderEntryRequest request, String oepOrderAction) {
		// TODO Auto-generated method stub
		String oldSim = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)){
			oldSim = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeSim().getOldSimNo();
		} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)){
			oldSim = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeSim().getOldSimNo();
		}
		
		return oldSim;
	}

	


	public static String getSimChangeReason(OrderEntryRequest request, String oepOrderAction) {
		// TODO Auto-generated method stub
		String simChangeReason = null;
		



		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)){

			simChangeReason = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeSim().getChangeReason();

		} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)){
			simChangeReason = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeSim().getChangeReason();
		}
		
		return simChangeReason;
	}



	public static SIMDetailsType getSimDetails(OrderEntryRequest request, String oepOrderAction) {
		
		SIMDetailsType simDetails = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)){
			simDetails = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeSim().getSIMDetails();
		} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)){
			simDetails = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeSim().getSIMDetails();
		}
		
		return simDetails;
	}

	


	public static String getWaiverOptions(OrderEntryRequest request, String oepOrderAction) {
		// TODO Auto-generated method stub
		
		OptionsEnumType waiverOptions = null;
		



		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)){

			waiverOptions = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeSim().getOptions();
		} 
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)){
			
			waiverOptions = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeSim().getOptions();
		}
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)){
			
			waiverOptions = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getOptions();
		}
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)){
			
			waiverOptions = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getOptions();
		}
		 
		return waiverOptions.value();
	}


	public static String getWaiverReason(OrderEntryRequest request, String oepOrderAction) {
		// TODO Auto-generated method stub
		
		String waiverReason = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)){
			waiverReason = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeSim().getWaiverReason();


		} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)){

			waiverReason = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeSim().getWaiverReason();
		}
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)){

			waiverReason = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getWaiverReason();
		}
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)){

			waiverReason = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getWaiverReason();
		}





		
		return waiverReason;
	}



	public static String getOldServiceNo(OrderEntryRequest request, String oepOrderAction) {
		// TODO Auto-generated method stub
		String oldMsisdn = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)){
			oldMsisdn = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getOldMsIsdn();
		}
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)){
			oldMsisdn = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getOldMsIsdn();
		}
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE)){
			oldMsisdn = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getOldShortCodeNumber();
		}
		
		return oldMsisdn;
	}


	public static String getNiceCategory(final OrderEntryRequest request,final String oepOrderAction){
		
		String niceCategory = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE))
		{
			if( request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getNiceCategory()!=null)
				niceCategory = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getNiceCategory().toString();
		} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)) {
			if( request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getNiceCategory()!=null)
				niceCategory = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getNiceCategory().toString();
		}

		
		return niceCategory;






		

	}
	
	public static String getNiceCategoryValue(final OrderEntryRequest request,final String oepOrderAction){
		
		String niceCategoryValue = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE))
		{
			if( request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getNiceCategory()!=null)				
				niceCategoryValue = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getNiceCategoryValue();
				
		} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)) {
			
			if( request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getNiceCategory()!=null)			
				niceCategoryValue = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getNiceCategoryValue();
		}
		
		return niceCategoryValue;
		
	}


	public static String getMsisdnChangeReason(OrderEntryRequest request, String oepOrderAction) {
		// TODO Auto-generated method stub
		String msisdnChangeReason = null;

		
		if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)){
			msisdnChangeReason = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangeMsIsdn().getChangeReason();
		}
		else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)){

			msisdnChangeReason = request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangeMsIsdn().getChangeReason();
		}

		
		return msisdnChangeReason;
	}


	public static String getMsisdnFlag(OrderEntryRequest request, String oepOrderAction) {
		// TODO Auto-generated method stub
		return OEPConstants.OEP_CONSTANT_Y;

	}
	
	 public static List<ChangePlanActionPlanType> getChangePlansFromOrder(final OrderEntryRequest request,final String oepOrderAction){
			
		 List<ChangePlanActionPlanType> changePlanList = null;
			
			if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION) )
			{
				changePlanList =  request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getPlan();
			} 
			else if(oepOrderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)) {
				
				changePlanList = new ArrayList<ChangePlanActionPlanType>();
				
				ChangePlanActionPlanType newBasePlan = new ChangePlanActionPlanType();
				
				newBasePlan.setPlanName(request.getProcessChangePlan().getNewPackageName());								
				
				changePlanList.add(newBasePlan);
				
			} else if(oepOrderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN) )
			{
				changePlanList =  request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getPlan();
			} else if(oepOrderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION) )
			{
				changePlanList =  request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getPlan();
			} else if(oepOrderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION) )
			{
				changePlanList =  request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangePlan().getPlan();
			}
			
			return changePlanList; 
		
		}
	 
	 
	 public static BigDecimal getCreditScores(final OrderEntryRequest request,final String orderAction){
			
		 BigDecimal creditScore = null;
			
			if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION) && (request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getCreditScore()!=null
					&& !"".equals(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getCreditScore()))) {
				
				creditScore = new BigDecimal(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getCreditScore());				
			} else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN) && (request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getCreditScore()!=null
					&& !"".equals(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getCreditScore()))) {
				
				creditScore = new BigDecimal(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getCreditScore());				
			}
			
			
			return creditScore;
		}
		
		
	public static BigDecimal getSpendingCap(final OrderEntryRequest request,final String orderAction){
		
		BigDecimal spendingCap = null;		
			
		 if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION) && (
				 (request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getSpendingCap()!=null)
				 && !"".equals(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getSpendingCap()))) {
			
			 spendingCap = new BigDecimal(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getSpendingCap());
		}
		 
		 else if(orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION) && 
				 (request.getProcessChangePlan().getSpendingCap()!=null
				 && !"".equals(request.getProcessChangePlan().getSpendingCap()))
				 ){
			 
			 spendingCap = new BigDecimal(request.getProcessChangePlan().getSpendingCap());
		 }
		 else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN) && (
				 request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getSpendingCap()!=null
				 && !"".equals(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getSpendingCap())
				 )){
			 
			 spendingCap = new BigDecimal(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getSpendingCap());
		 }
		
		return spendingCap;

	}
		
	public static BigDecimal getSpendingCapMax(final OrderEntryRequest request,final String orderAction){
		
		BigDecimal spendingCapMax = null;
			
		if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION) && ((
				request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getSpendingCapMax()!=null)
				&& !"".equals(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getSpendingCapMax()))) {
			
			spendingCapMax = new BigDecimal(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getSpendingCapMax());
		}
		else if(orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION) && (
				request.getProcessChangePlan().getSpendingCapMax()!=null
				&& !"".equals(request.getProcessChangePlan().getSpendingCapMax()))){
			 
			spendingCapMax = new BigDecimal(request.getProcessChangePlan().getSpendingCapMax());
		 }
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN) && (request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getSpendingCapMax()!=null
				&& !"".equals(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getSpendingCapMax()))) {

			
			spendingCapMax = new BigDecimal(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getSpendingCapMax());

		}	
		
		return spendingCapMax;
	}	
	
	public static BigDecimal getSpendingCapMin(final OrderEntryRequest request,final String orderAction){
		
		BigDecimal spendingCapMin = null;	
		
		if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION) && (request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getSpendingCapMin()!=null
				&& !"".equals(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getSpendingCapMin()))) {
			
			spendingCapMin = new BigDecimal(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getChangePlan().getSpendingCapMin());
		} else if(orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN) && (request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getSpendingCapMin()!=null
				
				&& !"".equals(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getSpendingCapMin()))) {

			spendingCapMin = new BigDecimal(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getChangePlan().getSpendingCapMin());
		}
		else if(orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION) && (request.getProcessChangePlan().getSpendingCapMin()!=null
				&& !"".equals(request.getProcessChangePlan().getSpendingCapMin()))){
			 
			spendingCapMin = new BigDecimal(request.getProcessChangePlan().getSpendingCapMin());
		}
		return spendingCapMin;
	}
	
	public static String getPriorityCode(final String orderAction) {
		String priorityCode = null;

		if(orderAction != null && orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
			
			priorityCode= OEPConstants.OSM_ORDER_HIGHPRIORITY_CODE_DEFAULT;
			
		}else{
			
			priorityCode= OEPConstants.OSM_ORDER_PRIORITY_CODE_DEFAULT;
			
		}

		return priorityCode;

	}
	 
	 
}
