package com.oracle.oep.troubleticket.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.soap.SoapJaxbDataFormat;
import org.apache.camel.dataformat.soap.name.TypeNameStrategy;

import com.oracle.oep.troubleticket.processor.CreateTroubleTicketFulfilmentProcessor;
import com.oracle.oep.troubleticket.xsd.CreateTroubleTicketResponse;

public class CreateTroubleTicketFulfilmentRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		from("weblogicJMS:OEP_CRTTTREQ_JMSQ?exchangePattern=InOnly&concurrentConsumers=20")
		.threads(10,10)		
		.process("createTTProcessor");
		
		SoapJaxbDataFormat soap = new SoapJaxbDataFormat("com.oracle.oep.troubleticket.xsd", new TypeNameStrategy());
		JaxbDataFormat jaxb = new JaxbDataFormat(CreateTroubleTicketResponse.class.getPackage().getName());
		
		from("direct:callTroubleTicketService")
			.onException(Exception.class)
				.maximumRedeliveries("{{oep.ws.redeliveryLimit}}")
				.redeliveryDelay("{{oep.ws.redeliveryDelay}}")
				.end()
			.log(LoggingLevel.INFO, log, "TT WS Request payload : \n ${in.body}")
			.to("spring-ws:{{ttService.url}}")
			.log(LoggingLevel.INFO, log, "TT WS Response payload : \n ${in.body}")
			.unmarshal(jaxb);
		
		
		from("direct:sendFailureEventToNotif").to("weblogicJMS:OEP_EVENTS_NOTIFICATTION_QUEUE?exchangePattern=InOnly");
		
		
	}

}
