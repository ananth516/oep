package com.oracle.oep.order.abm.transform;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brmorder.model.SpendingCapAction;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.BarringServicesType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ProcessCollectionBarringReqType;
import com.oracle.oep.order.server.processors.ProcessBRMSalesOrderFulfillmentProcessor;

public class ProcessSpendingCapAbmMapping {
	public static final Logger log = LogManager.getLogger(ProcessSpendingCapAbmMapping.class); 	
	
public OrderEntryRequest createOrderEntryRequest(final SpendingCapAction request, String brmOrderAction, final String brmJmsTimeStamp){
	
	log.info("Spending cap ABM MAPPING" + request);
		
		OrderEntryRequest oeprequest = new OrderEntryRequest();
		
		ProcessCollectionBarringReqType barringReqType = new ProcessCollectionBarringReqType();
		log.info("New object" + ProcessSpendingCapAbmHelper.getAccountno(request));
		
		
		
		barringReqType.setAccountNo(ProcessSpendingCapAbmHelper.getAccountno(request));
		barringReqType.setBarringAction(ProcessSpendingCapAbmHelper.getBarringAction(request));
		barringReqType.setBarringLevel(ProcessSpendingCapAbmHelper.getBarringLevel(request));
		barringReqType.setContactNo(ProcessSpendingCapAbmHelper.getNotifyContactsPhone(request));
		barringReqType.setEmail(ProcessSpendingCapAbmHelper.getNotifyContactsEmailAddress(request));
		
		BarringServicesType barringServicesType = new BarringServicesType();
		barringServicesType.setPlanName(ProcessSpendingCapAbmHelper.getplanName(request));;
		barringServicesType.setBarringType(ProcessSpendingCapAbmHelper.getBarringType(request));
		barringServicesType.setServiceIdentifier(ProcessSpendingCapAbmHelper.getServiceIdentifier(request));
		barringServicesType.setServiceNo(ProcessSpendingCapAbmHelper.getMsisdn(request));
		barringServicesType.setAction(ProcessSpendingCapAbmHelper.getActionCode(request));
		
		
		barringReqType.getServices().add(barringServicesType);
		barringReqType.setChannelName(OEPConstants.BRM_CHANNEL_NAME);
		barringReqType.setChannelTrackingId(brmJmsTimeStamp);
		barringReqType.setOrderUserId(OEPConstants.BRM_USER_ID);
		
		oeprequest.setProcessCollectionBarring(barringReqType);
		
		
		
		return oeprequest;
		
	}

}
