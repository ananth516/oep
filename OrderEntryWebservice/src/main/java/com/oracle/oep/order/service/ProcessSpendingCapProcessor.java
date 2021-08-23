package com.oracle.oep.order.service;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brmorder.model.SpendingCapAction;
import com.oracle.oep.order.abm.transform.ProcessSpendingCapAbmMapping;
import com.oracle.oep.order.model.OrderEntryRequest;


public class ProcessSpendingCapProcessor {

	public static final Logger log = LogManager.getLogger(ProcessSpendingCapProcessor.class);

	ProducerTemplate template = null;

	/*
	 * 
	 * This method processes the spending cap orders from BRM
	 * 
	 */
	public void processSpendingCapOrder(final SpendingCapAction request, final Exchange exchange,
			final String brmOrderAction, final String orderPayload, final String brmJmsTimeStamp) throws Exception {

		OrderEntryRequest abmRequest = new OrderEntryRequest();
		Object response = null;

		template = exchange.getContext().createProducerTemplate();

		ProcessSpendingCapAbmMapping spendingCapAbmMapping = new ProcessSpendingCapAbmMapping();

		log.info("Calling abm mappping " + (String) template.requestBody("direct:convertxmlToString", request));
		
		abmRequest = spendingCapAbmMapping.createOrderEntryRequest(request, brmOrderAction, brmJmsTimeStamp);

		log.info("ABM Request  " + (String) template.requestBody("direct:convertxmlToString", abmRequest));

		response = template.requestBody("direct:callOEPOrderEntryWebservice", abmRequest);

		log.info("ABM Response Order sumitted" + (String) template.requestBody("direct:convertxmlToString", response));

	}

}
