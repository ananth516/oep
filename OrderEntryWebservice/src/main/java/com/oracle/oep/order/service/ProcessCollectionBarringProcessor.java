package com.oracle.oep.order.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brmorder.model.CollectionsActionBarring;
import com.oracle.oep.brmorder.model.SpendingCapAction;
import com.oracle.oep.order.abm.transform.ProcessCollectionBarringAbmMapping;
import com.oracle.oep.order.abm.transform.ProcessSpendingCapAbmMapping;
import com.oracle.oep.order.model.OrderEntryRequest;


public class ProcessCollectionBarringProcessor {
	
	
	public static final Logger log = LogManager.getLogger(ProcessCollectionBarringProcessor.class);
	ProducerTemplate template = null;

	/*
	 * 
	 * This method processes the  Collection Barring orders from BRM
	 * 
	 */
	public void processCollectionBarringOrder(final CollectionsActionBarring request, final Exchange exchange,
			final String brmOrderAction, final String orderPayload, final String brmJmsTimeStamp) throws Exception {

		OrderEntryRequest abmRequest = new OrderEntryRequest();
		Object response = null;

		template = exchange.getContext().createProducerTemplate();

		ProcessCollectionBarringAbmMapping collectionBarringAbmMapping = new ProcessCollectionBarringAbmMapping();

		log.info("Calling abm mappping " + (String) template.requestBody("direct:convertxmlToString", request));
		
		abmRequest = collectionBarringAbmMapping.createOrderEntryRequest(request, brmOrderAction, brmJmsTimeStamp);

		log.info("ABM Request  " + (String) template.requestBody("direct:convertxmlToString", abmRequest));

		response = template.requestBody("direct:callOEPOrderEntryWebservice", abmRequest);

		log.info("ABM Response Order sumitted" + (String) template.requestBody("direct:convertxmlToString", response));

	}

}