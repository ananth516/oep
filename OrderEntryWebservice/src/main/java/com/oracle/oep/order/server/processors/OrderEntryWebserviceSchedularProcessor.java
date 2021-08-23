package com.oracle.oep.order.server.processors;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brm.opcode.root.Opcode;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.PARAMS;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ProcessStartCancelOrderType;
import com.oracle.oep.order.model.ServiceTypeEnum;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.typeConverters.dynamic.OrderActionFilterStringToMapConverter;
import com.oracle.oep.order.utils.OrderNumberGenerator;

public class OrderEntryWebserviceSchedularProcessor implements Processor {

	public static final Logger log = LogManager.getLogger(OrderEntryWebserviceSchedularProcessor.class);
	
	public void process(Exchange exchange) throws Exception {
		
		log.info("OEP Schedular is executing the job. Job fired at "+exchange.getIn().getHeader("fireTime"));
		
		ProducerTemplate template = exchange.getContext().createProducerTemplate();
		Message in=exchange.getIn();	
		
		Map<String,String> map = new HashMap<String,String>();
    	map.put("isFuture", OEPConstants.OEP_IS_FUTURE_DATE_Y);
    	map.put("isPark", OEPConstants.OEP_IS_PARK_Y);
    	map.put("action", OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION);
    	map.put("bbAction", OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION);
    	map.put("mobDisconnect", OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION);
    	map.put("mobileDisconnectFutureDate",OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION);
    	
    	//exchange.getContext().getTypeConverterRegistry().addTypeConverters(new OrderActionFilterStringToMapConverter());
		
		Vector<com.oracle.oep.order.presistence.model.OepOrder> result = (Vector<OepOrder>) template.requestBody("jpa:com.oracle.oep.order.presistence.model.OepOrder?query=select o from com.oracle.oep.order.presistence.model.OepOrder o where "
    			+ " o.isPark=:isPark and o.isFutureDate=:isFuture and o.action in(:action,:bbAction,:mobDisconnect,:mobileDisconnectFutureDate) and o.orderStartDate<=CURRENT_DATE&parameters=\"orderActionFilter\":${body}",map);
		
		log.info("No of Future dated orders found : "+result.size());
		Iterator<OepOrder> iterator = result.iterator();
		
		while(iterator.hasNext()){
			
			
			
			OepOrder oepOrderDO = iterator.next();
			log.info("OEP Order Number : "+oepOrderDO.getOrderId()+". Order Action is : "+oepOrderDO.getAction());
			
			//Query BRM for Account details
			
			if(oepOrderDO.getAction().equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
					||oepOrderDO.getAction().equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION))
			{

				log.info("OEP Order Number : "+oepOrderDO.getOrderId()+" Checking BRM For Collection MobileService-Suspend-TravelReasons");

				String accountNo = null;
				String oepOrder = null;
				String channelName = null;
				CUSOPSEARCHOutputFlist searchResultForAccount=null;
				RESULTS searchResultForAccountresult = null;
				CUSOPSEARCHOutputFlist searchResult = null;
				RESULTS result1 = null;
				
				if(oepOrderDO.getChannelName()!=null)
					channelName = oepOrderDO.getChannelName();
				else
					channelName = OEPConstants.OAP_CHANNEL_NAME;
				
							
				try {
					
					searchResultForAccount = getAccountDataFromBRMForMSISDN(template, oepOrderDO.getServiceNo(), channelName, oepOrderDO.getCsrid(), oepOrderDO.getOrderId());	
					
					searchResultForAccountresult = getActiveServiceFromSearchResult(searchResultForAccount);

					if(searchResultForAccountresult == null){
	
						throw new RuntimeException("No Active Service is Found in BRM");
					}
					
					String billingAccountId = searchResultForAccountresult.getCUSFLDBAACCOUNTNO();
					String accountId = searchResultForAccountresult.getACCOUNTNO();
					String accId = null;

					if (billingAccountId!=null)
					{
						accId = billingAccountId;
					}
					else
					{
						accId = accountId;
					}
					
					searchResult = getAccountDataFromBRMForAccount(template, accId, channelName, oepOrderDO.getCsrid(),oepOrder);	
	
					result1 = getActiveServiceFromSearchResultAccount(searchResult);
	
					if(result1 == null){
	
						throw new RuntimeException("No Active Service is Found in BRM");
					}
				
					int collectionResult = result1.getCUSFLDCOLLECTIONSTATUS().intValue();
					log.info("OEP Order Number : "+oepOrderDO.getOrderId()+" Collection Result "+collectionResult);

					if(collectionResult==1)
					{
						log.info("OEP Order Number : "+oepOrderDO.getOrderId()+" Order will be parked as collection not happened");

						oepOrderDO.setIsFutureDate(OEPConstants.OEP_CONSTANT_N);
						template.sendBody("direct:saveOrderToRespository", oepOrderDO);	

					}
					else
					{
						OrderEntryRequest requestFromOrder = (OrderEntryRequest) template.requestBody("direct:convertToOrderEntryRequest",oepOrderDO.getPayloadXml());
						String orderAction = null;
						String serviceType=null;
						orderAction = getOrderAction(requestFromOrder,template);	
						serviceType = getServiceType(requestFromOrder,template);
						//map to set order haeders
						//as future dated order is already sent to OSM.
						
						Map<String, Object> orderHeaders = new HashMap<String,Object>();
						orderHeaders.put(OEPConstants.ORDER_ACTION_HEADER, orderAction);
						orderHeaders.put(OEPConstants.SERVICE_TYPE_HEADER, serviceType);
						orderHeaders.put(OEPConstants.OEP_ORDER_NUMBER_HEADER, oepOrderDO.getOrderId());
						oepOrderDO.setIsFutureDate(OEPConstants.OEP_CONSTANT_N);
						oepOrderDO.setIsPark(OEPConstants.OEP_CONSTANT_N);
						template.sendBody("direct:saveOrderToRespository", oepOrderDO);	
						template.sendBodyAndHeaders("direct:sendToCancelResumePortInQueue",  oepOrderDO.getPayloadXml(),orderHeaders);					
						
						log.info("Order sent for further processing");	
						
					}
				}
				catch(Exception e){
					
					log.info("OEP Order Number : "+oepOrderDO.getOrderId()+" Order will be parked as collection not happened");

					oepOrderDO.setIsFutureDate(OEPConstants.OEP_CONSTANT_N);
					oepOrderDO.setIsPark(OEPConstants.OEP_CONSTANT_N);
					oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_FAILED);
					oepOrderDO.setStatusDescription(e.getLocalizedMessage());
					template.sendBody("direct:saveOrderToRespository", oepOrderDO);	
					
				}

				

			}
			
			//End of query BRM
			
			/*
			else
			{
			OrderEntryRequest request = new OrderEntryRequest();
			ProcessStartCancelOrderType startOrder = new ProcessStartCancelOrderType();
			startOrder.setOrderId(oepOrderDO.getOrderId());
			startOrder.setOrderAction(OEPConstants.OEP_START_ACTION);
			startOrder.setServiceIdentifier(oepOrderDO.getServiceNo());
			startOrder.setChannelTrackingId(OrderNumberGenerator.generateOrderNumber());
			request.setProcessStartCancelOrder(startOrder);	
			
			log.info("OEP Order Number : "+oepOrderDO.getOrderId()+". Sending the Order to OEP_CANCELRESUMEPORTIN_JMSQUEUE");
			
			template.sendBody("direct:sendToStartCancelOrderQueue", request);
			
			}*/
			else
			{
				OrderEntryRequest requestFromOrder = (OrderEntryRequest) template.requestBody("direct:convertToOrderEntryRequest",oepOrderDO.getPayloadXml());
				String orderAction = null;
				String serviceType=null;
				orderAction = getOrderAction(requestFromOrder,template);	
				serviceType = getServiceType(requestFromOrder,template);
				//map to set order haeders
				
				Map<String, Object> orderHeaders = new HashMap<String,Object>();
				orderHeaders.put(OEPConstants.ORDER_ACTION_HEADER, orderAction);
				orderHeaders.put(OEPConstants.SERVICE_TYPE_HEADER, serviceType);
				orderHeaders.put(OEPConstants.OEP_ORDER_NUMBER_HEADER, oepOrderDO.getOrderId());
				oepOrderDO.setIsFutureDate(OEPConstants.OEP_CONSTANT_N);
				oepOrderDO.setIsPark(OEPConstants.OEP_CONSTANT_N);
				template.sendBody("direct:saveOrderToRespository", oepOrderDO);	
				template.sendBodyAndHeaders("direct:sendToCancelResumePortInQueue",  oepOrderDO.getPayloadXml(),orderHeaders);					
				
				log.info("Order sent for further processing");	
				
				
			}
		
		}		
		
		//template.sendBody("controlbus:route?routeId=pollOrderRepo&action=stop", null);
		
	}
	
	private CUSOPSEARCHOutputFlist getAccountDataFromBRMForAccount(final ProducerTemplate template, 
			final String accountNo, final String channelName, final String userName,final String oepOrder){
		
		
		CUSOPSEARCHInputFlist opcodeSearch = new CUSOPSEARCHInputFlist();
		PARAMS param = new PARAMS();
		opcodeSearch.setPOID(OEPConstants.BRM_POID);
		opcodeSearch.setPROGRAMNAME(channelName);
		opcodeSearch.setUSERNAME(userName);
		opcodeSearch.setFLAGS(new BigInteger("5"));
//		opcodeSearch.setPARAMNAME("MSISDN");
		param.setVALUE(accountNo);
		param.setElem(new BigInteger("0"));		
		opcodeSearch.getPARAMS().add(param);
		
		String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);

		return queryBRMForAccountData(template, inputXML,oepOrder);
		
	}
	
	private CUSOPSEARCHOutputFlist queryBRMForAccountData(final ProducerTemplate template, final String inputXML,final String oepOrderNumber){
		
		CUSOPSEARCHOutputFlist searchResult = null;
		
		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_CUS_SEARCH_OPCODE);
		opcode.setInputXml(inputXML);		
		
		try{
		
		 log.info("OEP order number : "+oepOrderNumber+". Calling BRM webservice to fetch Account data. Input xml : \n"+inputXML);
		 
		 Exchange syncExchange = template.request("direct:getAccountAndPlanDetailsFromBRM", new Processor() {
				
				@Override
				public void process(Exchange exchange) throws Exception {
					// TODO Auto-generated method stub
					Message in = exchange.getIn();
					in.setBody(opcode);
				}
			});
			 
			 Exception cause = syncExchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
			
			 if(cause!=null){
				 
				 throw cause;
				 
			}
			 
		searchResult = (CUSOPSEARCHOutputFlist) syncExchange.getOut().getBody();	


		 
		if(searchResult.getERRORCODE()!=null){
			 
			 log.info("OEP order number : "+oepOrderNumber+". Error while fetching Account data from BRM "+searchResult.getERRORDESCR());
			 throw new RuntimeException(searchResult.getERRORDESCR()); 
			 
			 
		 }
		 
		}
		catch (Exception e) {
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			 log.info("OEP order number : "+oepOrderNumber+". Exception while fetching Account data from BRM "+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return searchResult;
		
	}
	
	private RESULTS getActiveServiceFromSearchResult(CUSOPSEARCHOutputFlist searchResult){
		
		
		for (RESULTS result: searchResult.getRESULTS()){
			
			if((result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_ACTIVE_STATUS_CODE)
				||(result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_SUSPENDED_STATUS_CODE)){
				
				log.info("Found Active Service. ServiceId: "+result.getLOGIN());
				return result;
			}
		}
		return null;
	}
	
	private RESULTS getActiveServiceFromSearchResultAccount(CUSOPSEARCHOutputFlist searchResult){
		
		
		for (RESULTS result: searchResult.getRESULTS()){
						
				log.info("Found Active Service. ServiceId: "+result.getLOGIN());
				return result;
			
		}
		return null;
	}
	
	private CUSOPSEARCHOutputFlist getAccountDataFromBRMForMSISDN(final ProducerTemplate template, 
			final String msisdn, final String channelName, final String userName, final String oepOrderNumber){
		
		
		CUSOPSEARCHInputFlist opcodeSearch = new CUSOPSEARCHInputFlist();
		PARAMS param = new PARAMS();
		opcodeSearch.setPOID(OEPConstants.BRM_POID);
		opcodeSearch.setPROGRAMNAME(channelName);
		opcodeSearch.setUSERNAME(userName);
		opcodeSearch.setFLAGS(new BigInteger("2"));
		opcodeSearch.setPARAMNAME("MSISDN");
		param.setVALUE(msisdn);
		param.setElem(new BigInteger("0"));		
		opcodeSearch.getPARAMS().add(param);
		
		String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);

		return queryBRMForAccountData(template, inputXML, oepOrderNumber);
		
	}
	
	/**
	 * 
	 * This method takes in coming order request and return the Order Action. This is overloaded method which doesn't take order index. 
	 * Use this method for orders whose count is 1
	 * 
	 * **/
	private String getOrderAction(final OrderEntryRequest request,ProducerTemplate template){
		
		String orderAction=null;
		
		orderAction = getOrderAction(request, 0,template);	
		
		
		return orderAction;		
		
	}
	
	/**
	 * 
	 * This method takes in coming order request and return the Order Action. This method is overloaded with a version that doesn't that order index.
	 * 
	 * **/
	private String getOrderAction(final OrderEntryRequest request, final int orderIndex,ProducerTemplate template){
		
		String orderAction=null;
		
		orderAction = (String) template.requestBodyAndHeader("direct:getOrderAction", request,"OrderIndex",orderIndex+1);		
		
		
		return orderAction;		
		
	}
	
	/**
	 *  This method takes in coming order request and returns the ServiceType
	 * 
	 * **/
	
	private String getServiceType(final OrderEntryRequest request,ProducerTemplate template){
		
		String serviceType=null;
		
		serviceType = (String) template.requestBody("direct:getServiceType", request);				
		
		return serviceType;	
	}

}
