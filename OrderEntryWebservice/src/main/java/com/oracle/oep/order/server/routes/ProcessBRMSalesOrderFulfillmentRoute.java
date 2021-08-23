package com.oracle.oep.order.server.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;

import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.OrderEntryResponse;
import com.oracle.oep.order.server.processors.ProcessBRMSalesOrderFulfillmentProcessor;
import com.oracle.oep.order.server.processors.ProcessSalesOrderFulfillmentProcessor;

import org.apache.camel.dataformat.soap.name.TypeNameStrategy;

public class ProcessBRMSalesOrderFulfillmentRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		
		
		SoapJaxbDataFormat soap = new SoapJaxbDataFormat("com.oracle.oep.osm.order.model", new TypeNameStrategy());
		soap.setVersion("1.2");
				
		from("weblogicJMS:BRM_SALESORDER_JMS_QUEUE?exchangePattern=InOnly&concurrentConsumers=40").threads(20,20).process(new ProcessBRMSalesOrderFulfillmentProcessor());
		
		from("direct:convertxmlToString?exchangePattern=InOut").convertBodyTo(String.class);
		
		
		//from("direct:sendOrderEntryRequestToOEP?exchangePattern=InOnly").log(LoggingLevel.INFO, "BRM order payload : \n ${in.body}").to("weblogicJMS:"+OEPConstants.OEP_SALESORDER_JMS_QUEUE_JNDI+"?exchangePattern=InOnly");
	
		//from("direct:callOEPOrderEntryWebservice").to("spring-ws:{{oepWebService.url}}").transform(xpath("//n:OrderEntryResponse/n:ProcessOrderResponse/n:ListOfOEPOrderResponse/n:OEPOrderResponse/n:OrderId/text()", OrderEntryResponse.class).namespace("n", "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1"));
		
		from("direct:callOEPOrderEntryWebservice")
			.log(LoggingLevel.INFO, "Payload : \n ${in.body}")
			.to("spring-ws:{{oepWebService.url}}")
				.convertBodyTo(OrderEntryResponse.class);
	}

}
