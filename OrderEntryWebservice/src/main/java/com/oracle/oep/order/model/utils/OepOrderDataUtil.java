package com.oracle.oep.order.model.utils;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.AddOrderMobileType;
import com.oracle.oep.order.model.ChangeAddOnOrderType;
import com.oracle.oep.order.model.ModifyOrderMobileServiceType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ProcessAddOnManagmentType;
import com.oracle.oep.order.model.ProcessAddOrderType;
import com.oracle.oep.order.model.ProcessModifyOrderType;
import com.oracle.oep.order.model.SuspendOrderMobileServiceType;

@Component
public class OepOrderDataUtil {

	public String getOrderDataByParamName(OrderEntryRequest request,String paramName) {

		if (request.getProcessAddOrder() != null) {
			return getOrderDataParamForAddOrder(request.getProcessAddOrder(),paramName);
		}

		else if (request.getProcessModifyOrder() != null) {
			return getOrderDataParamForModifyOrder(request.getProcessModifyOrder(),paramName);
		}

		else if (request.getProcessSuspendOrResumeOrder() != null) {
			return getProcessSuspendOrResumeOrder(request);
		}

		return null;

	}
	
	private String getOrderDataParamForAddOrder(ProcessAddOrderType processAddOrderType,String paramName){
		
		if(processAddOrderType.getListOfOEPOrder().getOEPOrder().getMobileService() != null)
			return getOrderDataParamForAddMobileService(processAddOrderType.getListOfOEPOrder().getOEPOrder().getMobileService(),paramName);
		
		return null;
	}
	
	private String getOrderDataParamForAddMobileService(AddOrderMobileType addOrderMobileType,String paramName){
		
		if(OEPConstants.PARAM_NAME_ORDER_ACTION.equals(paramName))
			return  addOrderMobileType.getOrderAction();
		else if(OEPConstants.PARAM_NAME_MSISDN.equals(paramName))
			return addOrderMobileType.getMsIsdn();
		else if(OEPConstants.PARAM_NAME_CSRID.equals(paramName))
			return addOrderMobileType.getOrderUserId();
		else if(OEPConstants.PARAM_NAME_CHANNEL_TRACKING_ID.equals(paramName))
			return addOrderMobileType.getChannelTrackingId();
		return null;
	}
	
	private String getOrderDataParamForModifyOrder(ProcessModifyOrderType processModifyOrder,String paramName) {

		
		if (processModifyOrder.getListOfOEPOrder().getOEPOrder().getMobileService() != null)
			return getOrderDataParamForModifyMobileService(processModifyOrder.getListOfOEPOrder().getOEPOrder().getMobileService(),paramName);

		return null;
	}

	private String getOrderDataParamForModifyMobileService(ModifyOrderMobileServiceType modifyOrderMobileServiceType,String paramName) {

		if (modifyOrderMobileServiceType.getAddRemoveAddon() != null)
			return getOrderDataParamForAddRemoveAddon(modifyOrderMobileServiceType.getAddRemoveAddon(),paramName);
		else if (modifyOrderMobileServiceType.getChangePlan() != null)
			return modifyOrderMobileServiceType.getChangePlan().getOrderAction();
		else if (modifyOrderMobileServiceType.getChangeMsIsdn() != null)
			return modifyOrderMobileServiceType.getChangeMsIsdn().getOrderAction();
		else if (modifyOrderMobileServiceType.getChangeSim() != null)
			return modifyOrderMobileServiceType.getChangeSim().getOrderAction();
		else if (modifyOrderMobileServiceType.getFca() != null)
			return modifyOrderMobileServiceType.getFca().getOrderAction();

		return null;
	}
	
	private String getOrderDataParamForAddRemoveAddon(ChangeAddOnOrderType addonType,String paramName){
		if(OEPConstants.PARAM_NAME_ORDER_ACTION.equals(paramName))
			return  addonType.getOrderAction();
		return null;
	}

	private String getProcessSuspendOrResumeOrder(OrderEntryRequest request) {

		if (request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().get(0)
				.getMobileService() != null)
			return getOrderActionForSuspendMobileService(request.getProcessSuspendOrResumeOrder().getListOfOEPOrder()
					.getOEPOrder().get(0).getMobileService());
		return null;
	}

	private String getOrderActionForSuspendMobileService(SuspendOrderMobileServiceType suspendOrderMobileServiceType) {

		return suspendOrderMobileServiceType.getServiceDetails().getOrderAction();

	}

	public String generateOrderId(BigDecimal nextValOepOrderSeq) {
		
		return OEPConstants.OEP_ORDER_PREFIX+nextValOepOrderSeq.toString();
	}

}
