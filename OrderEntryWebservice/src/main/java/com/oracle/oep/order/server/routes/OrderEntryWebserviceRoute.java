package com.oracle.oep.order.server.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.builder.Namespaces;
import org.springframework.beans.factory.annotation.Autowired;

import com.oracle.oep.brm.opcode.root.OpcodeResponse;
import com.oracle.oep.brm.opcodes.CUSOPCCUPDATESERVICEOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTVALIDATECHANGEPLANOutputFlist;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.server.processors.OrderEntryWebserviceProcessor;
import com.oracle.oep.order.server.processors.OrderEntryWebserviceSchedularProcessor;
import com.oracle.oep.order.service.OrderEntryWebservice;
import com.oracle.oep.order.service.QueryOrderDetailsService;
import com.oracle.oep.order.service.QueryOrderService;
import com.oracle.oep.order.utils.TransformXqueryResponseToString;

import org.apache.camel.converter.jaxb.JaxbDataFormat;

public class OrderEntryWebserviceRoute extends RouteBuilder {

	@Autowired
	private QueryOrderService queryOrderService;
	
	@Autowired
	private QueryOrderDetailsService queryOrderDetailsService;
	
	@Autowired
	private OrderEntryWebservice orderEntryWebservice;
		
	@Override
	public void configure() throws Exception {
		
		
		JaxbDataFormat jaxb = new JaxbDataFormat(OrderEntryRequest.class.getPackage().getName());
		
		JaxbDataFormat opCodeResponseJaxb = new JaxbDataFormat(OpcodeResponse.class.getPackage().getName());
		
		errorHandler(deadLetterChannel("jms:OEP_DEADLETTER_CHANNEL").useOriginalMessage());
        
		Namespaces ns = new Namespaces();
		ns.add("oep", "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1");
		
        from("spring-ws:rootqname:{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderEntryRequest?endpointMapping=#endpointMapping")
        .routeId("OrderEntryWebserviceRoute")
        .log(LoggingLevel.INFO, "Order Entry Request : \n ${body}")
        .unmarshal(jaxb)
        .choice()
        	.when(xpath("local-name(./oep:OrderEntryRequest/*) = 'ProcessQueryOrder'",ns))
        		.bean(queryOrderService,"processQueryOrder")
        	.when(xpath("local-name(./oep:OrderEntryRequest/*) = 'ProcessQueryOrderDetails'",ns))
        		.bean(queryOrderDetailsService)
        	.otherwise()        
        		.bean(orderEntryWebservice)
        .log(LoggingLevel.INFO, "Order Entry Response : \n ${body}")
        .marshal(jaxb);
        
        from("spring-ws:rootqname:{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}RetryOSMOrderRequest?endpointMapping=#endpointMapping")
        	.unmarshal(jaxb)
        	.process("retryOSMOrderProcessor")            
        	.marshal(jaxb);
        
        from("direct:sendToSalesOrderQueue").to("weblogicJMS:OEP_SALESORDER_JMS_QUEUE?exchangePattern=InOnly");
        
        from("direct:sendToSalesOrderPriorityQueue").to("weblogicJMS:OEP_SALESORDER_JMS_QUEUE?exchangePattern=InOnly&explicitQosEnabled=true&priority=9");

		from("direct:sendToCancelResumePortInQueue").to("weblogicJMS:OEP_CRTFO_IN_JMSQ?exchangePattern=InOnly");
        
        from("direct:getOrderAction").to("xquery:xquery/getOrderAction.xquery").convertBodyTo(org.w3c.dom.Document.class).bean(TransformXqueryResponseToString.class);
        
        from("direct:getServiceIdentifier").to("xquery:xquery/getServiceIdentifier.xquery").convertBodyTo(org.w3c.dom.Document.class).bean(TransformXqueryResponseToString.class);
        
        from("direct:getServiceType").to("xquery:xquery/getServiceType.xquery").convertBodyTo(org.w3c.dom.Document.class).bean(TransformXqueryResponseToString.class);
        
        from("direct:getAccountParams").to("xquery:xquery/getAccountParams.xquery").convertBodyTo(org.w3c.dom.Document.class).bean(TransformXqueryResponseToString.class);
        
        
       from("direct:getCAAccountNumber").transform().xquery(".//n:CustomerAccountId/text()", String.class, new Namespaces("n","http://xmlns.oracle.com/OEP/OrderManagement/Order/V1"));
       
        from("direct:getAccountName").transform().xquery(".//n:AccountName/text()", String.class, new Namespaces("n","http://xmlns.oracle.com/OEP/OrderManagement/Order/V1"));
        
        from("direct:getBAAccountName").transform().xquery(".//n:BillingAccountId/text()", String.class, new Namespaces("n","http://xmlns.oracle.com/OEP/OrderManagement/Order/V1"));
        
        from("direct:queryOrderFromOEP").to("jpa:com.oracle.oep.order.presistence.model.OepOrder?query=select o from com.oracle.oep.order.presistence.model.OepOrder o where o.orderId=:param&parameters=\"param\":${in.body}");

        from("direct:queryOepOrder").to("jpa:com.oracle.oep.order.presistence.model.OepOrder?query=select o from com.oracle.oep.order.presistence.model.OepOrder o where o.orderId=:param and o.statusCode != 'COMPLETE' &parameters=\"param\":${in.body}");

        
        from("direct:queryOrderFromOEPMSISDN")
        .log("before JPA ${body}")
        .to("jpa:com.oracle.oep.order.presistence.model.OepOrder?resultClass=com.oracle.oep.order.presistence.model.OepOrder&nativeQuery=select o from com.oracle.oep.order.presistence.model.OepOrder o where o.serviceNo=:param&parameters=\"param\":${in.body}");

        
        /***
         * 
         * Camel route scheduled to execute every day to 1 am
         *  
         */
        //from("quartz://oepResumeOrderGroup/resumeOrderTimer?cron=0+0+1+?+*+*&job.name=futuderDateOrders").routeId("pollOrderRepo").process(new OrderEntryWebserviceSchedularProcessor());
        
        from("direct:sendToStartCancelOrderQueue").to("weblogicJMS:OEP_CANCELSTARTORDER_JMSQUEUE?exchangePattern=InOnly");
        
        from("direct:validateRequestChangePlanRequest")
	        .onException(Exception.class)
				.maximumRedeliveries("{{oep.ws.redeliveryLimit}}")
				.redeliveryDelay("{{oep.ws.redeliveryDelay}}")
				.end()
        	.log(LoggingLevel.INFO, "${in.body}")
        	.to("spring-ws:{{brmwebservice.url}}")
	        	.log(LoggingLevel.INFO, "Opcode Response : \n${in.body}")
	        	.unmarshal(opCodeResponseJaxb)
	        	.transform(xpath("//n:opcodeResponse/opcodeResponse/text()",CUSOPCUSTVALIDATECHANGEPLANOutputFlist.class,new Namespaces("n","http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes/")));
	        
        from("direct:getOEPOrderSequence").to("sql:classpath:sql/getOepOrderSequence.sql?dataSource=#oepDataSource");
       		
		
		
	}

}
