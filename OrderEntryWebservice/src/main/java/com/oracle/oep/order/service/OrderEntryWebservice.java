package com.oracle.oep.order.service;

import org.apache.camel.Exchange;

import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.OrderEntryResponse;

public interface OrderEntryWebservice {

	public OrderEntryResponse processOrder(Exchange exchange,OrderEntryRequest request);
}
