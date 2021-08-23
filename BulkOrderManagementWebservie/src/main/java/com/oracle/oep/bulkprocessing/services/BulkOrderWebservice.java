package com.oracle.oep.bulkprocessing.services;

import org.apache.camel.Exchange;

import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderEntryRequest;
import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderEntryResponse;

public interface BulkOrderWebservice {
	
	public BulkOrderEntryResponse processOrder(Exchange exchange,BulkOrderEntryRequest bulkOrderRequest);
	
	public BulkOrderEntryResponse queryByFileOrderId(Exchange exchange,BulkOrderEntryRequest bulkOrderRequest);	
	
	public BulkOrderEntryResponse queryByBatchId(Exchange exchange,BulkOrderEntryRequest bulkOrderRequest);
	
	public BulkOrderEntryResponse queryFailedRecords(Exchange exchange,BulkOrderEntryRequest bulkOrderRequest);

}
