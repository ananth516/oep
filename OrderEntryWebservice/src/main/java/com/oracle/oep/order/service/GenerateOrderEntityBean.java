package com.oracle.oep.order.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.AddActionPlanType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.utils.OrderDataUtils;

public class GenerateOrderEntityBean {
	
	public static final Logger log = LogManager.getLogger(GenerateOrderEntityBean.class);
	
	/*
	 * 
	 *This method sets all the required parameters for given OepOrder entity
	 * 
	 */
	
	public void getOepOrderDO(final OepOrder oepOrderDO, final String serviceNo,final String csrId,final Timestamp orderStartDate,
					final String oepOrderAction,final String oepOrderNumber,final String orderPayload, final String orderPriority, final String channelName){

						
		oepOrderDO.setOrderId(oepOrderNumber);
		oepOrderDO.setAction(oepOrderAction);
		oepOrderDO.setCsrid(csrId);
		oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		oepOrderDO.setStatusDescription(OEPConstants.OEP_ORDER_STATUS_Description);
		oepOrderDO.setPayloadXml(orderPayload);
		oepOrderDO.setOrderStartDate(orderStartDate);	
		oepOrderDO.setServiceNo(serviceNo);
		oepOrderDO.setChannelName(channelName);
		
		log.info("OEP Order NUmber : "+oepOrderNumber+". Initialized OepOrderDO.");
		
		
		}
	
	/*
	 * 
	 *This method sets all the required parameters for given OepOrderLine entity
	 * 
	 */
		public OepOrderLine getOepOrderLineDO(final String oepOrderAction,
			final String planName, final String productSpec, final String planAction,final String accountId, 
			final String serviceType, final String parentLineId,String oepOrderNumber){
		
			String orderLineId = UUID.randomUUID().toString();			
			OepOrderLine orderLine = new OepOrderLine();
			
			orderLine.setOrderLineId(orderLineId);
			if(parentLineId!=null){
			orderLine.setParentOrderLineId(parentLineId);
			}
			else {
			
			orderLine.setParentOrderLineId(orderLineId);
			}
			orderLine.setPlanName(planName);
			orderLine.setPlanAction(planAction);
			orderLine.setStatusCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
			orderLine.setServiceType(serviceType);
			orderLine.setAccountId(accountId);
			orderLine.setProductSpecification(productSpec);
			
			log.info("OEP Order Number : "+oepOrderNumber+"-"+planName+" OLI is set to OepOrderLine Bean");
			
			return orderLine;
		}
		
		/**
		 * 
		 * @param oepOrderNumber
		 * @param template
		 * @return
		 * 
		 */
		public OepOrder queryOepOrderDO(final String oepOrderNumber,ProducerTemplate template){
			
			log.info("OEP Order Number : "+oepOrderNumber+". Querying Order from DB");
			
			Map<String, String> planMap = new HashMap<String, String>();
			
			planMap.put("param", oepOrderNumber);
			
			Vector<com.oracle.oep.order.presistence.model.OepOrder> result = (Vector<OepOrder>) template.requestBody("direct:queryOrderFromOEP",planMap);
			
			if(result.size()>0) {
				
				log.info("OEP Order Number : "+oepOrderNumber+". Vector size : "+result.size());
				return result.get(0);
			}
			else {
				
				return new OepOrder();
				
			}
			
			
		}
}
