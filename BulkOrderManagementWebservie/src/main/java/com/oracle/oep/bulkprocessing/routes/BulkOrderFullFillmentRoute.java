package com.oracle.oep.bulkprocessing.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderEntryRequest;
import com.oracle.oep.bulkprocessing.processors.BulkOrderFullFilmentProcessor;

public class BulkOrderFullFillmentRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		JaxbDataFormat jaxb = new JaxbDataFormat();
		 jaxb.setContextPath(BulkOrderEntryRequest.class.getPackage().getName());

		 BulkOrderFullFilmentProcessor jobProcessor = new BulkOrderFullFilmentProcessor();
		 
		 errorHandler(deadLetterChannel("jms:OEP_DEADLETTER_CHANNEL")
				 .useOriginalMessage());
		
		from("weblogicJMS:OEP_BULKORDER_JMS_QUEUE")
		.log(LoggingLevel.INFO, "${in.body}")
		.unmarshal(jaxb)
		.process(jobProcessor)
		.to("jpa:com.oracle.oep.model.BulkJobMaster");
		
		
		
	}

}
