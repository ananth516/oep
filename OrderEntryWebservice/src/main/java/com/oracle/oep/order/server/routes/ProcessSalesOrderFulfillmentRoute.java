package com.oracle.oep.order.server.routes;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.camel.dataformat.soap.name.TypeNameStrategy;
import org.w3c.dom.Document;

import com.oracle.oep.brm.opcode.root.OpcodeResponse;
import com.oracle.oep.brm.opcodes.CUSOPCCUPDATESERVICEOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTADDPLANInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTADDPLANOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTCANCELPLANOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTINCOMPATIBLEADDONOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTUPDATESERVICEOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.server.processors.ProcessSalesOrderFulfillmentProcessor;
import com.oracle.oep.order.server.processors.ProcessStartCancelOrderFulfillmentProcessor;
import com.sun.mail.imap.protocol.Namespaces;

public class ProcessSalesOrderFulfillmentRoute extends RouteBuilder {
	

	@Override
	public void configure() throws Exception {

		JaxbDataFormat jaxb = new JaxbDataFormat(OrderEntryRequest.class.getPackage().getName());
		
		JaxbDataFormat opCodeResponseJaxb = new JaxbDataFormat(OpcodeResponse.class.getPackage().getName());
		
		SoapJaxbDataFormat soap = new SoapJaxbDataFormat("com.oracle.oep.osm.order.model", new TypeNameStrategy());
		soap.setVersion("1.2");
		
		errorHandler(deadLetterChannel("jms:OEP_DEADLETTER_CHANNEL").useOriginalMessage());
				
		from("weblogicJMS:OEP_SALESORDER_JMS_QUEUE?exchangePattern=InOnly&concurrentConsumers=40")
			.threads(20, 20)
			.unmarshal(jaxb)
			.process(new ProcessSalesOrderFulfillmentProcessor());
		
		from("direct:queryPlanFromBRM")
			.onException(Exception.class)
				.log("Exception ####################### \n ${exception.stacktrace}")
				.handled(true)
				.throwException(new Exception("${exception.message}"))
			.end()
			.to("brmJpa:com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT?query=select o from com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT o where o.systemPlanName=:param&parameters=\"param\":${in.body}");
		
		from("direct:saveOrderToRespository")
			.process(new Processor() {
				
				@Override
				public void process(Exchange exchange) throws Exception {
					Message in = exchange.getIn();
					OepOrder oepOrderDO = in.getBody(OepOrder.class);
					oepOrderDO.setLastModifiedDate(new Timestamp(new Date().getTime()));					
					in.setBody(oepOrderDO);
				}
			})
			.to("jpa:com.oracle.oep.order.presistence.model.OepOrder");		
		
		from("direct:convertToDocument?exchangePattern=InOut").convertBodyTo(Document.class);	
		
		from("direct:convertToOrderEntryRequest?exchangePattern=InOut").convertBodyTo(OrderEntryRequest.class);	
		
		from("direct:convertToString?exchangePattern=InOut").convertBodyTo(String.class);
		
		from("direct:sendSalesOrderEBMToOSM?exchangePattern=InOnly")
			.log(LoggingLevel.INFO,"OSM order payload : \n ${in.body}")
				.to("weblogicJMS:"+OEPConstants.OEP_OSM_WS_REQUEST_QUEUE_JNDI+"?exchangePattern=InOnly");
		
		from("direct:sendSalesOrderEBMToOSMWithPriority?exchangePattern=InOnly")
		.log(LoggingLevel.INFO,"OSM order payload : \n ${in.body}")
			.to("weblogicJMS:"+OEPConstants.OEP_OSM_WS_REQUEST_QUEUE_JNDI+"?exchangePattern=InOnly&explicitQosEnabled=true&priority=9");
		
		
		org.apache.camel.support.builder.Namespaces namespaces = new org.apache.camel.support.builder.Namespaces();
		namespaces.add("n", "http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes/");
		
		from("direct:getAccountAndPlanDetailsFromBRM").log(LoggingLevel.INFO, log, "${in.body}")
			.onException(Exception.class)
				.maximumRedeliveries("{{oep.ws.redeliveryLimit}}")
				.redeliveryDelay("{{oep.ws.redeliveryDelay}}")
				.end()
			.to("spring-ws:{{brmwebservice.url}}")	
				.log(LoggingLevel.INFO, "Opcode Response: \n${in.body}")			
				.unmarshal(opCodeResponseJaxb)
				.transform(xpath("//n:opcodeResponse/opcodeResponse/text()",CUSOPSEARCHOutputFlist.class,namespaces));
		
		from("direct:getIncompatibleAddonFromBRM")
			.onException(Exception.class)
				.maximumRedeliveries("{{oep.ws.redeliveryLimit}}")
				.redeliveryDelay("{{oep.ws.redeliveryDelay}}")
				.end()
			.log(LoggingLevel.INFO, "${in.body}")
			.to("spring-ws:{{brmwebservice.url}}")
				.log(LoggingLevel.INFO, "Opcode Response : \n${in.body}")
				.unmarshal(opCodeResponseJaxb)
				.transform(xpath("//n:opcodeResponse/opcodeResponse/text()",CUSOPCUSTINCOMPATIBLEADDONOutputFlist.class,namespaces));
					//.namespace("n", "http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes/"));
		
		from("direct:sendUpdateServiceRequestToBRM")
			.onException(Exception.class)
				.maximumRedeliveries("{{oep.ws.redeliveryLimit}}")
				.redeliveryDelay("{{oep.ws.redeliveryDelay}}")
				.end()		
			.log(LoggingLevel.INFO, "${in.body}")
			.to("spring-ws:{{brmwebservice.url}}")
				.log(LoggingLevel.INFO, "Opcode Response : \n${in.body}")
				.unmarshal(opCodeResponseJaxb)
				.transform(xpath("//n:opcodeResponse/opcodeResponse/text()",CUSOPCCUPDATESERVICEOutputFlist.class,namespaces));
					
			
		from("direct:sendAddPlanRequestToBRM")
			.onException(Exception.class)
				.maximumRedeliveries("{{oep.ws.redeliveryLimit}}")
				.redeliveryDelay("{{oep.ws.redeliveryDelay}}")
				.end()
			.log(LoggingLevel.INFO, "${in.body}")
			.to("spring-ws:{{brmwebservice.url}}")
				.log(LoggingLevel.INFO, "Opcode Response : \n${in.body}")
				.unmarshal(opCodeResponseJaxb)
				.transform(xpath("//n:opcodeResponse/opcodeResponse/text()",CUSOPCUSTADDPLANOutputFlist.class,namespaces));
						//.namespace("n", "http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes/"));
			
		from("direct:sendCancelPlanRequestToBRM")
			.onException(Exception.class)
				.maximumRedeliveries("{{oep.ws.redeliveryLimit}}")
				.redeliveryDelay("{{oep.ws.redeliveryDelay}}")
				.end()
			.log(LoggingLevel.INFO, "${in.body}")
			.to("spring-ws:{{brmwebservice.url}}")
				.log(LoggingLevel.INFO, "Opcode Response : \n${in.body}")
				.unmarshal(opCodeResponseJaxb)
		        .transform(xpath("//n:opcodeResponse/opcodeResponse/text()",CUSOPCUSTCANCELPLANOutputFlist.class,namespaces));
		        		//.namespace("n", "http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes/"));
		
		from("direct:sendUpdateHandsetRequestToBRM")
			.onException(Exception.class)
				.maximumRedeliveries("{{oep.ws.redeliveryLimit}}")
				.redeliveryDelay("{{oep.ws.redeliveryDelay}}")
				.end()
			.log(LoggingLevel.INFO, "${in.body}")
			.to("spring-ws:{{brmwebservice.url}}")
				.log(LoggingLevel.INFO, "Opcode Response : \n${in.body}")
					.unmarshal(opCodeResponseJaxb)
					.transform(xpath("//n:opcodeResponse/opcodeResponse/text()",CUSOPCUSTUPDATESERVICEOutputFlist.class,namespaces));
							//.namespace("n", "http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes/"));
		
		
		from("weblogicJMS:OEP_CANCELSTARTORDER_JMSQUEUE?exchangePattern=InOnly&concurrentConsumers=40")
		.threads(5, 10)
		.unmarshal(jaxb).process(new ProcessStartCancelOrderFulfillmentProcessor());
		
		from("direct:getProperty").setBody(simple("${properties:${body}}"));
		
		from("direct:queryCosFromBRM").to("sql:classpath:sql/queryPCosAndSCosFromBRM.sql?dataSource=#brmDataSource");
		
		from("direct:sendEventNotificationsToNotif").to("weblogicJMS:OEP_EVENTS_NOTIFICATTION_QUEUE?exchangePattern=InOnly");
		
	}

}
