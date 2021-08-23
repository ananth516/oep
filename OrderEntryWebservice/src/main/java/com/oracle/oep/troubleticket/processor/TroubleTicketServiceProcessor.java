package com.oracle.oep.troubleticket.processor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.exceptions.TroubleTicketServiceException;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.troubleticket.model.OapCallLogMasterT;
import com.oracle.oep.troubleticket.model.OapTicketUpdateHistoryT;
import com.oracle.oep.troubleticket.model.OapWorkOrderMasterT;
import com.oracle.oep.troubleticket.xsd.CreateTroubleTicketRequest;
import com.oracle.oep.troubleticket.xsd.CreateTroubleTicketResponse;

public class TroubleTicketServiceProcessor implements Processor {

	public static final Logger log = LogManager.getLogger(TroubleTicketServiceProcessor.class);
	
		
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		ProducerTemplate template= null;
		Message in= null;
		Message out= null;
		CreateTroubleTicketRequest ttRequest= null;	
		CreateTroubleTicketResponse ttResponse= null;
		
		List<Map<String,Object>> dispositionParamsList= null;
		
		template = exchange.getContext().createProducerTemplate();
		in = exchange.getIn();
		out = exchange.getOut();
		
		
		
		ttRequest = in.getBody(CreateTroubleTicketRequest.class);
		
		String ttRequestStr = in.getBody(String.class);
		
		log.info("Received Trouble Ticket Request : \n"+ttRequestStr);
		
		String orderId = ttRequest.getOrderId();
		String callArea = ttRequest.getCallArea();
		String callCaterogry = ttRequest.getCallCategory();
		String callSubCategory = ttRequest.getCallSubCategory();
		String callType = ttRequest.getCallType();
		
		log.info("Received trouble ticket request for OrderId: "+orderId);
		
		dispositionParamsList = queryDispositionParams(ttRequest, template);
		
		ttResponse = new CreateTroubleTicketResponse();
		
		if(dispositionParamsList == null ||!(dispositionParamsList.size()>0)){
			
			log.info("OrderID : "+ orderId+". Validation failed setting fault response.");
			
			ttResponse.setResponseCode("1");
			ttResponse.setResponseDesscription("Invalid disposition parameters");
			
		}
		
		else {			
			
			if(ttRequest.getAction().equalsIgnoreCase("Add"))
			   addTTRequest(dispositionParamsList, ttRequest, template, ttResponse);
		}
		
		log.info("OrderID : "+ orderId+". TTResponse is set");
		;
		out.setBody(ttResponse);
		
		/*
		if(ttResponse.getResponseCode().equals("1")){
			
			out.setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
			out.setFault(true);
			exchange.setException( new TroubleTicketServiceException(ttResponse.getResponseDesscription()));
		}
		*/
		//throw new Exception("Retried : "+Exchange.REDELIVERY_COUNTER+ " Testing Retry : "+ orderId);

		
	}
	
	/**
	 * 
	 * @param ttRequest 
	 * @param template 
	 * @return
	 */
	private List<Map<String, Object>> queryDispositionParams(CreateTroubleTicketRequest ttRequest, ProducerTemplate template){
		
		String orderId = ttRequest.getOrderId();
		String callArea = ttRequest.getCallArea();
		String callCaterogry = ttRequest.getCallCategory();
		String callSubCategory = ttRequest.getCallSubCategory();
		String callType = ttRequest.getCallType(); 
		
		log.debug("Below validate query: "+ttRequest.getOrderId());
		 
		 Map<String,Object> params = new HashMap<String,Object>();
		 params.put("serviceGroup", "FALLOUT");
		 params.put("callSubCategory", callSubCategory);
		 params.put("callType", callType);
		 params.put("callArea", callArea);
		 params.put("callCategory", callCaterogry);
		
		 List<Map<String, Object>>  resultSelt = (List<Map<String, Object>>) template.requestBody("direct:queryDispositionParams", params);
		 
		 log.info("OrderId : "+orderId+". ResultSet size "+resultSelt.size()); 
		
		
		return resultSelt;
	}
	
	
	private void addTTRequest(List<Map<String,Object>> dispositionParamsList, CreateTroubleTicketRequest ttRequest, ProducerTemplate template, CreateTroubleTicketResponse ttResponse){
		
		Map<String,Object> dispositionParams = dispositionParamsList.get(0);
		
		String team_id = dispositionParams.get("TEAM_ID").toString();
		String callTypeId = dispositionParams.get("CALL_TYPE_ID").toString();
		String callAreadId = dispositionParams.get("CALL_AREA_ID").toString();
		String callSubCatId = dispositionParams.get("CALL_SUB_CATEGORY_ID").toString();
		String callCatId = dispositionParams.get("CALL_CATEGORY_ID").toString();
		String callResolutionId = dispositionParams.get("CALL_RESOLUTION_ID").toString();
		String callActionID = dispositionParams.get("CALL_ACTION_ID").toString();
		String queueId = dispositionParams.get("QUEUE_ID").toString();
		String orderId = ttRequest.getOrderId();
		String serviceNumber = ttRequest.getServiceNo();
		String logPrefix = "OrderId : "+orderId;
		String userid = template.requestBody("direct:getProperty","oap.tt.userid").toString();
		if(queueId==null){
			
			log.info(logPrefix+". Queue Id not found");
			
			ttResponse.setResponseCode("1");
			ttResponse.setResponseDesscription("Queue Id not found");
			
		}
		
		else {
			
			try{
				
				OapCallLogMasterT logMaster = new OapCallLogMasterT();
				logMaster.setClassifiedAs("WO");
				logMaster.setServiceGroup(OEPConstants.OEP_TT_SERVICE_GROUP);
				logMaster.setCustAccNo(ttRequest.getCustomerAccount());
				logMaster.setBillingAccNo(ttRequest.getBillingAccount());
				logMaster.setServAccNo(ttRequest.getServiceAccount());
				logMaster.setTypeId(new BigDecimal(callTypeId));
				logMaster.setAreaId(new BigDecimal(callAreadId));
				logMaster.setCategoryId(new BigDecimal(callCatId));
				logMaster.setSubCatId(new BigDecimal(callSubCatId));
				logMaster.setResolutionId(new BigDecimal(callResolutionId));
				logMaster.setActionId(new BigDecimal(callActionID));
				logMaster.setPriority(ttRequest.getPriority());
				logMaster.setCsrLogRemarks(ttRequest.getRemarks());
				logMaster.setCallbackRequired(new BigDecimal(ttRequest.getCallBack()));
				if(ttRequest.getCallBack().equals("0")){
					
					logMaster.setPreferredTime(OrderDataUtils.getTimeStampFromCalender(ttRequest.getIssueDateTime()));
				}
				logMaster.setUserId(userid);
				logMaster.setServiceType(ttRequest.getServiceType());
				logMaster.setServiceNo(ttRequest.getServiceNo());
				logMaster.setOrderedQuantity(new BigDecimal(0));
				logMaster.setServiceOrderId(ttRequest.getOrderId());
				logMaster.setOrderFailureSystem(ttRequest.getCallCategory());
				logMaster.setOrderFailureArea(ttRequest.getCallSubCategory());
				logMaster.setOrderFailureCode(ttRequest.getFailureCode());
				logMaster.setOrderFailureDesc(ttRequest.getFailureDescription());
				logMaster.setCallS(new BigDecimal(0));
				logMaster.setCreated(new Timestamp((new Date()).getTime()));
				logMaster.setModified(new Timestamp(new Date().getTime()));
				
				List<OapWorkOrderMasterT> workOrderList = new ArrayList<OapWorkOrderMasterT>();
				
				
				OapWorkOrderMasterT workOrder = new OapWorkOrderMasterT();
				
				workOrder.setTeamId(new BigDecimal(team_id));
				workOrder.setQueueId(new BigDecimal(queueId));
				workOrder.setAckSlaViolated(new BigDecimal(0));
				workOrder.setResSlaViolated(new BigDecimal(0));
				workOrder.setCreatedOn(new Timestamp(new Date().getTime()));
				workOrder.setModifiedLast(new Timestamp(new Date().getTime()));
				workOrder.setStatus("UNASSIGNED");
				workOrder.setOapCallLogMasterT(logMaster);
				
				workOrderList.add(workOrder);
				
				logMaster.setOapWorkOrderMasterTs(workOrderList);	
				
				
				OapTicketUpdateHistoryT ticketHistory = new OapTicketUpdateHistoryT();
				
				
						
				ticketHistory.setTypeId(Long.parseLong(callTypeId));
				ticketHistory.setAreaId(Long.parseLong(callAreadId));
				ticketHistory.setCategoryId(Long.parseLong(callCatId));
				ticketHistory.setSubCatId(Long.parseLong(callSubCatId));
				ticketHistory.setResolutionId(Integer.parseInt(callResolutionId));
				ticketHistory.setActionId(Long.parseLong(callActionID));
				ticketHistory.setCallS(0);
				ticketHistory.setPriority("NORMAL");
				//ticketHistory.setUpdateRemarks("Remarks");
				ticketHistory.setCallbackRequired(1);
				ticketHistory.setPreferredTime(new Timestamp((new Date()).getTime()));
				ticketHistory.setUserId(userid);
				ticketHistory.setOrderedQuantity(0);
				ticketHistory.setTeamId(Long.parseLong(team_id));
				ticketHistory.setQueueId(Long.parseLong(queueId));
				ticketHistory.setAckSlaViolated(0);
				ticketHistory.setResSlaViolated(0);
				ticketHistory.setWoModifiedLast(new Timestamp((new Date()).getTime()));
				ticketHistory.setModifiedLastBy(userid);
				ticketHistory.setStatus("UNASSIGNED");
				ticketHistory.setUserType("CSR");
				
				ticketHistory.setOapCallLogMasterT(logMaster);
				ticketHistory.setOapWorkOrderMasterT(workOrder);
				
				List<OapTicketUpdateHistoryT> tickethistoryList = new ArrayList<OapTicketUpdateHistoryT>();
				
				tickethistoryList.add(ticketHistory);
				logMaster.setOapTicketUpdateHistoryTs(tickethistoryList);
				workOrder.setOapTicketUpdateHistoryTs(tickethistoryList);				
				
				
				log.info(logPrefix+". Saving logMaster to DB ");
				
				Object result = template.requestBody("direct:saveLogMaster", logMaster);
				
				if(!(result instanceof OapCallLogMasterT)){
					
					log.error("OrderId : "+orderId+". Failed to process TT request ");
					throw new Exception("Failed to process TT request");				
					
				}
				
				long troubleTicketId = ((OapCallLogMasterT) result).getCusLogId();
				long workOrderId = ((OapCallLogMasterT) result).getOapWorkOrderMasterTs().get(0).getWorkOrderId();
				
				log.info(logPrefix+". Trouble ticket create successfully. TicketId : "+troubleTicketId+". WorkOrderId : "+workOrderId);
				
				
				//ttResponse = new CreateTroubleTicketResponse();
				ttResponse.setTicketId(troubleTicketId);
				ttResponse.setOrderId(orderId);
				ttResponse.setResponseCode("0");
				ttResponse.setResponseDesscription("Trouble ticket created successfully");				
				
				
				
			}
			catch(Exception e){
				
				
				//e.printStackTrace();
				log.error("OrderId : "+orderId+". Failed to create trouble ticket Setting Fault Response ");
				ttResponse.setOrderId(orderId);
				ttResponse.setResponseCode("1");
				ttResponse.setResponseDesscription(e.getMessage());				
				
			}
			
		}
	}

}
