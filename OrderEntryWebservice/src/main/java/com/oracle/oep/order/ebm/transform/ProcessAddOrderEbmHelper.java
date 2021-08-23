package com.oracle.oep.order.ebm.transform;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.AddActionPlanType;
import com.oracle.oep.order.model.DepositPaymentType;
import com.oracle.oep.order.model.HandSetDetailsType;
import com.oracle.oep.order.model.NotificationLanguageEnum;
import com.oracle.oep.order.model.OptionsEnumType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.SIMDetailsType;
import com.oracle.oep.order.utils.OrderDataUtils;

public class ProcessAddOrderEbmHelper {
	
	public static String getAccountName(final OrderEntryRequest request,final String orderAction){
		
		String accountName=null;		
				
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			accountName = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAccountName();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)) {
			accountName = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAccountName();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			accountName = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getAccountName();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			accountName = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAccountName();
		}
			
		return accountName;
	}
	
	public static String getServiceNo(final OrderEntryRequest request,final String orderAction){
		
		String serviceNo=null;		
				
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
			serviceNo = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getMsIsdn();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			serviceNo = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getMsIsdn();
		} else if(orderAction.equals(OEPConstants.OEP_NUMBER_RELEASE_MOBILE_SERVICE)) {
			serviceNo = request.getProcessNumberReleaseOrder().getMsIsdn();
		} else if(orderAction.equals(OEPConstants.OEP_ACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION) || orderAction.equals(OEPConstants.OEP_DEACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION)) {
			serviceNo = request.getProcessCallDivertManagment().getServiceNo();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			serviceNo = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getId();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			serviceNo = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getShortCodeNumber();
		}
			
		return serviceNo;
	}
	
	public static String getBillingAccountId(final OrderEntryRequest request,final String orderAction){
		
		String billingAccountId=null;			
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			
			billingAccountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getBillingAccountId();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)) {
			billingAccountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getBillingAccountId();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			billingAccountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getBillingAccountId();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			billingAccountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getBillingAccountId();
		}
			
		return billingAccountId;
	}
	
	
	//This method returns customer Account Id
	public static String getCustomerAccountId(final OrderEntryRequest request,final String orderAction){
			
		String customerAccountId = null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			customerAccountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getCustomerAccountId();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)) {
			customerAccountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getCustomerAccountId();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			customerAccountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getCustomerAccountId();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			customerAccountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getCustomerAccountId();
		}
			
		return customerAccountId;
	}
	
	public static SIMDetailsType getSIMDetails(final OrderEntryRequest request,final String orderAction){
		
		SIMDetailsType simDetails = null;			
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			simDetails = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getSIMDetails();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)) {
			simDetails = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getSIMDetails();
		}
			
		return simDetails;
	}
	
	public static String getServiceType(final OrderEntryRequest request,final String orderAction){
		
		String serviceType = null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
			serviceType = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getOrderServiceType();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)  || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			serviceType = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getOrderServiceType();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			serviceType = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getOrderServiceType();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			serviceType = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getOrderServiceType();
		}
		
		return serviceType;
	}
	
	/*public static String getDeviceModel(final OrderEntryRequest request,final String orderAction){
		
		String deviceModel = null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION)){
			
			deviceModel = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getHandSetDetails().getDeviceMake();
		}
		
		return deviceModel;
	}
	
	public static String getDeviceType(final OrderEntryRequest request,final String orderAction){
		
		String deviceType = null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION)){
			
			deviceType = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getHandSetDetails().getDeviceType();
		}	
		
		return deviceType;
	}
	*/
	public static BigDecimal getCreditScores(final OrderEntryRequest request,final String orderAction){
		
		String creditScore = null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) 
				|| orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
			
			creditScore = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getCreditScore();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)  || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			creditScore = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getCreditScore();
		}
		
		if(creditScore!=null && !("".equals(creditScore)))
			return new BigDecimal(creditScore);
		else 
			return null; 
	}
	
	
	public static BigDecimal getSpendingCap(final OrderEntryRequest request,final String orderAction){
		
		String spendingCap = null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)){
			
			spendingCap = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getSpendingCap();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			spendingCap = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getSpendingCap();
		}		
		
		if(spendingCap!=null && (!"".equals(spendingCap)))
			return new BigDecimal(spendingCap);
		else 
			return null; 
	}
	
	
	public static BigDecimal getSpendingCapMax(final OrderEntryRequest request,final String orderAction){
		
		String spendingCapMax = null;
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)){
			
			spendingCapMax = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getSpendingCapMax();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			spendingCapMax = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getSpendingCapMax();
		}	
		
		if(spendingCapMax!=null && !("".equals(spendingCapMax)))
			return new BigDecimal(spendingCapMax);
		else 
			return null; 
	}
	
	
	public static BigDecimal getSpendingCapMin(final OrderEntryRequest request,final String orderAction){
		
		String spendingCapMin = null;			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)){
			
			spendingCapMin = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getSpendingCapMin();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			spendingCapMin = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getSpendingCapMin();
		}			
		
		if(spendingCapMin!=null && !("".equals(spendingCapMin)))
			return new BigDecimal(spendingCapMin);
		else 
			return null; 
	}
	
	public static List<DepositPaymentType> getDepositsFromOrder(final OrderEntryRequest request,final String orderAction){
		
		List<DepositPaymentType> deposits = null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
			
			deposits = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getDepositPayment();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			deposits = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getDepositPayment();
		}
		
		return deposits;
	}
	
	public static List<HandSetDetailsType> getHandSetDetailsFromOrder(final OrderEntryRequest request,final String orderAction){
		
		List<HandSetDetailsType> handSetList = null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			
			handSetList = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getHandSetDetails();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			if(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getHandSetDetails() != null) {
				handSetList = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getHandSetDetails();
			}
		}
		
		return handSetList;
	}
	
	public static BigDecimal getOverridenPrice(final OrderEntryRequest request,final String orderAction,final String planName){
		
		BigDecimal overridenPrice  = null;	
		List<AddActionPlanType> plans = null;
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			plans = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getPlan();			
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			plans = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getPlan();
		}
		if(plans != null){
		for (AddActionPlanType plan : plans){
			
			if(plan.getPlanName().equals(planName)){
				overridenPrice = plan.getOverridenPrice();
				break;
			}
		}
		}
	
		return overridenPrice;
	}
	
	public static XMLGregorianCalendar getPlanStartDate(final OrderEntryRequest request,final String orderAction,final String planName){
		
		XMLGregorianCalendar planStartDate = null;	
		List<AddActionPlanType> plans = null;
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
			
			plans=request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getPlan();	
			
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			
			plans=request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getPlan();
			
		} else if(orderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			
			plans=request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getPlan();
			
		} else if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			
			plans = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getPlan();
		}
		
		for (AddActionPlanType plan : plans){
			
			if(plan.getPlanName().equals(planName)){
				planStartDate= plan.getPlanStartDate();
				break;
			}
		}
		return planStartDate;
	}
	
	
	public static XMLGregorianCalendar getPlanEndDate(final OrderEntryRequest request,final String orderAction,final String planName){
		
		XMLGregorianCalendar planEndDate = null;
		List<AddActionPlanType> plans = null;
		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
			
			plans=request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getPlan();			
			
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			
			plans=request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getPlan();
			
		} else if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			
			plans = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getPlan();
		
		} else if(orderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			
			plans = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getPlan();
		}
		
		for (AddActionPlanType plan : plans){
			
			if(plan.getPlanName().equals(planName)){
				planEndDate= plan.getPlanEndDate();
				break;
			}
		}
		
		return planEndDate;
	}
	
	
	public static String getAccountId(final OrderEntryRequest request,final String orderAction){
		
		String accountId=null;
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
			
			accountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAccountNo();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			accountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getAccountNo();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			accountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getAccountNo();
		} else if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			accountId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAccountNo();
		}
					
			
		return accountId;
	}
	
		
   public static String getCsrId(OrderEntryRequest request, String oepOrderAction){
		
		String csrId = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)) {
			csrId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getOrderUserId();
		} else if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			csrId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getOrderUserId();
		} else if(oepOrderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			csrId =  request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getOrderUserId();
		} else if(oepOrderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			csrId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getOrderUserId();
		}
			
		return csrId;
	}
	
	
/*	public static String getOrderActionFromRequest(OrderEntryRequest request, String oepOrderAction){
		
		String orderActionFromRequest = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			
			orderActionFromRequest = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getOrderAction();
		}
			
		return orderActionFromRequest;
	}*/
	
	public static Timestamp getOrderStartDate(OrderEntryRequest request, String oepOrderAction){
		
		Timestamp orderStartDate=null; 
		if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION)|| oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
			
			orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getOrderStartDate());
			
		} else if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getOrderStartDate());
		} else if(oepOrderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getOrderStartDate());
		} else if(oepOrderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			orderStartDate = OrderDataUtils.getTimeStampFromCalender(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getOrderStartDate());
		}
		
		return orderStartDate;
		
	}
	
	public static List<AddActionPlanType> getPlansFromOrder(final OrderEntryRequest request,final String oepOrderAction){
		
		
		if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION))
		{
			return request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getPlan();
		} else if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			return request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getPlan();
		} else if(oepOrderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			return request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getPlan();
		} else if(oepOrderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			return request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getPlan();
		}
		
		else
			return null; 
	
	}
	
	public static String getNiceCategory(final OrderEntryRequest request,final String oepOrderAction){
		
		String niceCategory = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION)  || oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION))
		{
			if( request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getNiceCategory()!=null)
				niceCategory = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getNiceCategory().toString();
		}
		
		return niceCategory;
		
	}
	
	public static String getNiceCategoryValue(final OrderEntryRequest request,final String oepOrderAction){
		
		String niceCategoryValue = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || oepOrderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION))
		{
			if( request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getNiceCategory()!=null)
				niceCategoryValue = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getNiceCategoryValue();
		}
		
		return niceCategoryValue;
		
	}
	
	public static String getPortType(final OrderEntryRequest request,final String orderAction){
		
		String portType = null;			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			
			portType = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getPortType();
			
		}
		else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)){
			
			portType = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getPortType();
		}
		else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
				|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
			
			portType = OEPConstants.OEP_MOBILE_BB;
		}
		
		return portType;
	}
	
	
	public static String getChannelName(final OrderEntryRequest request,final String orderAction ){
		 
		 String channelName = null;	 
		 
		  channelName = "OAP";	
		 
		 
		 return channelName;
		 
	}
	
	public static String getShortCodeNumber(OrderEntryRequest request, String orderAction) {
		// TODO Auto-generated method stub
		String shortCodeNumber = null;			
		if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			
			shortCodeNumber = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getShortCodeNumber();
		}
		
		return shortCodeNumber;
	}

	public static String getShortCodeType(OrderEntryRequest request, String orderAction) {
		// TODO Auto-generated method stub
		String shortCodeType = null;			
		if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			
			shortCodeType = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getShortCodeType();
		}
		
		return shortCodeType;
	}

	public static String getShortCodeCategory(OrderEntryRequest request, String orderAction) {
		// TODO Auto-generated method stub
		String shortCodeCategory = null;			
		if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			
			shortCodeCategory = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getShortCodeCategory();
		}
		
		return shortCodeCategory;
	}

	public static String getEmailId(OrderEntryRequest request, String orderAction) {
		// TODO Auto-generated method stub
		String emailId = null;			
		if(orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			
			emailId = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getEmailId();
		}
		
		return emailId;
	}
	
	public static String getNotificationLanguageFromOrder(final OrderEntryRequest request,final String orderAction){
		
		String notificationLanguage = null;		
			
		if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
			
			notificationLanguage = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getNotificationLanguage().toString();
			
		} else if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)) {
			notificationLanguage = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileBBService().getNotificationLanguage().toString();
		}
		
		
		return notificationLanguage;
	}
	
	public static String getWaiverOptions(OrderEntryRequest request, String oepOrderAction) {
		// TODO Auto-generated method stub
		
		String result = null;



		if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) && request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getOptions()!=null){

			result = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getOptions().value();
			  
		} 
		 
		return result;
	}


	public static String getWaiverReason(OrderEntryRequest request, String oepOrderAction) {
		// TODO Auto-generated method stub
		
		String waiverReason = null;
		
		if(oepOrderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) && request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getWaiverReason()!=null){
			waiverReason = request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getWaiverReason();


		} 
		return waiverReason;
	}


}
