package com.oracle.oep.order.fulfilment.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.brm.persistence.repository.OEPConfigPlanAttributesRepository;
import com.oracle.oep.exceptions.OepOrderProcessingException;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.ebm.transform.ProcessAddOrderEbmHelper;
import com.oracle.oep.order.fulfilment.service.ProcessAddOrderFulfilmentService;
import com.oracle.oep.order.model.AddActionPlanType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.OrderPlanType;
import com.oracle.oep.order.presistence.model.BulkBatchMaster;
import com.oracle.oep.order.presistence.model.OepMilestoneHistory;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.presistence.repository.OepOrderRepository;

@Component
public class ProcessAddOrderFufilmentServiceImpl implements ProcessAddOrderFulfilmentService {
	
	private static final Logger log = LoggerFactory.getLogger(ProcessAddOrderFufilmentServiceImpl.class);
	
	@Autowired
	private ProducerTemplate template;
	
	@Autowired
	private OepOrderRepository oepOrderRepo;
	
	@Autowired
	private OEPConfigPlanAttributesRepository planAttributesRepo;

	@Override
	public void processOrder(Exchange exchange, OrderEntryRequest orderEntryRequest) {
		
		Map<String,Object> orderHeadersMap = exchange.getIn().getHeaders();
		String orderId = String.valueOf(orderHeadersMap.get(OEPConstants.OEP_ORDER_NUMBER_HEADER));		
		OepOrder oepOrder = null;
		try{
			oepOrder = getOepOrderByOrderId(orderId);
			
			createOepOrderLines(oepOrder, orderEntryRequest);	
			
			
		}
		catch(Exception e){
			
			
		}
		
	}
	
	private OepOrder getOepOrderByOrderId(String orderId){
		
		BulkBatchMaster master= new BulkBatchMaster();
		master.setBatchId(OEPConstants.OEP_BATCH_ID_DUMMY_VALUE);
		
		OepOrder oepOrder = oepOrderRepo.findByOrderId(orderId);
		
		if(oepOrder==null)
			oepOrder = new OepOrder();
		
		oepOrder.setOrderId(orderId);
	/*	oepOrder.setChannelName(ProcessModifyOrderEbmHelperFL.getChannelName(request, oepOrderAction));
		oepOrder.setServiceNo(ProcessModifyOrderEbmHelperFL.getServiceNo(request, oepOrderAction));
		oepOrder.setPayloadXml(orderPayload);
		oepOrder.setAction(oepOrderAction);
		oepOrder.setCsrid(ProcessModifyOrderEbmHelperFL.getCsrId(request, oepOrderAction));*/
		oepOrder.setBulkBatchMaster(master);	
		oepOrder.setIsPark(OEPConstants.OEP_IS_PARK_N);
		oepOrder.setIsFutureDate(OEPConstants.OEP_IS_FUTURE_DATE_N);
		oepOrder.setStatusCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		oepOrder.setStatusDescription(OEPConstants.OEP_ORDER_STATUS_Description);
		oepOrder.setOrderStartDate(new Timestamp(new Date().getTime()));
		
		return oepOrder;
		
	}
	
	private void createOepOrderLines(OepOrder oepOrder,OrderEntryRequest orderEntryRequest) {
		
		String parentLineId = UUID.randomUUID().toString();
		String orderAction = oepOrder.getAction();
		
		List<AddActionPlanType> orderPlans = ProcessAddOrderEbmHelper.getPlansFromOrder(orderEntryRequest, orderAction);		
		
		orderPlans.stream().forEach(plan -> {
			
			log.debug("OEP Order Number : {}. Querying Plan attributes for {} ",oepOrder.getOrderId() ,plan.getPlanName());
			
			OEPConfigPlanAttributesT planAttributesT = planAttributesRepo.findBySystemPlanName(plan.getPlanName());
			
			if(planAttributesT==null){
				throw new OepOrderProcessingException(plan.getPlanName()+" not fond");
			}
						
			String lineId = OEPConstants.OEP_BASE_PLAN_TYPE.equals(planAttributesT.getPackageType()) ? parentLineId : UUID.randomUUID().toString();	
			OepOrderLine orderLine = createOepOrderLine(planAttributesT,plan.getPlanName(),OEPConstants.OSM_ADD_ACTION_CODE,lineId, parentLineId);
			oepOrder.addOepOrderLine(orderLine);		
								
			
		});	
		
		if(oepOrder.getOepOrderLines().isEmpty()) {			
			log.error("OEP Order :{}. No order lines are found for the order",oepOrder.getOrderId());
			throw new OepOrderProcessingException("No order lines are found for the order");			
		}
				
		log.info("OEP Order Number : {}. Order Lines are set",oepOrder.getOrderId());
	}
	
	private OepOrderLine createOepOrderLine(OEPConfigPlanAttributesT planAttrbiutes,String planName,String planAction,String lineId,String parentLineId) {
		
		OepOrderLine orderLine = new OepOrderLine();		
		
		orderLine.setOrderLineId(lineId);
		orderLine.setParentOrderLineId(parentLineId);
		orderLine.setPlanName(planName);
		orderLine.setPlanAction(planAction);
		orderLine.setProductSpecification(planAttrbiutes.getPs());
		orderLine.setServiceType(planAttrbiutes.getServiceType());
		orderLine.setCommercialPlanName(planAttrbiutes.getCommercialPlanName());
		orderLine.setStatusCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		orderLine.setStatusDescription(OEPConstants.OEP_ORDER_STATUS_OPEN);		
		
		createMileHistoryForOrderLine(orderLine);
		
		return orderLine;
	}
	
	private void createMileHistoryForOrderLine(OepOrderLine orderLine){
		
		OepMilestoneHistory orderLineMileStoneHistroy = new OepMilestoneHistory();
		
		orderLineMileStoneHistroy.setMilestoneCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		orderLineMileStoneHistroy.setMilestoneDescription(OEPConstants.OEP_ORDER_STATUS_OPEN);
		orderLineMileStoneHistroy.setOrderLineId(orderLine.getOrderLineId());
		
		orderLine.addOepMilestoneHistory(orderLineMileStoneHistroy);
	}



}
