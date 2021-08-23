package com.oracle.oep.order.server.processors;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.notif.xsd.NotifAddOnSubscription;
import com.oracle.oep.notif.xsd.NotifCancelAddOnSubscription;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.milestone.model.SalesOrderLine;
import com.oracle.oep.order.milestone.model.UpdateSalesOrderEBM;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;

public class UpdateSalesOrderFulfillmentProcessor implements Processor {

	public static final Logger log = LogManager.getLogger(UpdateSalesOrderFulfillmentProcessor.class);
	
	//ProducerTemplate template = null;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		UpdateSalesOrderEBM updateSalesOrder = exchange.getIn().getBody(UpdateSalesOrderEBM.class);
		
		String mileStonePayload = exchange.getIn().getBody(String.class);
		
		ProducerTemplate template = exchange.getContext().createProducerTemplate();
		
		String oepOrderNumber = updateSalesOrder.getDataArea().getUpdate().getUpdateSalesOrder().getIdentification().getID();
		
		log.info("OEP Order Number : "+oepOrderNumber+". Received milestone update");
		
		log.info(exchange);
		
		log.info("Mile stone payload \n"+mileStonePayload);
		
		Map<String,String> oepOrderNumberMap = new HashMap<String,String>();
		
		oepOrderNumberMap.put("param", oepOrderNumber);
		
		try{
		
			Vector<OepOrder> oepOrderResutlSet= (Vector<OepOrder>) template.requestBody("direct:getOepOrderDO", oepOrderNumberMap);
			
			
			
			Iterator<OepOrder> iterator=oepOrderResutlSet.iterator();
			
			OepOrder oepOrderDO=null;
			
			while(iterator.hasNext()){
				
				oepOrderDO = iterator.next();
			}
			
			if(oepOrderDO==null){
				
				log.info("Order with Order number : "+oepOrderNumber+" is not found.");
				
				throw new RuntimeException("Order with Order number : "+oepOrderNumber+" is not found.");
				
			}
			
			else
				
			{
				updateOrderMileStone(oepOrderNumber, oepOrderDO, updateSalesOrder, template);
				
			}		
		
		}
		
		catch(Exception e){
			
			e.printStackTrace();
			log.info("OEP Order Number : "+oepOrderNumber+". Exception caught while processing the milestone. Exception is "+e.getMessage());
			throw new RuntimeException(e.getMessage());
		}		
		
		
	}
	
	private void  updateOrderMileStone(String oepOrderNumber, OepOrder oepOrderDO, UpdateSalesOrderEBM updateSalesOrder, ProducerTemplate template){
		
		String orderMileStoneCode = updateSalesOrder.getDataArea().getUpdate().getUpdateSalesOrder().getStatus().getCode();
		String orderMileStoneDescription = updateSalesOrder.getDataArea().getUpdate().getUpdateSalesOrder().getStatus().getDescription();
		String allocatedMSISDN = updateSalesOrder.getDataArea().getUpdate().getUpdateSalesOrder().getAllocatedMsisdn();
		
		log.info("OEp Order Number : "+oepOrderNumber+". Order MileStone Code : "+orderMileStoneCode +" and Milestone description : "+orderMileStoneDescription);
		
		try{
			
			if(OEPConstants.OSM_ORDER_COMPLETE_MILESTONE_CODE.equals(orderMileStoneCode)					
				|| (
						OEPConstants.OSM_ORDER_INPROGRESS_MILESTONE_CODE.equals(orderMileStoneCode)
						&& (OEPConstants.OEP_ORDER_STATUS_OPEN.equals(oepOrderDO.getStatusCode())
								||OEPConstants.OEP_ORDER_STATUS_FAILED.equals(oepOrderDO.getStatusCode())) 
					)
				||OEPConstants.OEP_ORDER_STATUS_FAILED.equals(orderMileStoneCode)
					
					){	
				
				oepOrderDO.setStatusCode(orderMileStoneCode);
				oepOrderDO.setStatusDescription(orderMileStoneDescription);
				oepOrderDO.setLastModifiedDate((new Timestamp(new Date().getTime())));
			
			}
			List<SalesOrderLine> inputOrderLines = updateSalesOrder.getDataArea().getUpdate().getUpdateSalesOrder().getSalesOrderLine();
			
			if(inputOrderLines!=null && inputOrderLines.size()>0){
				
				updateOrderLineMileStones(oepOrderNumber, oepOrderDO, inputOrderLines, template);
			
			}
			
			if(orderMileStoneCode.equalsIgnoreCase(OEPConstants.OSM_ORDER_COMPLETE_MILESTONE_CODE)) {
				
				log.info("Complete Mielstone. OEP Order : "+oepOrderDO.getOrderId()+ ". Order Action : "+oepOrderDO.getAction());
				if(oepOrderDO.getAction().equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN) 
						|| oepOrderDO.getAction().equals(OEPConstants.OEP_MODIFY_BB_ADD_REMOVE_PLAN)
						|| oepOrderDO.getAction().equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)
						|| oepOrderDO.getAction().equals(OEPConstants.OEP_BULK_ADD_PLAN_ACTION)
						|| oepOrderDO.getAction().equals(OEPConstants.OEP_BULK_REMOVE_PLAN_ACTION)){
					
					generatePlanManagementNotification(oepOrderDO, template);
				}
				if(allocatedMSISDN != null)
					oepOrderDO.setServiceNo(allocatedMSISDN);
				
				
				oepOrderDO.setOrderEndDate((new Timestamp(new Date().getTime())));
			}
			
			//oepOrderDO.setLastModifiedDate((new Timestamp(new Date().getTime())));
			template.sendBody("direct:updateSalesOrder", oepOrderDO);			
			log.info("OEp Order Number : "+oepOrderNumber+". Order Status : "+oepOrderDO.getStatusCode()+" is set");
						
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			log.info("OEP Order Number : "+oepOrderNumber+". Exception caught while setting the order milestone. Exception is "+e.getMessage());
			throw new RuntimeException(e.getMessage());
			
		}
		
	}
	
	/**
	 * This method is used to generate notification for Add plan and Cancel plan notifications.
	 * 
	 * @param oepOrderDO
	 * 
	 */
	private void generatePlanManagementNotification(final OepOrder oepOrderDO, final ProducerTemplate template) {
		
		List<OepOrderLine> orderLines  = oepOrderDO.getOepOrderLines();
		
		for (OepOrderLine orderLine : orderLines){
			
			if(orderLine.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_ADD_ACTION_CODE)){
				
				NotifAddOnSubscription addonEvent = new NotifAddOnSubscription();
				
				addonEvent.setAccountNo(oepOrderDO.getBaAccountNo());
				addonEvent.setAccountObj(OEPConstants.BRM_ACCOUNT_OBJ);
				addonEvent.setServiceNo(Long.parseLong(oepOrderDO.getServiceNo()));
				addonEvent.setEventDescr("Addon Subscription");
				addonEvent.setChannel(oepOrderDO.getChannelName());
				addonEvent.setMessage("You have successfully subscribed: "+orderLine.getCommercialPlanName());
				
				NotifAddOnSubscription.NotifAddOnSubscriptionEvents  susbscription = new NotifAddOnSubscription.NotifAddOnSubscriptionEvents();
				susbscription.setAddOnPlanName(orderLine.getCommercialPlanName());
				
				addonEvent.setNotifAddOnSubscriptionEvents(susbscription);
				
				
				String eventPayload = (String) template.requestBody("direct:convertToString",addonEvent);
				
				log.info("OEP Order "+ oepOrderDO.getOrderId()+". Generate payload : \n"+eventPayload);
				
				Map<String,Object> headers = new HashMap<String, Object>();
				headers.put("CHANNEL_NAME","NOTIF/SMS");		
				headers.put("JMSCorrelationID", "NotifAddOnSubscription");
				headers.put("NOTIFICATION_SOURCE", "OEP");
				
				template.sendBodyAndHeaders("direct:sendEventNotificationsToNotif", eventPayload, headers);
				
				log.info("OEP Order "+ oepOrderDO.getOrderId()+". Event sent to Notif Queue");
				
			}
			 
			else if(orderLine.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE)){
				
				NotifCancelAddOnSubscription cancelAddonEvent = new NotifCancelAddOnSubscription();
				cancelAddonEvent.setAccountNo(oepOrderDO.getBaAccountNo());
				cancelAddonEvent.setAccountObj(OEPConstants.BRM_ACCOUNT_OBJ);
				cancelAddonEvent.setServiceNo(Long.parseLong(oepOrderDO.getServiceNo()));
				cancelAddonEvent.setEventDescr("Addon Subscription Cancellation");
				cancelAddonEvent.setChannel(oepOrderDO.getChannelName());
				cancelAddonEvent.setMessage("You have successfully cancelled: "+orderLine.getCommercialPlanName());
				
				NotifCancelAddOnSubscription.NotifCancelAddOnSubscriptionEvents  susbscription = new NotifCancelAddOnSubscription.NotifCancelAddOnSubscriptionEvents();
				susbscription.setPlanName(orderLine.getCommercialPlanName());
				
				cancelAddonEvent.setNotifCancelAddOnSubscriptionEvents(susbscription);
				
				
				String eventPayload = (String) template.requestBody("direct:convertToString",cancelAddonEvent);
				
				log.info("OEP Order "+ oepOrderDO.getOrderId()+". Generate payload : \n"+eventPayload);
				
				Map<String,Object> headers = new HashMap<String, Object>();
				headers.put("CHANNEL_NAME","NOTIF/SMS");		
				headers.put("JMSCorrelationID", "NotifCancelAddOnSubscription");
				headers.put("NOTIFICATION_SOURCE", "OEP");
				
				template.sendBodyAndHeaders("direct:sendEventNotificationsToNotif", eventPayload, headers);
				
				log.info("OEP Order "+ oepOrderDO.getOrderId()+". Event sent to Notif Queue");
				
			}
		}
		
		
	}

	private void updateOrderLineMileStones(String oepOrderNumber, OepOrder oepOrderDO, List<SalesOrderLine> inputOrderLines, ProducerTemplate template){
		
		List<OepOrderLine> oepOrderLines = oepOrderDO.getOepOrderLines();
		List<OepOrderLine> newOepOrderLines = new ArrayList<OepOrderLine>();
		for (SalesOrderLine orderLine : inputOrderLines){
			
			String orderLineMileStone = orderLine.getMilestoneCode();
			String orderLineStatusCode = orderLine.getStatus().getCode();
			String orderLineStatusDescription = orderLine.getStatus().getDescription();
			String inputLineId = orderLine.getIdentification().getID();
			boolean isLineFound=false;
			
			for (OepOrderLine line : oepOrderLines){				
				
				if(inputLineId.equalsIgnoreCase(line.getOrderLineId())){
					
					
					line.setMilestoneCode(orderLineMileStone);
					line.setStatusCode(orderLineStatusCode);
					line.setStatusDescription(orderLineStatusDescription);					
					line.setOepOrder(oepOrderDO);
					
					
					log.info("Found Order line with Id : "+inputLineId);
					log.info("OEp Order Number : "+oepOrderNumber+". Order line MileStone : "+orderLineMileStone+". Order Line Status Code : "+orderLineStatusCode+
							". OrderLine Status Description : "+orderLineStatusDescription+". Order row id : "+line.getOrderLineRowid());
					
					isLineFound = true;
					break;
				}		
				
			}			
			
			if(isLineFound==false){
				
				throw new RuntimeException("No Order line is found with Line Id : "+orderLine.getIdentification().getID()+" for order : "+oepOrderNumber);
			}			
			
			
		}			
		
	}	

}
