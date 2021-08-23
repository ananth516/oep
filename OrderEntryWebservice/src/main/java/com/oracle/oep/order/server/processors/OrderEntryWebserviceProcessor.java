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
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import com.oracle.oep.brm.opcodes.CUSOPCCUPDATESERVICEOutputFlist;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.AddonActionEnum;
import com.oracle.oep.order.model.ListOfOEPOrderResponseType;
import com.oracle.oep.order.model.OEPOrderResponseType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.OrderEntryResponse;
import com.oracle.oep.order.model.ProcessOrderResponseType;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.presistence.repository.OepOrderRepository;
import com.oracle.oep.order.utils.OEPRequestValidator;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.order.utils.OrderNumberGenerator;

public class OrderEntryWebserviceProcessor implements Processor {

	public static final Logger log = LogManager.getLogger(OrderEntryWebserviceProcessor.class);

	@Autowired
	private ProducerTemplate template;
	
	@Autowired
	private OepOrderRepository oepOrderRepo;
	

	@Override
	public void process(Exchange exchange) throws Exception {

		Message in = exchange.getIn();

		OrderEntryRequest request = in.getBody(OrderEntryRequest.class);

		if (request.getProcessManagePortInOrder() != null || request.getProcessStartCancelOrder() != null
				|| request.getProcessUpdateServiceSuspensionDate() != null) {
			processStartCancelAndPortInOrder(exchange, request);
		}
		else if (request.getProcessCollectionBarring() != null) {
			processBarringOrder(exchange, request);
		}
		else {
			processFulfillmentOrder(exchange, request);
		}

	}

	/**
	 * 
	 * This method takes in coming order request and return the count of orders
	 * 
	 **/
	private int getOrderCount(final OrderEntryRequest request) {

		int orderCount = 1;

		if (request.getProcessSuspendOrResumeOrder() != null) {

			orderCount = request.getProcessSuspendOrResumeOrder().getListOfOEPOrder().getOEPOrder().size();

		}

		return orderCount;

	}

	/**
	 * 
	 * This method takes in coming order request and return the Order Action.
	 * This method is overloaded with a version that doesn't that order index.
	 * 
	 **/
	private String getOrderAction(final OrderEntryRequest request, final int orderIndex) {

		String orderAction = null;

		orderAction = (String) template.requestBodyAndHeader("direct:getOrderAction", request, "OrderIndex",
				orderIndex + 1);

		return orderAction;

	}

	/**
	 * 
	 * This method takes in coming order request and return the Service
	 * Identifier.
	 * 
	 **/
	private String getServiceIdentifier(final OrderEntryRequest request, final int orderIndex) {

		String serviceIdentifier = null;

		serviceIdentifier = (String) template.requestBodyAndHeader("direct:getServiceIdentifier", request, "OrderIndex",
				orderIndex + 1);

		return serviceIdentifier;

	}

	/**
	 * This method takes in coming order request and returns the ServiceType
	 * 
	 **/

	private String getServiceType(final OrderEntryRequest request) {

		String serviceType = null;

		serviceType = (String) template.requestBody("direct:getServiceType", request);

		return serviceType;
	}

	/**
	 * 
	 * This method takes in coming order request and return the Order Action.
	 * This is overloaded method which doesn't take order index. Use this method
	 * for orders whose count is 1
	 * 
	 **/
	private String getOrderAction(final OrderEntryRequest request) {

		String orderAction = null;

		orderAction = getOrderAction(request, 0);

		return orderAction;

	}

	/**
	 * 
	 * This method generates order ID and sends the incoming order to OEP sales
	 * order queue
	 * 
	 **/
	public void processFulfillmentOrder(Exchange exchange, OrderEntryRequest request) throws Exception {

		Message in = exchange.getIn();
		Message out = null;
		String orderAction = null;
		String channelTrackingId = null;
		String serviceType = null;

		// Initialize response objects
		OrderEntryResponse mainResponse = new OrderEntryResponse();
		ProcessOrderResponseType processOrderResponse = new ProcessOrderResponseType();
		ListOfOEPOrderResponseType listOepOrder = new ListOfOEPOrderResponseType();

		try {

			int orderCount = getOrderCount(request);

			for (int i = 0; i < orderCount; i++) {

				final String oepOrderNumber = OrderNumberGenerator.generateOrderNumber(template);
				log.info("Generated OEP Order Number : " + oepOrderNumber);

				log.info("OEP Order Number : " + oepOrderNumber + "-Order payload : \n" + in.getBody(String.class));

				orderAction = getOrderAction(request, i);
				serviceType = getServiceType(request);
				String serviceIdentifier = getServiceIdentifier(request, i);
				String channelName = template.requestBodyAndHeader("direct:getAccountParams", in.getBody(String.class),
						"param", "ChannelName").toString();

				checkOpenOrders(request, orderAction, channelName, i);
				channelTrackingId = OrderDataUtils.getChannelTrackingId(request, orderAction, i);

				log.info("OEP Order Number : " + oepOrderNumber + ", ChannelTrackingID : " + channelTrackingId
						+ ", OrderAction : " + orderAction + ", Order Count : " + orderCount);

				// Calling Validate Request

				OEPRequestValidator.validateRequest(request, orderAction, template, channelName);

				// map to set order haeders
				Map<String, Object> orderHeaders = new HashMap<>();
				orderHeaders.put(OEPConstants.ORDER_ACTION_HEADER, orderAction);
				orderHeaders.put(OEPConstants.SERVICE_TYPE_HEADER, serviceType);
				orderHeaders.put(OEPConstants.OEP_ORDER_NUMBER_HEADER, oepOrderNumber);
				orderHeaders.put(OEPConstants.JMS_CORRELATION_ID, oepOrderNumber);

				try {

					// Forward the request to oep sales order jms queue
					saveOrder(oepOrderNumber, orderAction, serviceIdentifier, in.getBody(String.class));

					if (orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)) {
						log.info("OEP Order Number : " + oepOrderNumber + " Setting Priority 9");
						template.sendBodyAndHeaders("direct:sendToSalesOrderPriorityQueue", in.getBody(Document.class),
								orderHeaders);
					} else {
						log.info("OEP Order Number : " + oepOrderNumber + " Setting Priority 4");
						template.sendBodyAndHeaders("direct:sendToSalesOrderQueue", in.getBody(Document.class),
								orderHeaders);
					}

					log.info("OEP Order Number : " + oepOrderNumber + ". Request Sent to JMS Queue");

					OEPOrderResponseType oepOrderResponse = new OEPOrderResponseType();

					oepOrderResponse.setOrderId(oepOrderNumber);
					oepOrderResponse.setChannelTrackingId(channelTrackingId);
					oepOrderResponse.setStatusCode(OEPConstants.OEP_SUCCESS_STATUS_CODE);
					oepOrderResponse.setStatusDescription(OEPConstants.OEP_SUCCESS_STATUS_DESCRIPTION);

					if (orderAction.equalsIgnoreCase(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)) {

						log.info("AddOnAction : " + request.getProcessAddOnManagment().getAction().value());

						if (request.getProcessAddOnManagment().getAction().value().equals(AddonActionEnum.ADD.value())
								&& request.getProcessAddOnManagment().getChannelName()
										.equalsIgnoreCase(OEPConstants.OEP_RELOAD_CHANNEL_NAME)) {

							//checkOrderCompletion(oepOrderNumber, oepOrderResponse);

						}
					}

					listOepOrder.getOEPOrderResponse().add(oepOrderResponse);

				}

				catch (Exception e) {

					log.error("OEP Order Number : " + oepOrderNumber + "Failed to send request to Sales Order Queue"
							+ e.getMessage());
					e.printStackTrace();
					throw new Exception(OEPConstants.OEP_FAILURE_STATUS_DESCRIPTION);

				}

			}

		}

		catch (Exception e) {

			log.error("Failed to send request to Sales Order Queue" + e.getMessage());
			e.printStackTrace();
			String errorMessage = null;
			if (e.getCause() != null) {

				errorMessage = e.getCause().getLocalizedMessage();
			}

			else {

				errorMessage = e.getLocalizedMessage();
			}

			errorMessage = errorMessage.length() > 200 ? errorMessage.substring(0, 200) : errorMessage;

			log.error("Failed to send request to Sales Order Queue " + errorMessage);

			OEPOrderResponseType oepOrderResponse = new OEPOrderResponseType();

			oepOrderResponse.setStatusCode(OEPConstants.OEP_FAILURE_STATUS_CODE);
			oepOrderResponse.setStatusDescription(errorMessage);
			listOepOrder.getOEPOrderResponse().add(oepOrderResponse);

		}

		finally {

			processOrderResponse.setListOfOEPOrderResponse(listOepOrder);
			mainResponse.setProcessOrderResponse(processOrderResponse);

			log.info("Setting response on exchange.getMessage()");

			out = exchange.getMessage();
			out.setBody(mainResponse);

			request = null;
			listOepOrder = null;
			processOrderResponse = null;
			mainResponse = null;

		}

	}

	private void checkOpenOrders(OrderEntryRequest request, String orderAction, String channelName, int i) {

		if (OEPConstants.OEP_PORTOUT_MOBILE_SERVICE_ACTION.equalsIgnoreCase(orderAction)
				|| OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION.equalsIgnoreCase(orderAction)
				|| OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION.equalsIgnoreCase(orderAction)
				|| OEPConstants.OEP_START_ACTION.equalsIgnoreCase(orderAction)
				|| OEPConstants.OEP_CANCEL_ACTION.equalsIgnoreCase(orderAction)
				|| OEPConstants.OEP_UPDATE_ACTION.equalsIgnoreCase(orderAction)
				|| OEPConstants.BRM_CHANNEL_NAME.equals(channelName)
				|| (request.getProcessDisconnectOrder() != null
						&& OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION.equalsIgnoreCase(orderAction)
						&& OEPConstants.BRM_PREPAID_LIFECYCLE_TERMINATE_SERVICE_ACTION
								.equalsIgnoreCase(request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder()
										.getMobileService().getServiceDetails().getOrderName())
						&& request.getProcessDisconnectOrder().getListOfOEPOrder().getOEPOrder().getMobileService()
								.getServiceDetails().getOrderStartDate().toGregorianCalendar().getTime()
								.after(new Date()))) {

			return;
		}

		else {

			log.info("checkOpenOrders : Fetching Service Identifier");
			String serviceIdentifier = getServiceIdentifier(request, i);

			log.info("checkOpenOrders : Service Identifier : " + serviceIdentifier);
			
			log.info("template Name {}",template.toString());

			Map<String, String> params = new HashMap<>();

			params.put("param", serviceIdentifier);
			
			
			List<OepOrder> orderList = oepOrderRepo.findByServiceNo(serviceIdentifier);
			
			log.info("Result object {} ",orderList.size());

			int count = 0;

			/*if (!orderList.isEmpty()) {

				log.info("Service Identifier : " + serviceIdentifier + ". Found Open Orders");

				for (OepOrder oepOrder : orderList) {
					if (OEPConstants.OEP_ORDER_STATUS_IN_PROGRESS.equalsIgnoreCase(oepOrder.getStatusCode())
							|| OEPConstants.OEP_ORDER_STATUS_OPEN.equalsIgnoreCase(oepOrder.getStatusCode())) {

						if ((OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION.equalsIgnoreCase(oepOrder.getAction())
								|| OEPConstants.OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION
										.equalsIgnoreCase(oepOrder.getAction()))
								&& OEPConstants.OEP_IS_PARK_Y.equalsIgnoreCase(oepOrder.getIsPark())
								&& OEPConstants.OEP_IS_FUTURE_DATE_Y.equalsIgnoreCase(oepOrder.getIsFutureDate())) {

							continue;
						}
						count++;
					}
				}

				if (count > 0)
					throw new RuntimeException(OEPConstants.OEP_OPEN_ORDER_FAILURE_STATUS);
			}

			else {

				return;
			}*/
			
			
			log.info("On Exit ... ");

		}

	}

	public void processStartCancelAndPortInOrder(Exchange exchange, OrderEntryRequest request) {

		Message in = exchange.getIn();
		Message out = null;
		String oepOrderNumber = null;
		String orderAction = null;
		String serviceType = null;
		String channelTrackingId = null;
		String msisdn = null;
		// Initialize response objects
		OrderEntryResponse mainResponse = new OrderEntryResponse();
		ProcessOrderResponseType processOrderResponse = new ProcessOrderResponseType();
		ListOfOEPOrderResponseType listOepOrder = new ListOfOEPOrderResponseType();
		OEPOrderResponseType oepOrderResponse = new OEPOrderResponseType();
		OepOrder oepOrderDO = null;

		try {

			if (request.getProcessStartCancelOrder() != null) {

				oepOrderNumber = request.getProcessStartCancelOrder().getOrderId();

			}

			else if (request.getProcessUpdateServiceSuspensionDate() != null) {

				oepOrderDO = queryOEPForOrderPortIn(
						request.getProcessUpdateServiceSuspensionDate().getServiceIdentifier());

				oepOrderNumber = oepOrderDO.getOrderId();
			}

			// Forward the request to oep sales order jms queue

			else if (request.getProcessManagePortInOrder() != null) {

				msisdn = request.getProcessManagePortInOrder().getMsIsdn();
				oepOrderDO = this.queryOEPForOrderPortIn(msisdn);
				oepOrderNumber = oepOrderDO.getOrderId();

			} else {
				oepOrderDO = this.queryOEPForOrder(oepOrderNumber);
			}

			log.info("OEP Order Number : " + oepOrderNumber + "-Order payload : \n" + in.getBody(String.class));

			orderAction = getOrderAction(request);
			serviceType = getServiceType(request);

			channelTrackingId = OrderDataUtils.getChannelTrackingIdPortIn(request, orderAction);
			log.info("OEP Order Number : " + oepOrderNumber + ", ChannelTrackingID : " + channelTrackingId
					+ ", OrderAction : " + orderAction);

			// map to set order haeders
			Map<String, Object> orderHeaders = new HashMap<>();
			orderHeaders.put(OEPConstants.ORDER_ACTION_HEADER, orderAction);
			orderHeaders.put(OEPConstants.SERVICE_TYPE_HEADER, serviceType);
			orderHeaders.put(OEPConstants.OEP_ORDER_NUMBER_HEADER, oepOrderNumber);

			log.info("OEP Order Number : " + oepOrderNumber + " Status code for OEP databse  : "
					+ oepOrderDO.getStatusCode());

			if (orderAction.equals(OEPConstants.OEP_START_ACTION)) {

				if (oepOrderDO.getIsPark().equals(OEPConstants.OEP_CONSTANT_Y)) {

					oepOrderDO.setIsPark(OEPConstants.OEP_CONSTANT_N);
					oepOrderDO.setIsFutureDate(OEPConstants.OEP_CONSTANT_N);
					oepOrderDO.setLastModifiedDate(new Timestamp(new Date().getTime()));
					oepOrderDO.setOrderStartDate(new Timestamp(new Date().getTime()));

					template.sendBody("jpa:com.oracle.oep.order.presistence.model.OepOrder", oepOrderDO);

					/*
					 * if(originalOrderAction.equals(OEPConstants.
					 * OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION) ||
					 * originalOrderAction.equals(OEPConstants.
					 * OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)){
					 * 
					 * template.sendBodyAndHeaders(
					 * "direct:sendToStartCancelOrderQueue",
					 * in.getBody(Document.class), orderHeaders);
					 * log.info("OEP Order Number : "
					 * +oepOrderNumber+" Sent to StartCancelOrder ");
					 * 
					 * 
					 * } else {
					 * 
					 * template.sendBodyAndHeaders(
					 * "direct:sendToCancelResumePortInQueue",
					 * oepOrderDO.getPayloadXml(),orderHeaders); }
					 */

					template.sendBodyAndHeaders("direct:sendToCancelResumePortInQueue", oepOrderDO.getPayloadXml(),
							orderHeaders);

					log.info("Order sent for further processing");

				}

				else {

					throw new Exception("Park staus from oep database " + oepOrderDO.getIsPark() + "  is not valid");

				}
			}

			else if (orderAction.equals(OEPConstants.OEP_CANCEL_ACTION)) {
				if (oepOrderDO.getStatusCode().equals(OEPConstants.OEP_ORDER_STATUS_OPEN)) {

					log.info("Cancel order recieved");
					oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_CANCELLED);
					oepOrderDO.setLastModifiedDate(new Timestamp(new Date().getTime()));
					template.sendBody("jpa:com.oracle.oep.order.presistence.model.OepOrder", oepOrderDO);

				}

				else {

					throw new Exception(
							"Status Code from OEP database " + oepOrderDO.getStatusCode() + "  is not valid");

				}

			} else if (orderAction.equals(OEPConstants.OEP_UPDATE_ACTION)) {

				ProcessStartCancelOrderFulfillmentProcessor startOrderProcessor = new ProcessStartCancelOrderFulfillmentProcessor();
				CUSOPCCUPDATESERVICEOutputFlist updateResult = startOrderProcessor.processUpdateSuspensionDate(template,
						oepOrderDO, request);

				if (updateResult.getSTATUS() != null && "1".equals(updateResult.getSTATUS().toString())) {

					log.info("OEP Order Number : " + oepOrderNumber + ". UpdateService Error Code : "
							+ updateResult.getERRORCODE() + ". Error Description : " + updateResult.getERRORDESCR());
					throw new Exception(updateResult.getERRORDESCR());
				}

				oepOrderDO.setOrderStartDate(OrderDataUtils.getTimeStampFromCalender(
						request.getProcessUpdateServiceSuspensionDate().getOrderResumeDate()));
				oepOrderDO.setLastModifiedDate(new Timestamp(new Date().getTime()));

				saveOrder(oepOrderDO);

			}

			else {

				throw new Exception("OrderAction " + orderAction + " is not valid");

			}

			log.info("OEP Order Number : " + oepOrderNumber + ". Request Sent to JMS Queue");
			oepOrderResponse.setOrderId(oepOrderNumber);
			oepOrderResponse.setChannelTrackingId(channelTrackingId);
			oepOrderResponse.setStatusCode(OEPConstants.OEP_SUCCESS_STATUS_CODE);
			oepOrderResponse.setStatusDescription(OEPConstants.OEP_SUCCESS_STATUS_DESCRIPTION);
			// saveOrder(oepOrderNumber,in.getBody(String.class));

		}

		catch (Exception e) {

			log.info("OrderNumber : " + oepOrderNumber + ": Failed to send request to Sales Order Queue"
					+ e.getMessage());
			e.printStackTrace();

			oepOrderResponse.setStatusCode(OEPConstants.OEP_FAILURE_STATUS_CODE);
			oepOrderResponse.setStatusDescription(e.getMessage());

		}

		finally {

			listOepOrder.getOEPOrderResponse().add(oepOrderResponse);
			processOrderResponse.setListOfOEPOrderResponse(listOepOrder);
			mainResponse.setProcessOrderResponse(processOrderResponse);

			log.info("OrderNumber : " + oepOrderNumber + ": setting response on exchange");

			out = exchange.getOut();
			out.setHeader("OEP_ORDER_NUMBER", oepOrderNumber);
			out.setBody(mainResponse);

			request = null;
			oepOrderResponse = null;
			listOepOrder = null;
			processOrderResponse = null;
			mainResponse = null;

		}

	}

	/**
	 * Query Order from DB
	 * 
	 * @param orderId
	 * @param template
	 * @return
	 */
	private OepOrder queryOEPForOrder(final String orderId) {

		log.info("OEP Order Number : " + orderId + ".Querying OEP ORDER from OEP DB for " + orderId);

		Map<String, String> planMap = new HashMap<>();

		planMap.put("param", orderId);

		Vector<OepOrder> oepOrder = (Vector) template.requestBody("direct:queryOrderFromOEP", planMap);

		Iterator<OepOrder> iterator = oepOrder.listIterator();

		OepOrder oepOrderObject = null;

		while (iterator.hasNext()) {

			oepOrderObject = iterator.next();

		}

		if (oepOrderObject == null) {

			log.info("Order with Order number : " + orderId + " is not found.");

			throw new RuntimeException("Order with Order number : " + orderId + " is not found.");

		}

		log.info("OEP Order Number : " + orderId + ". Returning for Querying");
		return oepOrderObject;

	}

	/**
	 * 
	 * Save Order to the database
	 * 
	 * @param OepOrderNumber
	 * @throws Exception
	 */
	private void saveOrder(String oepOrderNumber, String orderPayload) throws Exception {

		OepOrder oepOrderDO = new OepOrder();

		oepOrderDO.setOrderId(oepOrderNumber);
		oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		oepOrderDO.setStatusDescription(OEPConstants.OEP_ORDER_CREATED_STATUS_DESCRIPTION);
		oepOrderDO.setPayloadXml(orderPayload);
		oepOrderDO.setOrderCreationDate(new Timestamp(new Date().getTime()));

		Exchange retunedExchange = template.send("jpa:com.oracle.oep.order.presistence.model.OepOrder",
				ExchangePattern.InOut, new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {

						Message in = exchange.getIn();
						in.setBody(oepOrderDO);
					}
				});

		if (retunedExchange.getException() != null) {

			log.info("##############################################");
			log.error(retunedExchange.getException().getCause().getMessage());
			throw new Exception(retunedExchange.getException().getCause().getMessage());
		}

	}

	/**
	 * 
	 * Save Order to the database
	 * 
	 * @param OepOrderNumber
	 * @throws Exception
	 */
	private void saveOrder(String oepOrderNumber, String orderAction, String serviceIdentifier, String orderPayload)
			throws Exception {

		OepOrder oepOrderDO = new OepOrder();

		oepOrderDO.setOrderId(oepOrderNumber);
		oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		oepOrderDO.setStatusDescription(OEPConstants.OEP_ORDER_CREATED_STATUS_DESCRIPTION);
		oepOrderDO.setPayloadXml(orderPayload);
		oepOrderDO.setOrderCreationDate(new Timestamp(new Date().getTime()));
		oepOrderDO.setServiceNo(serviceIdentifier);
		oepOrderDO.setAction(orderAction);

		Exchange retunedExchange = template.send("jpa:com.oracle.oep.order.presistence.model.OepOrder",
				ExchangePattern.InOut, new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {

						Message in = exchange.getIn();
						in.setBody(oepOrderDO);
					}
				});

		if (retunedExchange.getException() != null) {

			log.info("##############################################");
			log.error(retunedExchange.getException().getCause().getMessage());
			throw new Exception(retunedExchange.getException().getCause().getMessage());
		}

	}

	/**
	 * Persist OepOrder enity bean to OEP DB
	 * 
	 * @param oepOrder
	 * @param template
	 * @param oepOrderNumber
	 * @throws Exception
	 */

	private void saveOrder(final OepOrder oepOrder) throws Exception {

		// template.sendBody("jpa:com.oracle.oep.order.presistence.model.OepOrder",
		// oepOrder);

		Exchange retunedExchange = template.send("jpa:com.oracle.oep.order.presistence.model.OepOrder",
				ExchangePattern.InOut, new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {

						Message in = exchange.getIn();
						in.setBody(oepOrder);
					}
				});

		if (retunedExchange.getException() != null) {

			log.info("##############################################");
			log.error(retunedExchange.getException().getCause().getMessage());
			throw new Exception(retunedExchange.getException().getCause().getMessage());
		}

	}

	private OepOrder queryOEPForOrderPortIn(final String msisdn) {

		log.info("OEP Order Number : " + ".Querying OEP ORDER from OEP DB for " + msisdn);

		Map<String, String> planMap = new HashMap<>();

		planMap.put("param", msisdn);

		ArrayList<OepOrder> oepOrder = (ArrayList) template.requestBody("direct:queryOrderFromOEPMSISDN", planMap);

		Iterator<OepOrder> iterator = oepOrder.listIterator();

		OepOrder oepOrderObject = null;
		OepOrder arrayoepOrderObject = null;

		while (iterator.hasNext()) {

			arrayoepOrderObject = iterator.next();

			if (arrayoepOrderObject.getIsPark() != null) {
				log.info("IsPark : " + arrayoepOrderObject.getIsPark() + " StatusCode : "
						+ arrayoepOrderObject.getStatusCode() + "Action : " + arrayoepOrderObject.getAction());

				if (arrayoepOrderObject.getIsPark().equals(OEPConstants.OEP_IS_PARK_Y)
						&& arrayoepOrderObject.getStatusCode().equals(OEPConstants.OEP_ORDER_STATUS_OPEN)
						&& (arrayoepOrderObject.getAction().equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION)
								|| arrayoepOrderObject.getAction().equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION)
								|| arrayoepOrderObject.getAction()
										.equals(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
								|| arrayoepOrderObject.getAction()
										.equals(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION)
								|| arrayoepOrderObject.getAction()
										.equals(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION))) {
					oepOrderObject = arrayoepOrderObject;
					log.info("OEPOrder found : " + oepOrderObject.getOrderId());

				}
			}

		}

		if (oepOrderObject == null) {

			log.info("Order with MSISDN : " + msisdn + " is not found.");

			throw new RuntimeException("Order with MSISDN : " + msisdn + " is not found.");

		}

		log.info("MSISDN Number : " + msisdn + ". Returning for Querying");
		return oepOrderObject;

	}

	/**
	 * 
	 * This method generates order ID and sends the incoming order to OEP sales
	 * order queue
	 * 
	 **/
	public void processBarringOrder(Exchange exchange, OrderEntryRequest request) throws Exception {

		Message in = exchange.getIn();
		Message out = null;
		String orderAction = null;
		String channelTrackingId = null;
		String serviceType = null;

		// Initialize response objects
		OrderEntryResponse mainResponse = new OrderEntryResponse();
		ProcessOrderResponseType processOrderResponse = new ProcessOrderResponseType();
		ListOfOEPOrderResponseType listOepOrder = new ListOfOEPOrderResponseType();

		try {

			final String oepOrderNumber = OrderNumberGenerator.generateOrderNumber(template);
			log.info("Generated OEP Order Number : " + oepOrderNumber);

			log.info("OEP Order Number : " + oepOrderNumber + "-Order payload : \n" + in.getBody(String.class));

			orderAction = getOrderAction(request);
			serviceType = getServiceType(request);

			channelTrackingId = OrderDataUtils.getChannelTrackingId(request, orderAction, 0);

			log.info("OEP Order Number : " + oepOrderNumber + ", ChannelTrackingID : " + channelTrackingId
					+ ", OrderAction : " + orderAction);

			// Calling Validate Request

			// validateRequest(request, orderAction, template);

			// map to set order haeders
			Map<String, Object> orderHeaders = new HashMap<>();
			orderHeaders.put(OEPConstants.ORDER_ACTION_HEADER, orderAction);
			orderHeaders.put(OEPConstants.SERVICE_TYPE_HEADER, serviceType);
			orderHeaders.put(OEPConstants.OEP_ORDER_NUMBER_HEADER, oepOrderNumber);

			try {

				saveOrder(oepOrderNumber, in.getBody(String.class));
				// Forward the request to oep sales order jms queue
				if (request.getProcessCollectionBarring() != null
						&& request.getProcessCollectionBarring().getBarringAction() != null
						&& request.getProcessCollectionBarring().getBarringAction()
								.equals(OEPConstants.ORDER_UNBARRINGACTION_VALUE)) {
					log.info("OEP Order Number : " + oepOrderNumber + " Setting Priority 9");
					template.sendBodyAndHeaders("direct:sendToSalesOrderPriorityQueue", in.getBody(Document.class),
							orderHeaders);
				} else {
					log.info("OEP Order Number : " + oepOrderNumber + " Setting Priority 4");
					template.sendBodyAndHeaders("direct:sendToSalesOrderQueue", in.getBody(Document.class),
							orderHeaders);
				}
				log.info("OEP Order Number : " + oepOrderNumber + ". Request Sent to JMS Queue");

				OEPOrderResponseType oepOrderResponse = new OEPOrderResponseType();

				oepOrderResponse.setOrderId(oepOrderNumber);
				oepOrderResponse.setChannelTrackingId(channelTrackingId);
				oepOrderResponse.setStatusCode(OEPConstants.OEP_SUCCESS_STATUS_CODE);
				oepOrderResponse.setStatusDescription(OEPConstants.OEP_SUCCESS_STATUS_DESCRIPTION);

				listOepOrder.getOEPOrderResponse().add(oepOrderResponse);

			}

			catch (Exception e) {

				log.info("OEP Order Number : " + oepOrderNumber + "Failed to send request to Sales Order Queue"
						+ e.getMessage());
				e.printStackTrace();
				throw new Exception(OEPConstants.OEP_FAILURE_STATUS_DESCRIPTION);

			}

		}

		catch (Exception e) {

			log.info("Failed to send request to Sales Order Queue" + e.getMessage());
			e.printStackTrace();

			OEPOrderResponseType oepOrderResponse = new OEPOrderResponseType();

			oepOrderResponse.setStatusCode(OEPConstants.OEP_FAILURE_STATUS_CODE);
			oepOrderResponse.setStatusDescription(e.getMessage());
			listOepOrder.getOEPOrderResponse().add(oepOrderResponse);

		}

		finally {

			processOrderResponse.setListOfOEPOrderResponse(listOepOrder);
			mainResponse.setProcessOrderResponse(processOrderResponse);

			log.info("Setting response on exchange");

			out = exchange.getOut();
			out.setBody(mainResponse);
			request = null;
			listOepOrder = null;
			processOrderResponse = null;
			mainResponse = null;

		}

	}

	private void checkOrderCompletion(String oepOrderNumber, OEPOrderResponseType oepOrderResponse) throws Exception {

		int retry = Integer.parseInt(template.getCamelContext().resolvePropertyPlaceholders("{{sync.retry}}"));
		long timeout = Long.parseLong(template.getCamelContext().resolvePropertyPlaceholders("{{sync.retry.interval}}"))
				* 1000;

		long expectedTime = System.currentTimeMillis() + timeout;

		while (true) {

			if (System.currentTimeMillis() >= expectedTime) {

				log.info("OEP Order Number : " + oepOrderNumber + " .Time out after " + expectedTime);

				expectedTime = System.currentTimeMillis() + timeout;

				retry--;

				OepOrder oepOrderDO = this.queryOEPForOrder(oepOrderNumber);

				log.info("OEP Order Number : " + oepOrderNumber + " . Order Status " + oepOrderDO.getStatusCode());

				if (oepOrderDO.getStatusCode() != null
						&& oepOrderDO.getStatusCode().equals(OEPConstants.OEP_ORDER_STATUS_FAILED)) {

					oepOrderResponse.setStatusCode(oepOrderDO.getStatusCode());
					oepOrderResponse.setStatusDescription(oepOrderDO.getStatusDescription());
					break;

				}

				else if (oepOrderDO.getStatusCode() != null
						&& oepOrderDO.getStatusCode().equalsIgnoreCase(OEPConstants.OEP_ORDER_STATUS_COMPLETE)) {

					oepOrderResponse.setStatusCode(OEPConstants.OEP_ORDER_STATUS_COMPLETE);
					oepOrderResponse.setStatusDescription(OEPConstants.OEP_ORDER_STATUS_COMPLETE_DESCRIPTION);
					break;
				}

				else {

					boolean isResponseSet = false;

					for (OepOrderLine orderLine : oepOrderDO.getOepOrderLines()) {

						if (!orderLine.getParentOrderLineId().equals(orderLine.getOrderLineId())) {

							switch (orderLine.getStatusCode()) {

							case "BILLING FAILED":
								oepOrderResponse.setStatusCode(OEPConstants.OEP_ORDER_STATUS_FAILED);
								oepOrderResponse.setStatusDescription(orderLine.getStatusDescription());
								log.info("OEP Order Number : " + oepOrderNumber + " . Line Status "
										+ orderLine.getStatusCode());
								isResponseSet = true;
								break;

							case "FULFILL BILLING COMPLETE":
							case "PROVISION START":
							case "PROVISION FAILED":
							case "PROVISION COMPLETE":
								oepOrderResponse.setStatusCode(OEPConstants.OEP_ORDER_STATUS_COMPLETE);
								oepOrderResponse
										.setStatusDescription(OEPConstants.OEP_ORDER_STATUS_COMPLETE_DESCRIPTION);
								log.info("OEP Order Number : " + oepOrderNumber + " . Line Status "
										+ orderLine.getStatusCode());
								isResponseSet = true;
								break;

							default:

								if (!(retry > 0)) {

									log.info("OEP Order Number : " + oepOrderNumber + ". Retries expired.");
									oepOrderResponse.setStatusCode(OEPConstants.OEP_ORDER_STATUS_FAILED);
									oepOrderResponse.setStatusDescription(null);
									isResponseSet = true;
								}

							}

						}
					}

					if (isResponseSet)
						break;
				}

			}

		}

	}

}
