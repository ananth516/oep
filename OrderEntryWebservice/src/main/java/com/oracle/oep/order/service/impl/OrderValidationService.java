package com.oracle.oep.order.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oracle.oep.exceptions.OepOrderProcessingException;
import com.oracle.oep.exceptions.OepOrderValidationException;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.repository.OepOrderRepository;

@Component
public class OrderValidationService {
	
	@Autowired
	private OepOrderRepository oepOrderRepo;
	
	
	private static final Logger log = LoggerFactory.getLogger(OrderValidationService.class);
	
	public void validateOrder(OrderEntryRequest request,String orderAction,String msisdn){
		checkOpenOrders(msisdn);
	}

	private void checkOpenOrders(String msisdn) {
		
		try{
			
			List<OepOrder> oepOrderList = oepOrderRepo.findByServiceNo(msisdn);
			long openOrderCount = oepOrderList
					.stream()
					.filter(oepOrder -> OEPConstants.OEP_ORDER_STATUS_OPEN.equals(oepOrder.getStatusCode())
					||OEPConstants.OEP_ORDER_STATUS_IN_PROGRESS.equals(oepOrder.getStatusCode())).count();
			
			if(openOrderCount>0)
				throw new OepOrderValidationException(OEPConstants.OEP_OPEN_ORDER_FAILURE_STATUS);
			
			
		}
		catch(Exception e){
			
			throw new OepOrderProcessingException(e.getMessage());
			
		}
		
	}

}
