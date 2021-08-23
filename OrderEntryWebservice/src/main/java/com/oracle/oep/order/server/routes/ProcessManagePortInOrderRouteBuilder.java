package com.oracle.oep.order.server.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.soap.name.TypeNameStrategy;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;
import org.w3c.dom.Document;

import com.oracle.oep.brm.opcode.root.OpcodeResponse;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.server.processors.ProcessFetchUpdateOrderProcessor;
import com.oracle.oep.order.server.processors.ProcessSalesOrderFulfillmentProcessor;

public class ProcessManagePortInOrderRouteBuilder extends RouteBuilder {
 
	
	
	@Override
	public void configure() throws Exception {

		JaxbDataFormat jaxb = new JaxbDataFormat(OrderEntryRequest.class.getPackage().getName());
		
		JaxbDataFormat opCodeResponseJaxb = new JaxbDataFormat(OpcodeResponse.class.getPackage().getName());
		
		errorHandler(deadLetterChannel("jms:OEP_DEADLETTER_CHANNEL").useOriginalMessage());
		
		SoapJaxbDataFormat soap = new SoapJaxbDataFormat("com.oracle.oep.osm.order.model", new TypeNameStrategy());
		soap.setVersion("1.2");
				
		from("weblogicJMS:OEP_CRTFO_IN_JMSQ?exchangePattern=InOnly&concurrentConsumers=10").threads(10,10).unmarshal(jaxb).process(new ProcessFetchUpdateOrderProcessor());
		

	}
}
