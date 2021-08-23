package com.oracle.oep.order.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oracle.oep.exceptions.OepOrderProcessingException;
import com.oracle.oep.exceptions.OepOrderValidationException;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.ListOfOEPOrderResponseType;
import com.oracle.oep.order.model.OEPOrderResponseType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.OrderEntryResponse;
import com.oracle.oep.order.model.ProcessOrderResponseType;
import com.oracle.oep.order.model.utils.OepOrderDataUtil;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.repository.OepOrderRepository;
import com.oracle.oep.order.service.OrderEntryWebservice;

@Service
public class OrderEntryWebserviceImpl implements OrderEntryWebservice {

	private static final Logger log = LoggerFactory.getLogger(OrderEntryWebserviceImpl.class);
	
	@Autowired
	private ProducerTemplate template;
	
	@Autowired
	private OrderValidationService validationService;
	
	@Autowired
	private OepOrderDataUtil orderDataUtils;
	
	@Autowired
	private OepOrderRepository oepOrderRepo;
	
	@Override
	public OrderEntryResponse processOrder(Exchange exchange, OrderEntryRequest request) {
		
		String orderAction = orderDataUtils.getOrderDataByParamName(request, OEPConstants.PARAM_NAME_ORDER_ACTION);
		String serviceNo = orderDataUtils.getOrderDataByParamName(request, OEPConstants.PARAM_NAME_MSISDN);		
		String channelTrackingId = orderDataUtils.getOrderDataByParamName(request, OEPConstants.PARAM_NAME_CHANNEL_TRACKING_ID);
		
		OrderEntryResponse orderEntryResponse = createOrderEntryResponse();
		OEPOrderResponseType oepOrderResponse = new OEPOrderResponseType();
		
		try{
			
			validationService.validateOrder(request, orderAction, serviceNo);
			OepOrder oepOrder = createOepOrder(request, orderAction, serviceNo, channelTrackingId);
			
			oepOrderResponse.setOrderId(oepOrder.getOrderId());
			oepOrderResponse.setStatusCode("00");
			oepOrderResponse.setStatusDescription(OEPConstants.OEP_SUCCESS_STATUS_DESCRIPTION);
			
			sendOrderToOepSalesOrderQueue(oepOrder);
			
		}
		catch(OepOrderValidationException | OepOrderProcessingException e){
			
			log.error("Order Processing Failed : {}",e.getMessage());
			
			oepOrderResponse.setStatusCode("01");
			oepOrderResponse.setStatusDescription(e.getMessage());		
		}
		catch(Exception e){
			
			e.printStackTrace();
			throw new OepOrderProcessingException(e.getMessage());
		}
		
		setOepOrderResponseType(orderEntryResponse, oepOrderResponse);
		return orderEntryResponse;
	}	

	private OrderEntryResponse createOrderEntryResponse(){
		
		OrderEntryResponse orderEntryResponse = new OrderEntryResponse();
		ProcessOrderResponseType processOrderResponse = new ProcessOrderResponseType();
		ListOfOEPOrderResponseType lisOfOrderResponse = new ListOfOEPOrderResponseType();
		processOrderResponse.setListOfOEPOrderResponse(lisOfOrderResponse);
		orderEntryResponse.setProcessOrderResponse(processOrderResponse);
		
		return orderEntryResponse;
	}
	
	private void setOepOrderResponseType(OrderEntryResponse orderEntryResponse,OEPOrderResponseType oepOrderResponse){		
		
		orderEntryResponse.getProcessOrderResponse().getListOfOEPOrderResponse().getOEPOrderResponse().add(oepOrderResponse);
	}
	
	
	private OepOrder createOepOrder(OrderEntryRequest request,String orderAction,String serviceNo,String channelTrackingId) {
		
		
		String channelName = orderDataUtils.getOrderDataByParamName(request, OEPConstants.PARAM_NAME_CHANNEL_NAME);		
		String accountId = orderDataUtils.getOrderDataByParamName(request, OEPConstants.PARAM_NAME_ACCOUNT_ID);
		String orderId = orderDataUtils.generateOrderId(oepOrderRepo.getNextValOepOrderSeq());
		
		log.info("Genereated Order ID : {} ",orderId);
		
		OepOrder oepOrder = new OepOrder();
		oepOrder.setOrderId(orderId);
		oepOrder.setAction(orderAction);
		oepOrder.setChannelName(channelName);
		oepOrder.setCsrid(orderDataUtils.getOrderDataByParamName(request, OEPConstants.PARAM_NAME_CSRID));
		oepOrder.setAccountNo(accountId);
		oepOrder.setServiceNo(serviceNo);
		oepOrder.setPayloadXml(String.valueOf(template.requestBody("direct:convertToString", request)));
		oepOrder.setStatusCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		oepOrder.setStatusDescription(OEPConstants.OEP_ORDER_CREATED_STATUS_DESCRIPTION);
		//oepOrder.setChannelTrackingId("");
		
		log.info("Saved {} order, Order ID : {} ,Service No : {}, ChannelTrackingID : {} ",orderAction,orderId,serviceNo,channelTrackingId);
		
		return oepOrderRepo.saveAndFlush(oepOrder);
		
	}
	
	
	private void sendOrderToOepSalesOrderQueue(OepOrder oepOrder){
		
		Map<String,Object> orderHeaderMap = new HashMap<>();
		orderHeaderMap.put(OEPConstants.ORDER_ACTION_HEADER, oepOrder.getAction());
		orderHeaderMap.put(OEPConstants.OEP_ORDER_NUMBER_HEADER, oepOrder.getOrderId());
		orderHeaderMap.put(OEPConstants.JMS_CORRELATION_ID, oepOrder.getOrderId());
		try{
			
			template.sendBodyAndHeaders("direct:sendToSalesOrderQueue", oepOrder.getPayloadXml(), orderHeaderMap);

			log.info("Order sent to OEP Sales order queue : {}", oepOrder.getOrderId());
			
		}
		catch(Exception ex){
			
			ex.printStackTrace();
			throw new OepOrderProcessingException(OEPConstants.UNABLE_TO_PROCESS_ORDER);
		}
		
		
	}
	
	
}
