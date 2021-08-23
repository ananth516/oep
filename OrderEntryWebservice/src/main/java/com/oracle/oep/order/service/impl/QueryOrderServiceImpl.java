package com.oracle.oep.order.service.impl;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.ListOfOEPQueryOrderResponseType;
import com.oracle.oep.order.model.OEPOrderDetailResponseType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.OrderEntryResponse;
import com.oracle.oep.order.model.ProcessQueryOrderResponseType;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.repository.OepOrderRepository;
import com.oracle.oep.order.service.QueryOrderService;
import com.oracle.oep.order.utils.OrderDataUtils;

@Service
public class QueryOrderServiceImpl implements QueryOrderService {
	
	private static final Logger log = LoggerFactory.getLogger(QueryOrderServiceImpl.class);
	
	@Autowired
	private OepOrderRepository oepOrderRepo;

	public OrderEntryResponse processQueryOrder(OrderEntryRequest request) {

		OrderEntryResponse queryOrderResponse = new OrderEntryResponse();
		
		if (request.getProcessQueryOrder().getAccountNo() != null && request.getProcessQueryOrder().getServiceNo() != null){
			queryOrdersByAccountNoandServiceNo(request,queryOrderResponse);			
		}
		
		else if (request.getProcessQueryOrder().getAccountNo() != null && request.getProcessQueryOrder().getOrderId() != null){
			queryOrdersByAccountNoandOrderId(request,queryOrderResponse);			
		}		
		
		else if (request.getProcessQueryOrder().getAccountNo() != null && request.getProcessQueryOrder().getOrderCreatedDateMin() != null){
			queryOrdersByAccountNoandDateRange(request,queryOrderResponse);			
		}	
		
		else if (request.getProcessQueryOrder().getServiceNo() != null && request.getProcessQueryOrder().getOrderCreatedDateMin() != null){
			queryOrdersByServiceNoandDateRange(request,queryOrderResponse);			
		}	
		
		else if (request.getProcessQueryOrder().getAccountNo() != null){
			queryOrdersByAccountNo(request,queryOrderResponse);			
		}
		
		else if (request.getProcessQueryOrder().getServiceNo() != null ){
			queryOrdersByServiceNo(request,queryOrderResponse);			
		}	
		
		else if(request.getProcessQueryOrder().getOrderId()!=null){			
			queryOrdersByOrderId(request,queryOrderResponse);			
		}
		
		else {			
			queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.UNABLE_TO_FETCH_RECORDS);						
		}
		
		return queryOrderResponse;
	}

	@Transactional
	private void queryOrdersByAccountNoandServiceNo(OrderEntryRequest request,OrderEntryResponse queryOrderResponse) {
		
		queryOrderResponse.setProcessQueryOrderResponse(new ProcessQueryOrderResponseType());
		
		try{
			List<OepOrder> oepOrderList = oepOrderRepo.findByAccountNoAndServiceNo(request.getProcessQueryOrder().getAccountNo(),request.getProcessQueryOrder().getServiceNo());
			if(oepOrderList.isEmpty())
				queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.NO_RECORDS_FOUND);
			else{
				 createQueryOrderResponse(oepOrderList, queryOrderResponse);
			}
		}
		catch (Exception e) {
			
			queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.UNABLE_TO_FETCH_RECORDS);
		}	
	}
	
	@Transactional
	private void queryOrdersByAccountNoandDateRange(OrderEntryRequest request,OrderEntryResponse queryOrderResponse) {
		
		queryOrderResponse.setProcessQueryOrderResponse(new ProcessQueryOrderResponseType());
		
		try{
			
			Timestamp orderCreationDateMin = OrderDataUtils.getTimeStampFromCalender(request.getProcessQueryOrder().getOrderCreatedDateMin());
			Timestamp orderCreationDateMax = OrderDataUtils.getTimeStampFromCalender(request.getProcessQueryOrder().getOrderCreatedDateMax());
			
			List<OepOrder> oepOrderList = oepOrderRepo.findByAccountNoAndDateRange(request.getProcessQueryOrder().getAccountNo(), orderCreationDateMin, orderCreationDateMax);
			
			if(oepOrderList.isEmpty())
				queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.NO_RECORDS_FOUND);
			else{
				 createQueryOrderResponse(oepOrderList, queryOrderResponse);
			}
		}
		catch (Exception e) {
			
			queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.UNABLE_TO_FETCH_RECORDS);
		}		
		
	}
	
	@Transactional
	private void queryOrdersByServiceNoandDateRange(OrderEntryRequest request,OrderEntryResponse queryOrderResponse) {
		
		queryOrderResponse.setProcessQueryOrderResponse(new ProcessQueryOrderResponseType());
		
		try{
			Timestamp orderCreationDateMin = OrderDataUtils.getTimeStampFromCalender(request.getProcessQueryOrder().getOrderCreatedDateMin());
			Timestamp orderCreationDateMax = OrderDataUtils.getTimeStampFromCalender(request.getProcessQueryOrder().getOrderCreatedDateMax());
			
			List<OepOrder> oepOrderList = oepOrderRepo.findByServiceNoAndDateRange(request.getProcessQueryOrder().getServiceNo(), orderCreationDateMin, orderCreationDateMax);
			
			if(oepOrderList.isEmpty())
				queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.NO_RECORDS_FOUND);
			else{
				 createQueryOrderResponse(oepOrderList, queryOrderResponse);
			}
		}
		catch (Exception e) {
			
			queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.UNABLE_TO_FETCH_RECORDS);
		}		
		
	}
	
	
	@Transactional
	private void queryOrdersByAccountNoandOrderId(OrderEntryRequest request,OrderEntryResponse queryOrderResponse) {
		
		queryOrderResponse.setProcessQueryOrderResponse(new ProcessQueryOrderResponseType());
		
		try{
			List<OepOrder> oepOrderList = oepOrderRepo.findByAccountNoAndOrderId(request.getProcessQueryOrder().getAccountNo(),request.getProcessQueryOrder().getOrderId());
			if(oepOrderList.isEmpty())
				queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.NO_RECORDS_FOUND);
			else{
				 createQueryOrderResponse(oepOrderList, queryOrderResponse);
			}
		}
		catch (Exception e) {
			
			queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.UNABLE_TO_FETCH_RECORDS);
		}		
		
	}
	
	@Transactional
	private void queryOrdersByOrderId(OrderEntryRequest request,OrderEntryResponse queryOrderResponse) {
		
		queryOrderResponse.setProcessQueryOrderResponse(new ProcessQueryOrderResponseType());
		
		try{
			OepOrder oepOrder = oepOrderRepo.findByOrderId(request.getProcessQueryOrder().getOrderId());
			if(oepOrder==null)
				queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.NO_RECORDS_FOUND);
			else{
				 createQueryOrderResponse(oepOrder, queryOrderResponse);
			}
		}
		catch (Exception e) {
			
			queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.UNABLE_TO_FETCH_RECORDS);
		}		
		
		
	}
	
	@Transactional
	private void queryOrdersByAccountNo(OrderEntryRequest request,OrderEntryResponse queryOrderResponse) {
		
		queryOrderResponse.setProcessQueryOrderResponse(new ProcessQueryOrderResponseType());
		
		try{
			List<OepOrder> oepOrderList = oepOrderRepo.findByAccountNo(request.getProcessQueryOrder().getAccountNo());
			if(oepOrderList.isEmpty())
				queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.NO_RECORDS_FOUND);
			else{
				 createQueryOrderResponse(oepOrderList, queryOrderResponse);
			}
		}
		catch (Exception e) {
			
			queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.UNABLE_TO_FETCH_RECORDS);
		}		
		
		
	}
	
	@Transactional
	private void queryOrdersByServiceNo(OrderEntryRequest request,OrderEntryResponse queryOrderResponse) {
		
		queryOrderResponse.setProcessQueryOrderResponse(new ProcessQueryOrderResponseType());
		
		try{
			List<OepOrder> oepOrderList = oepOrderRepo.findByServiceNo(request.getProcessQueryOrder().getServiceNo());
			if(oepOrderList.isEmpty())
				queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.NO_RECORDS_FOUND);
			else{
				 createQueryOrderResponse(oepOrderList, queryOrderResponse);
			}
		}
		catch (Exception e) {
			
			queryOrderResponse.getProcessQueryOrderResponse().setStatus(OEPConstants.UNABLE_TO_FETCH_RECORDS);
		}		
		
		
	}
		
	private void createQueryOrderResponse(List<OepOrder> oepOrderList, OrderEntryResponse queryOrderResponse){
		
		ListOfOEPQueryOrderResponseType oepOrderResponseList = new ListOfOEPQueryOrderResponseType();
		oepOrderList
		.stream()
		.sorted((oepOrder1,oepOrder2)->oepOrder2.getOrderCreationDate().compareTo(oepOrder1.getOrderCreationDate()))
		.forEach(oepOrder -> oepOrderResponseList.getOEPOrder().add(createOrderDetails(oepOrder)));	
		queryOrderResponse.getProcessQueryOrderResponse().setListOfOrders(oepOrderResponseList);
		
	}
	
	private void createQueryOrderResponse(OepOrder oepOrder, OrderEntryResponse queryOrderResponse){
		
		ListOfOEPQueryOrderResponseType oepOrderResponseList = new ListOfOEPQueryOrderResponseType();
		oepOrderResponseList.getOEPOrder().add(createOrderDetails(oepOrder));	
		queryOrderResponse.getProcessQueryOrderResponse().setListOfOrders(oepOrderResponseList);
		
	}
	
	private OEPOrderDetailResponseType createOrderDetails(OepOrder oepOrder ){
		
		OEPOrderDetailResponseType orderDetailsRespone = new OEPOrderDetailResponseType();
		
		orderDetailsRespone.setOrderId(oepOrder.getOrderId());
		orderDetailsRespone.setServiceNo(oepOrder.getServiceNo());
		orderDetailsRespone.setOrderType(oepOrder.getAction());
		orderDetailsRespone.setAccountNo(!oepOrder.getOepOrderLines().isEmpty()?oepOrder.getOepOrderLines().get(0).getAccountId():null);
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(oepOrder.getOrderCreationDate());
		
		try {
			orderDetailsRespone.setOrderCreatedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
		} catch (DatatypeConfigurationException e) {
			
			log.error("Error while Parsing the date {}",e.getMessage());
		}
		
		orderDetailsRespone.setChannelName(oepOrder.getChannelName());
		orderDetailsRespone.setOrderStatus(oepOrder.getStatusCode());
		orderDetailsRespone.setStatusDescription(oepOrder.getStatusDescription());
		
		return orderDetailsRespone;
		
	}
	

}
