package com.oracle.oep.order.service;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brmorder.model.PrepaidLifecycleBarring;
import com.oracle.oep.brmorder.model.SpendingCapAction;
import com.oracle.oep.order.abm.transform.ProcessFcaAbmMapping;
import com.oracle.oep.order.abm.transform.ProcessPrepaidLifecycleBarringAbmMapping;
import com.oracle.oep.order.abm.transform.ProcessSpendingCapAbmMapping;
import com.oracle.oep.order.model.OrderEntryRequest;


public class ProcessPrepaidLifecycleBarringProcessor {

	public static final Logger log = LogManager.getLogger(ProcessPrepaidLifecycleBarringProcessor.class);

	ProducerTemplate template = null;

	/*
	 * 
	 * This method processes the spending cap orders from BRM
	 * 
	 */
	public void processPrepaidLifecycleBarringOrder(final PrepaidLifecycleBarring request, final Exchange exchange,
			final String brmOrderAction, final String orderPayload, final String brmJmsTimeStamp) throws Exception {

		OrderEntryRequest abmRequest = new OrderEntryRequest();
		Object response = null;

		template = exchange.getContext().createProducerTemplate();

		ProcessPrepaidLifecycleBarringAbmMapping processPrepaidLifecycleBarringAbmMapping = new ProcessPrepaidLifecycleBarringAbmMapping();

		log.info("Calling abm mappping " + (String) template.requestBody("direct:convertxmlToString", request));
		
		abmRequest = processPrepaidLifecycleBarringAbmMapping.createOrderEntryRequest(request, brmOrderAction, brmJmsTimeStamp);

		log.info("ABM Request  " + (String) template.requestBody("direct:convertxmlToString", abmRequest));

		response = template.requestBody("direct:callOEPOrderEntryWebservice", abmRequest);

		log.info("ABM Response Order sumitted" + (String) template.requestBody("direct:convertxmlToString", response));

	}

}
