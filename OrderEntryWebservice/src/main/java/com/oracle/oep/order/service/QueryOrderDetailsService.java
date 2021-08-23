package com.oracle.oep.order.service;

import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.OrderEntryResponse;

public interface QueryOrderDetailsService {

	public OrderEntryResponse processQueryOrderDetailsRequest(OrderEntryRequest request);
}
