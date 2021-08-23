package com.oracle.oep.order.server.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.order.milestone.model.UpdateSalesOrderEBM;
import com.oracle.oep.order.server.processors.ProcessSalesOrderFulfillmentProcessor;
import com.oracle.oep.order.server.processors.UpdateSalesOrderFulfillmentProcessor;

public class UpdateSalesOrderFulfillmentRoute extends RouteBuilder {

	public static final Logger log = LogManager.getLogger(UpdateSalesOrderFulfillmentRoute.class);
 	
	@Override
	public void configure() throws Exception {
		
		JaxbDataFormat jaxb = new JaxbDataFormat(UpdateSalesOrderEBM.class.getPackage().getName());
		
		errorHandler(deadLetterChannel("jms:OEP_DEADLETTER_CHANNEL").useOriginalMessage());
		
		from("weblogicJMS:OEP_UPDSO_INT_JMSQ?concurrentConsumers=20&acknowledgementModeName=CLIENT_ACKNOWLEDGE").threads(10,10).unmarshal(jaxb).process(new UpdateSalesOrderFulfillmentProcessor());
		
		from("direct:getOepOrderDO").to("jpa:com.oracle.oep.order.presistence.model.OepOrder?query=select o from com.oracle.oep.order.presistence.model.OepOrder o where o.orderId=:param&parameters=\"param\":${in.body}");
		
		from("direct:updateSalesOrder").to("jpa:com.oracle.oep.order.presistence.model.OepOrder");
		
		log.info("Registered UpdateSalesOrderFulfillment routes");

	}

}
