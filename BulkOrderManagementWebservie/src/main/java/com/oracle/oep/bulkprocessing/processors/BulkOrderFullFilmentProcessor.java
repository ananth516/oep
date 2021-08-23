package com.oracle.oep.bulkprocessing.processors;


import java.sql.Timestamp;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderEntryRequest;
import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderParamsType;
import com.oracle.oep.bulkprocessing.constants.BulkOrderConstants;
import com.oracle.oep.bulkprocessing.constants.ServiceActionEnumType;
import com.oracle.oep.bulkprocessing.model.BulkJobMaster;

public class BulkOrderFullFilmentProcessor implements Processor  {
	
	ProducerTemplate template = null;
	BulkOrderEntryRequest request  = null;

	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
			
			Message in=exchange.getIn();	
			
			request = in.getBody(BulkOrderEntryRequest.class);
			
			if(request.getProcessBulkOrderRequest() != null) {
				BulkJobMaster job = null;
				BulkOrderParamsType params = ((BulkOrderEntryRequest) request).getProcessBulkOrderRequest().getBulkOrderParams();
				
				job = new BulkJobMaster();
				job.setFileOrderId(params.getFileOrderId());
				job.setFilePath(params.getFileDirectory() + "/" + params.getFileName());
				job.setAction(params.getAction().name());
				job.setJobCreationDate(new Timestamp(new Date().getTime()));
				
				String action = ServiceActionEnumType.fromName(params.getAction().name()).toString();
				
				switch(action) {
				case BulkOrderConstants.OEP_ADD_PRE_PAID_STARTER_KIT_ACTION:
					job.setStatus(BulkOrderConstants.OEP_ORDER_INITIAL_STATUS);
					break;
				case BulkOrderConstants.OEP_ADD_PLAN_ACTION:
				case BulkOrderConstants.OEP_REMOVE_PLAN_ACTION:
				case BulkOrderConstants.OEP_SUSPEND_SERVICE_ACTION:
				case BulkOrderConstants.OEP_CHANGE_PLAN_ACTION:
				case BulkOrderConstants.OEP_PRE_PAID_PROVISIONING_ACTION:
				case BulkOrderConstants.OEP_POST_PAID_PROVISIONING_ACTION:
				case BulkOrderConstants.OEP_RECONNECT_SERVICE_ACTION:
				case BulkOrderConstants.OEP_TERMINATE_SERVICE_ACTION:
					job.setStatus(BulkOrderConstants.OEP_ORDER_INITIAL_STATUS);
					break;
					default:	
				}
				
				exchange.getOut().setBody(job);
			}
				
	}


}

