/**
 * 
 */
package com.oracle.oep.order.fulfilment.service;

import org.apache.camel.Exchange;

import com.oracle.oep.order.model.OrderEntryRequest;

/**
 * @author vammanam
 *
 */
public interface ProcessAddOrderFulfilmentService {
	
	
	public void processOrder(Exchange exchange,OrderEntryRequest orderEntryRequest);
}
