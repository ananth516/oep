package com.oracle.oep.order.server.processors;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.soap.SOAPMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import com.oracle.oep.exceptions.OEPException;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.RetryOSMOrderRequest;
import com.oracle.oep.order.model.RetryOSMOrderResponse;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.utils.OrderDataUtils;

public class RetryOSMOrderProcessor implements Processor{
	
	@Autowired
	private ProducerTemplate template;
	
	public static final Logger log = LogManager.getLogger(RetryOSMOrderProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		
		RetryOSMOrderRequest retryRequest = exchange.getIn().getBody(RetryOSMOrderRequest.class);
		RetryOSMOrderResponse response = new RetryOSMOrderResponse();
		String oepOrderNumber= retryRequest.getOrderId();
		Message out = exchange.getOut();
		String statusCode = null;
		String statusDescription = null;
		
		try {	
		
			log.info("Received retry request. OrderID :  "+oepOrderNumber);
			
			log.info("Checking for orders with id:  "+oepOrderNumber);
			
			Map<String, String> planMap = new HashMap<String, String>();
			
			planMap.put("param", oepOrderNumber);
			
			Vector<com.oracle.oep.order.presistence.model.OepOrder> result = (Vector<OepOrder>) template.requestBody("direct:queryOrderFromOEP",planMap);
			
			if(result.size()>0) {
				
				log.info("OEP Order Number : "+oepOrderNumber+". Vector size : "+result.size());
				String osmPayloadXml =  result.get(0).getOsmPayloadXml();
				
				log.info("OSM payload xml : "+osmPayloadXml);
				
				if(osmPayloadXml!=null){
				
					Document soapBodyDoc = (Document) template.requestBody("direct:convertToDocument", osmPayloadXml);				
					SOAPMessage soapMessage = OrderDataUtils.createSOAPRequest(soapBodyDoc, oepOrderNumber, template);
					
					Map<String, Object> orderHeaders = new HashMap<String,Object>();
					orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_URI, OEPConstants.OSM_WEBSERVICE_URI);
					orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_WLS_CONTENT_TYPE, OEPConstants.OSM_WEBSERVICE_CONTETNT_TYPE);		
					
					template.sendBodyAndHeaders("direct:sendSalesOrderEBMToOSM", soapMessage.getSOAPPart().getEnvelope(),orderHeaders);
					
					statusCode = "0";
					statusDescription = "Order sent to OSM";
					log.info("Order sent to OSM");
					
				}
				
				else {
					
					log.info("OSM payload not found");
					statusCode = "1";
					statusDescription = "OSM payload not found";
				}
			}
			else {
				
				log.info("No matching order found with id : "+oepOrderNumber);				
				statusCode = "2";
				statusDescription = "No matching order found with id : "+oepOrderNumber;
				
			}
			
			response.setStatusCode(statusCode);
			response.setStatusDescription(statusDescription);
			out.setBody(response);
		
		}
		
		catch(Exception e){			
			
			out.setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
			//out.setFault(true);
			exchange.setException( new OEPException(e.getLocalizedMessage()));
			out.setBody(response);
		}
		
	}

	
}
