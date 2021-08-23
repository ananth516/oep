package com.oracle.oep.order.abm.transform;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brmorder.model.CollectionNotifyContacts;
import com.oracle.oep.brmorder.model.CollectionsActionBarring;
import com.oracle.oep.brmorder.model.Collservices;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.BarringServicesType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ProcessCollectionBarringReqType;

public class ProcessCollectionBarringAbmMapping {

	public static final Logger log = LogManager.getLogger(ProcessCollectionBarringAbmMapping.class);

	public OrderEntryRequest createOrderEntryRequest(final CollectionsActionBarring request, String brmOrderAction,
			final String brmJmsTimeStamp) {

		log.info("Collection Barring ABM MAPPING" + request);
		
		ProcessCollectionBarringReqType barringReqType = new ProcessCollectionBarringReqType();
		OrderEntryRequest oeprequest = new OrderEntryRequest();
		
		barringReqType.setAccountNo(ProcessCollectionBarringAbmHelper.getAccountno(request));
		barringReqType.setBarringAction(ProcessCollectionBarringAbmHelper.getBarringAction(request));
		barringReqType.setBarringLevel(ProcessCollectionBarringAbmHelper.getBarringLevel(request));
		barringReqType.setContactNo(ProcessCollectionBarringAbmHelper.getNotifyContactsPhone(request));
		barringReqType.setEmail(ProcessCollectionBarringAbmHelper.getNotifyContactsEmailAddress(request));
		
		
		List<Collservices> services = request.getCollservices();
		
		
		for (Collservices service : services) {
			
			BarringServicesType barringServices = new BarringServicesType();
			
			barringServices.setPlanName(service.getPlanName());
			barringServices.setServiceIdentifier(service.getServiceIdentifier());
			barringServices.setServiceNo(service.getMsisdn());
			barringServices.setBarringType(OEPConstants.BRM_COLLECTIONBARRING);
			barringServices.setAction(service.getAction());
		
			barringReqType.getServices().add(barringServices);
		
		}
		
		
		barringReqType.setChannelName(OEPConstants.BRM_CHANNEL_NAME);
		barringReqType.setChannelTrackingId(brmJmsTimeStamp);
		barringReqType.setOrderUserId(OEPConstants.BRM_USER_ID);
		
		oeprequest.setProcessCollectionBarring(barringReqType);

		return oeprequest;

	}

}
