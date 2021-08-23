package com.oracle.oep.bulkprocessing.services.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;



import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.oracle.oep.bulkordermanagement.order.v1.AttributeType;
import com.oracle.oep.bulkordermanagement.order.v1.BatchIdBatchType;
import com.oracle.oep.bulkordermanagement.order.v1.BatchType;
import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderEntryRequest;
import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderEntryResponse;
import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderParamsType;
import com.oracle.oep.bulkordermanagement.order.v1.FailOrderType;
import com.oracle.oep.bulkordermanagement.order.v1.FileOrderBatchType;
import com.oracle.oep.bulkordermanagement.order.v1.OrderType;
import com.oracle.oep.bulkordermanagement.order.v1.ProcessBulkOrderResponseType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryBatchIdResponseType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryBatchesResponseType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryFailedBatchesResponseType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryFailedBatchesType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryFailedRecordsResponseType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryWithBatchIdResponseType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryWithBatchIdType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryWithFileOrderIdResponseType;
import com.oracle.oep.bulkprocessing.constants.BulkOrderConstants;
import com.oracle.oep.bulkprocessing.model.BulkBatchMaster;
import com.oracle.oep.bulkprocessing.model.BulkJobMaster;
import com.oracle.oep.bulkprocessing.model.OepOrder;
import com.oracle.oep.bulkprocessing.repository.BulkJobMasterRepository;
import com.oracle.oep.bulkprocessing.repository.OepOrderRepository;
import com.oracle.oep.bulkprocessing.services.BulkOrderWebservice;

@Service
public class BulkOrderWebserviceImpl implements BulkOrderWebservice{
	
	private static final Logger log = LoggerFactory.getLogger(BulkOrderWebserviceImpl.class);
	
	@Autowired
	private BulkJobMasterRepository bulkJobMasterRepo;
	
	@Autowired
	private OepOrderRepository oepOrderRepo;
	
	@Autowired
	private ProducerTemplate template;

	@Override
	public BulkOrderEntryResponse processOrder(Exchange exchange, BulkOrderEntryRequest bulkOrderRequest) {
		
		BulkOrderEntryResponse bulkOrderResponse = new BulkOrderEntryResponse();
		ProcessBulkOrderResponseType processBulkOrderResponse = new ProcessBulkOrderResponseType();
		try {
			createBulkJobMaster(bulkOrderRequest);
			log.info("Bulk Order record persisited {} ",bulkOrderRequest.getProcessBulkOrderRequest().getBulkOrderParams().getFileOrderId());
			
			processBulkOrderResponse.setFileOrderId(bulkOrderRequest.getProcessBulkOrderRequest().getBulkOrderParams().getFileOrderId());
			processBulkOrderResponse.setStatusCode(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
			processBulkOrderResponse.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_SENT);
				
		}
		catch(Exception e) {
			
			log.error("Order Processing Failed ",e);
			
			processBulkOrderResponse.setStatusCode(BulkOrderConstants.OEP_ORDER_FAILED_STATUS);
			processBulkOrderResponse.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_SENDING_FAILED);
			
		}
		
		bulkOrderResponse.setProcessBulkOrderResponse(processBulkOrderResponse);
		return bulkOrderResponse;
	}

	private BulkJobMaster createBulkJobMaster(BulkOrderEntryRequest bulkOrderRequest) {
		
		BulkJobMaster jobMaster = new BulkJobMaster();
		BulkOrderParamsType params = bulkOrderRequest.getProcessBulkOrderRequest().getBulkOrderParams();

		jobMaster.setFileOrderId(params.getFileOrderId());
		jobMaster.setFilePath(params.getFileDirectory() + "/" + params.getFileName());
		jobMaster.setAction(params.getAction().name());
		jobMaster.setJobCreationDate(new Timestamp(new Date().getTime()));
		jobMaster.setCsrId(params.getOrderUserId());
		jobMaster.setStatus(BulkOrderConstants.OEP_ORDER_INITIAL_STATUS);
		
		return bulkJobMasterRepo.saveAndFlush(jobMaster);
	}

	@Override
	public BulkOrderEntryResponse queryByFileOrderId(Exchange exchange, BulkOrderEntryRequest bulkOrderRequest) {
		
		String fileOrderId = bulkOrderRequest.getQueryWithFileOrderIdRequest().getQueryBatches().getFileOrderId();
		BulkJobMaster bulkJobMaster = bulkJobMasterRepo.findByFileOrderId(fileOrderId);
		
		QueryBatchesResponseType queryBatchesResponseType = new QueryBatchesResponseType();

		for (BulkBatchMaster batchMaster : bulkJobMaster.getBulkBatchMasters()) {
			
			FileOrderBatchType fileOrderBatchType = new FileOrderBatchType();
			fileOrderBatchType.setFileOrderId(bulkJobMaster.getFileOrderId());
			fileOrderBatchType.setBatchId("" + batchMaster.getBatchId());
			fileOrderBatchType.setFileOrderId(bulkJobMaster.getFileOrderId());
			queryBatchesResponseType.getBatch().add(fileOrderBatchType);

		}
		
		BulkOrderEntryResponse response = new BulkOrderEntryResponse();
		QueryWithFileOrderIdResponseType responseType = new QueryWithFileOrderIdResponseType();
		
		if(queryBatchesResponseType.getBatch() != null 
				&& !queryBatchesResponseType.getBatch().isEmpty()) {
			responseType.setQueryBatchesResponse(queryBatchesResponseType);
			responseType.setStatusCode(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
			responseType.setStatusDescription(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
		} else {
			responseType.setStatusCode(BulkOrderConstants.OEP_ORDER_NOT_FOUND_STATUS);
			responseType.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_DATA_NOT_FOUND);
		}
		
		response.setQueryWithFileOrderIdResponse(responseType);
		
		return response;
	}

	@Override
	public BulkOrderEntryResponse queryByBatchId(Exchange exchange, BulkOrderEntryRequest bulkOrderRequest) {		


		QueryWithBatchIdType queryBatches = bulkOrderRequest.getQueryWithBatchIdRequest()
				.getQueryBatches();
		
		QueryWithBatchIdResponseType batchIdResponseType = new QueryWithBatchIdResponseType();
		QueryBatchIdResponseType batchIdRespType = new QueryBatchIdResponseType();
		
		for (BatchIdBatchType batchIdType : queryBatches.getBatch()) {
			
			List<OepOrder> oepOrderList = oepOrderRepo.findByBatchId(Integer.parseInt(batchIdType.getBatchId()));		
			
			BatchType batchType = new BatchType();
			batchType.setBatchId(batchIdType.getBatchId());				
			
			if(!oepOrderList.isEmpty()) {
				for (OepOrder order : oepOrderList) {
					OrderType orderType = new OrderType();
					if(order.getStatusDescription().equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_RECORD)) {
						orderType = new OrderType();
						orderType.setOrderId(order.getOrderId());
						orderType.setStatusCode(order.getStatusCode());
						orderType.setStatusDescription(order.getStatusDescription());
					} else {
						
						orderType = setOrderParameters(order, orderType);
					
					}

					batchType.getItem().add(orderType);
					batchType.setStatusCode(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
					batchType.setStatusDescription(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);	
				}
			} else {
				batchType.setBatchId(batchType.getBatchId());
				batchType.setStatusCode(BulkOrderConstants.OEP_ORDER_NOT_FOUND_STATUS);
				batchType.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_DATA_NOT_FOUND);
			}
			batchIdRespType.getBatch().add(batchType);

		}

		batchIdResponseType.setQueryBatchesResponse(batchIdRespType);
		BulkOrderEntryResponse response = new BulkOrderEntryResponse();
		response.setQueryWithBatchIdResponse(batchIdResponseType);	
		
		return response;
	}

	private OrderType setOrderParameters(OepOrder order, OrderType orderType) {

		if (order.getPayloadXml() != null) {

			Document payloadDoc = (Document) template.requestBody("direct:convertBodyToDOM", order.getPayloadXml());

			Element rootElement = payloadDoc.getDocumentElement();
			orderType = new OrderType();
			orderType.setOrderId(order.getOrderId());
			orderType.setStatusCode(order.getStatusCode());
			orderType.setStatusDescription(order.getStatusDescription());

			if (rootElement != null) {
				log.debug("Inside root element Check ");
				NodeList childNodes = rootElement.getChildNodes();

				int length = rootElement.getChildNodes().getLength();

				for (int i = 0; i < length; i++) {
					Node node = childNodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						setAttributeNameValue(order, orderType, node);

					}
				}
			}

		}
		return orderType;
	}

	private void setAttributeNameValue(OepOrder order, OrderType orderType, Node node) {
		AttributeType attribute = new AttributeType();

		log.debug("Attribute Name --> {}", node.getLocalName());
		log.debug("Attribute value --> {}", node.getTextContent());
		attribute.setName(node.getLocalName());
		if (node.getLocalName().equalsIgnoreCase("msisdn")) {

			if (node.getTextContent() == null || "NA".equalsIgnoreCase(node.getTextContent())
					|| "".equalsIgnoreCase(node.getTextContent()))
				attribute.setValue(order.getServiceNo());
			else
				attribute.setValue(node.getTextContent());
		} else
			attribute.setValue(node.getTextContent());

		orderType.getAttribute().add(attribute);
	}
	
	private FailOrderType setFailedOrderParameters(OepOrder order) {
		
		FailOrderType orderType = new FailOrderType();

		if (order.getPayloadXml() != null) {

			Document payloadDoc = (Document) template.requestBody("direct:convertBodyToDOM", order.getPayloadXml());

			Element rootElement = payloadDoc.getDocumentElement();
			orderType.setOrderId(order.getOrderId());
			orderType.setFailureCode(order.getStatusCode());
			orderType.setFailureReason(order.getStatusDescription());

			if (rootElement != null) {
				log.debug("Inside root element Check ");
				NodeList childNodes = rootElement.getChildNodes();

				int length = rootElement.getChildNodes().getLength();

				for (int i = 0; i < length; i++) {
					Node node = childNodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						setFailedOrderAttributeNameValue(order, orderType, node);

					}
				}
			}

		}
		return orderType;
	}

	private void setFailedOrderAttributeNameValue(OepOrder order, FailOrderType orderType, Node node) {
		AttributeType attribute = new AttributeType();

		log.debug("Attribute Name --> {}", node.getLocalName());
		log.debug("Attribute value --> {}", node.getTextContent());
		attribute.setName(node.getLocalName());
		if (node.getLocalName().equalsIgnoreCase("msisdn")) {

			if (node.getTextContent() == null || "NA".equalsIgnoreCase(node.getTextContent())
					|| "".equalsIgnoreCase(node.getTextContent()))
				attribute.setValue(order.getServiceNo());
			else
				attribute.setValue(node.getTextContent());
		} else
			attribute.setValue(node.getTextContent());

		orderType.getAttribute().add(attribute);
	}

	@Override
	public BulkOrderEntryResponse queryFailedRecords(Exchange exchange, BulkOrderEntryRequest bulkOrderRequest) {

		BulkOrderEntryResponse response = null;
		QueryFailedBatchesType batches = bulkOrderRequest.getQueryFailedRecordsRequest()
				.getQueryFailedBatches();

		List<OepOrder> oepOrderList = null;

		if (batches.getBatchId() != null) {			
			response = getFailedOrderByBatchId(batches);		

		} else if (batches.getFileOrderId() != null) {
			
			response =  getFailedOrdersByFileOrderId(batches);
		}

	
		return response;
	}

	private BulkOrderEntryResponse getFailedOrdersByFileOrderId(QueryFailedBatchesType batches) {

		BulkOrderEntryResponse response = null;

		BulkJobMaster jobMaster = bulkJobMasterRepo.findByFileOrderId(batches.getFileOrderId());

		if (jobMaster != null) {

			QueryFailedBatchesResponseType failedBatchResponseType = new QueryFailedBatchesResponseType();
			for (BulkBatchMaster batchMaster : jobMaster.getBulkBatchMasters()) {

				List<OepOrder> oepOrderList = oepOrderRepo.findByBatchId(new Integer(batchMaster.getBatchId()));

				for (OepOrder order : oepOrderList) {

					if (order.getStatusCode().equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_STATUS)) {
						FailOrderType orderType = new FailOrderType();
						if (order.getStatusDescription().equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_RECORD)) {
							orderType = new FailOrderType();
							orderType.setOrderId(order.getOrderId());
							orderType.setFailureCode(order.getStatusCode());
							orderType.setFailureReason(order.getStatusDescription());
						} else {
							setFailedOrderParameters(order);
						}

						failedBatchResponseType.getItem().add(orderType);
					}
				}

			}
			if (failedBatchResponseType.getItem() != null && !failedBatchResponseType.getItem().isEmpty()) {
				response = setFailedRecordsResponse(failedBatchResponseType,
						BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS, BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);

			}

		} else {
			response = setFailedRecordsResponse(null, BulkOrderConstants.OEP_ORDER_NOT_FOUND_STATUS,
					BulkOrderConstants.OEP_ORDER_MESSAGE_DATA_NOT_FOUND);
		}

		return response;
	}

	private BulkOrderEntryResponse getFailedOrderByBatchId(QueryFailedBatchesType batches) {

		BulkOrderEntryResponse response = null;

		QueryFailedBatchesResponseType failedBatchResponseType = new QueryFailedBatchesResponseType();

		List<OepOrder> oepOrderList = oepOrderRepo.findByBatchId(new Integer(batches.getBatchId()));

		if (!oepOrderList.isEmpty()) {

			for (OepOrder order : oepOrderList) {

				if (order.getStatusCode().equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_STATUS)) {
					FailOrderType orderType = new FailOrderType();
					if (order.getStatusDescription().equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_RECORD)) {
						orderType = new FailOrderType();
						orderType.setOrderId(order.getOrderId());
						orderType.setFailureCode(order.getStatusCode());
						orderType.setFailureReason(order.getStatusDescription());
					} else {
						setFailedOrderParameters(order);
					}
					failedBatchResponseType.getItem().add(orderType);
				}
			}

			if (failedBatchResponseType.getItem() != null && !failedBatchResponseType.getItem().isEmpty()) {
				response = setFailedRecordsResponse(failedBatchResponseType,
						BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS, BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);

			}
		} else {
			response = setFailedRecordsResponse(null, BulkOrderConstants.OEP_ORDER_NOT_FOUND_STATUS,
					BulkOrderConstants.OEP_ORDER_MESSAGE_DATA_NOT_FOUND);

		}

		return response;
	}
	
	
	private BulkOrderEntryResponse setFailedRecordsResponse(QueryFailedBatchesResponseType failedBatchResponseType,String statusCode,String statusDescription) {
		
		BulkOrderEntryResponse response = new BulkOrderEntryResponse();
		QueryFailedRecordsResponseType failedRecsRespType = new QueryFailedRecordsResponseType();
		failedRecsRespType.setQueryFailedBatchesResponse(failedBatchResponseType);
		failedRecsRespType.setStatusCode(statusCode);
		failedRecsRespType.setStatusDescription(statusDescription);
		response.setQueryFailedRecordsResponse(failedRecsRespType);
		
		return response;
	}
	

}
