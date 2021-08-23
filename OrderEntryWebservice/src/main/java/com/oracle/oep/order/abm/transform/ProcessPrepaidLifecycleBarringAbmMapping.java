package com.oracle.oep.order.abm.transform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brmorder.model.PrepaidLifecycleBarring;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.BarringServicesType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ProcessCollectionBarringReqType;

public class ProcessPrepaidLifecycleBarringAbmMapping {
	public static final Logger log = LogManager.getLogger(ProcessPrepaidLifecycleBarringAbmMapping.class);

	public OrderEntryRequest createOrderEntryRequest(final PrepaidLifecycleBarring request, String brmOrderAction,
			final String brmJmsTimeStamp) {

		log.info("createOrderEntryRequest() for FCA." + request);

		OrderEntryRequest oeprequest = new OrderEntryRequest();
		
		ProcessCollectionBarringReqType prepaidLifecycleBarringReqType = new ProcessCollectionBarringReqType();
		log.info("New object" + ProcessPrepaidLifecycleBarringrAbmHelper.getAccountno(request));
		prepaidLifecycleBarringReqType.setAccountNo(ProcessPrepaidLifecycleBarringrAbmHelper.getAccountno(request));
		prepaidLifecycleBarringReqType.setBarringAction(ProcessPrepaidLifecycleBarringrAbmHelper.getBarringAction(request));
		prepaidLifecycleBarringReqType.setBarringLevel(ProcessPrepaidLifecycleBarringrAbmHelper.getBarringLevel(request));
		prepaidLifecycleBarringReqType.setEmail(ProcessPrepaidLifecycleBarringrAbmHelper.getEmailAddr(request));
		prepaidLifecycleBarringReqType.setContactNo(ProcessPrepaidLifecycleBarringrAbmHelper.getContactNo(request));
		prepaidLifecycleBarringReqType.setSuspendedIndicator(ProcessPrepaidLifecycleBarringrAbmHelper.getSuspendedIndicator(request));
		prepaidLifecycleBarringReqType.setOrderUserId(OEPConstants.BRM_USER_ID);
		
		BarringServicesType baringServiceType = new BarringServicesType();
		baringServiceType.setAction(ProcessPrepaidLifecycleBarringrAbmHelper.getActionName(request));
		baringServiceType.setBarringType(String.valueOf(ProcessPrepaidLifecycleBarringrAbmHelper.getServices(request).getBarringType()));
		baringServiceType.setPlanName(ProcessPrepaidLifecycleBarringrAbmHelper.getServices(request).getPlanName());
		baringServiceType.setServiceIdentifier(ProcessPrepaidLifecycleBarringrAbmHelper.getServices(request).getServiceIdentifier());
		baringServiceType.setServiceNo(String.valueOf(ProcessPrepaidLifecycleBarringrAbmHelper.getServices(request).getMSISDN()));
		prepaidLifecycleBarringReqType.getServices().add(baringServiceType);
		
		prepaidLifecycleBarringReqType.setChannelTrackingId(brmJmsTimeStamp);
		prepaidLifecycleBarringReqType.setChannelName(OEPConstants.BRM_CHANNEL_NAME);
		
		prepaidLifecycleBarringReqType.setChannelTrackingId(brmJmsTimeStamp);
		prepaidLifecycleBarringReqType.setChannelName(OEPConstants.BRM_CHANNEL_NAME);
		prepaidLifecycleBarringReqType.setOrderAction(OEPConstants.BRM_PREPAID_LIFECYCLE_BARRING_ACTION);
		
		oeprequest.setProcessCollectionBarring(prepaidLifecycleBarringReqType);
		
		return oeprequest;

	}

}
