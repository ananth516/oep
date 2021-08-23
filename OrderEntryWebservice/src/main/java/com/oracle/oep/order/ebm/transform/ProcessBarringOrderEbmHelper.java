package com.oracle.oep.order.ebm.transform;

import java.util.List;

import javax.swing.plaf.metal.MetalBorders.PaletteBorder;

import org.apache.logging.log4j.core.config.Order;

import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.AddActionPlanType;
import com.oracle.oep.order.model.BarringServicesType;
import com.oracle.oep.order.model.OrderEntryRequest;

public class ProcessBarringOrderEbmHelper {

	public static String getServiceNo(final BarringServicesType plan, final String oepOrderAction) {

		String serviceNo = null;

		serviceNo = plan.getServiceNo();

		return serviceNo;
	}

	public static String getChannelName(final OrderEntryRequest request, final String oepOrderAction) {

		String channelName = null;

		channelName = request.getProcessCollectionBarring().getChannelName();

		return channelName;
	}

	public static String getCsrid(final OrderEntryRequest request, final String oepOrderAction) {

		String csrId = null;

		csrId = request.getProcessCollectionBarring().getOrderUserId();

		return csrId;
	}

	public static String getChannelTrackingId(final OrderEntryRequest request, final String oepOrderAction) {

		String channelTrackingId = null;

		channelTrackingId = request.getProcessCollectionBarring().getChannelTrackingId();

		return channelTrackingId;
	}

	public static String getPlanAction(final BarringServicesType plan, final String basePlan,
			final String channelName) {

		String planAction = null;
		planAction = convertAction(plan.getAction());
		return planAction;
	}

	public static String convertAction(String action) {

		String planAction = null;
		if (action.equals("1")) {
			planAction = OEPConstants.OSM_BARRING_STEP_ACTION_CODE;
		} else if (action.equals("2")) {
			planAction = OEPConstants.OSM_BARRING_OCB_ACTION_CODE;
		} else if (action.equals("3")) {
			planAction = OEPConstants.OSM_BARRING_TOS_ACTION_CODE;
		} else if (action.equals("4")) {
			planAction = OEPConstants.OSM_UNBARRING_ACTION_CODE;
		}
		return planAction;

	}

	public static List<BarringServicesType> getPlansFromOrder(final OrderEntryRequest request,
			final String oepOrderAction) {

		if (oepOrderAction.equals(OEPConstants.BRM_ORDER_COLLECTION_ACTION_BARRING)
				|| oepOrderAction.equals(OEPConstants.BRM_ORDER_SPENDINGCAP_ACTION)
				|| oepOrderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_BARRING_ACTION)) {
			return request.getProcessCollectionBarring().getServices();
		}

		else
			return null;

	}

	public static String getAccountId(final OrderEntryRequest request, final String orderAction) {

		String accountId = null;

		if (orderAction.equals(OEPConstants.BRM_ORDER_COLLECTION_ACTION_BARRING)
				|| orderAction.equals(OEPConstants.BRM_ORDER_SPENDINGCAP_ACTION)
				|| orderAction.equals(OEPConstants.BRM_PREPAID_LIFECYCLE_BARRING_ACTION)) {

			accountId = request.getProcessCollectionBarring().getAccountNo();
		}

		return accountId;
	}

	public static String getServiceType(final BarringServicesType plan, final String channelName) {
		String serviceType = null;

		serviceType = plan.getServiceType();

		return serviceType;

	}
	public static String getServiceIdentifier(final BarringServicesType plan, final String channelName) {
		String serviceIdentfier = null;

		serviceIdentfier = plan.getServiceIdentifier();

		return serviceIdentfier;

	}
	
	
	public static String getPriorityCode(final String barringAction) {
		String priorityCode = null;

		if(barringAction != null && barringAction.equals(OEPConstants.ORDER_UNBARRINGACTION_VALUE)){
			
			priorityCode= OEPConstants.OSM_ORDER_HIGHPRIORITY_CODE_DEFAULT;
			
		}else{
			
			priorityCode= OEPConstants.OSM_ORDER_PRIORITY_CODE_DEFAULT;
			
		}

		return priorityCode;

	}

}
