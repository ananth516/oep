package com.oracle.oep.bulkprocessing.processors;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.oracle.oep.batch.PrePaidStarterKitOrder;
import com.oracle.oep.bulkordermanagement.order.v1.AttributeType;
import com.oracle.oep.bulkordermanagement.order.v1.BatchIdBatchType;
import com.oracle.oep.bulkordermanagement.order.v1.BatchType;
import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderEntryRequest;
import com.oracle.oep.bulkordermanagement.order.v1.BulkOrderEntryResponse;
import com.oracle.oep.bulkordermanagement.order.v1.FailOrderType;
import com.oracle.oep.bulkordermanagement.order.v1.FileOrderBatchType;
import com.oracle.oep.bulkordermanagement.order.v1.OrderType;
import com.oracle.oep.bulkordermanagement.order.v1.ProcessBulkOrderResponseType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryBatchIdResponseType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryBatchesResponseType;
import com.oracle.oep.bulkordermanagement.order.v1.QueryBatchesType;
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

public class BulkOrderEntryWebserviceProcessor implements Processor {

	ProducerTemplate template = null;
	BulkOrderEntryRequest request = null;
	CamelContext camelContext = null;

	public void process(Exchange exchange) throws Exception {

		Message in = exchange.getIn();
		camelContext = exchange.getContext();
		template = camelContext.createProducerTemplate();

		request = in.getBody(BulkOrderEntryRequest.class);

		try {
			if (request.getProcessBulkOrderRequest() != null) {
				template.sendBody("direct:jmsQueueChannel", exchange.getIn().getBody(Document.class));

				BulkOrderEntryResponse response = new BulkOrderEntryResponse();
				ProcessBulkOrderResponseType type = new ProcessBulkOrderResponseType();
				type.setFileOrderId(request.getProcessBulkOrderRequest().getBulkOrderParams().getFileOrderId());
				type.setStatusCode(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
				type.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_SENT);
				response.setProcessBulkOrderResponse(type);
				exchange.getOut().setBody(response);
			}

			if (request.getQueryWithFileOrderIdRequest() != null) {

				QueryBatchesType batches = ((BulkOrderEntryRequest) request).getQueryWithFileOrderIdRequest()
						.getQueryBatches();
				Map<String, String> jobMap = new HashMap<String, String>();

				jobMap.put("param", batches.getFileOrderId());

				Vector<BulkJobMaster> jobMasterList = (Vector) template.requestBody("direct:queryBulkJobMaster",
						jobMap);

				QueryBatchesResponseType queryBatchesResponseType = new QueryBatchesResponseType();

				for (BulkJobMaster jobMaster : jobMasterList) {

					Map<String, Long> batchMap = new HashMap<String, Long>();

					batchMap.put("param", jobMaster.getJobId());

					Vector<BulkBatchMaster> batchMasterList = (Vector) template
							.requestBody("direct:queryBulkBatchMasterByJobId", batchMap);

					for (BulkBatchMaster batchMaster : batchMasterList) {
						FileOrderBatchType fileOrderBatchType = new FileOrderBatchType();
						fileOrderBatchType.setFileOrderId(jobMaster.getFileOrderId());
						fileOrderBatchType.setBatchId("" + batchMaster.getBatchId());
						fileOrderBatchType.setFileOrderId(jobMaster.getFileOrderId());
						queryBatchesResponseType.getBatch().add(fileOrderBatchType);

					}

				}

				BulkOrderEntryResponse response = new BulkOrderEntryResponse();
				QueryWithFileOrderIdResponseType responseType = new QueryWithFileOrderIdResponseType();
				if(queryBatchesResponseType.getBatch() != null 
						&& queryBatchesResponseType.getBatch().size() > 0) {
					responseType.setQueryBatchesResponse(queryBatchesResponseType);
					responseType.setStatusCode(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
					responseType.setStatusDescription(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
				} else {
					responseType.setStatusCode(BulkOrderConstants.OEP_ORDER_NOT_FOUND_STATUS);
					responseType.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_DATA_NOT_FOUND);
				}
				response.setQueryWithFileOrderIdResponse(responseType);
				exchange.getOut().setBody(response);
			}

			if (request.getQueryWithBatchIdRequest() != null) {

				QueryWithBatchIdType queryBatches = ((BulkOrderEntryRequest) request).getQueryWithBatchIdRequest()
						.getQueryBatches();
				Vector<OepOrder> orderList;
				
				QueryWithBatchIdResponseType batchIdResponseType = new QueryWithBatchIdResponseType();
				QueryBatchIdResponseType batchIdRespType = new QueryBatchIdResponseType();
				for (BatchIdBatchType batchIdType : queryBatches.getBatch()) {
					
					
					Map<String, Integer> batchMap = new HashMap<String, Integer>();
					batchMap.put("param", new Integer(batchIdType.getBatchId()));
					orderList = (Vector) template.requestBody("direct:queryOrdersByBatchId", batchMap);
					
					
					BatchType batchType = new BatchType();
					batchType.setBatchId(batchIdType.getBatchId());
					
					
					JAXBContext jaxbContext = JAXBContext.newInstance(PrePaidStarterKitOrder.class);
					if(orderList.size() > 0) {
						for (OepOrder order : orderList) {
							OrderType orderType = new OrderType();
							if(order.getStatusDescription().equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_RECORD)) {
								orderType = new OrderType();
								orderType.setOrderId(order.getOrderId());
								orderType.setStatusCode(order.getStatusCode());
								orderType.setStatusDescription(order.getStatusDescription());
							} else {
								
								Document payloadDoc = (Document) template.requestBody("direct:convertBodyToDOM",order.getPayloadXml());
								
								System.out.println("After converting to Document ");
								
								if(payloadDoc!=null){
									System.out.println("Inside Payload Doc Check ");
									
									Element rootElement = payloadDoc.getDocumentElement(); 
									orderType = new OrderType();
									orderType.setOrderId(order.getOrderId());
									orderType.setStatusCode(order.getStatusCode());
									orderType.setStatusDescription(order.getStatusDescription());									
									
									if(rootElement!=null){
										System.out.println("Inside root element Check ");
										NodeList childNodes = rootElement.getChildNodes();
										
										int length = rootElement.getChildNodes().getLength();
										
										for(int i=0;i<length;i++){
											Node node = childNodes.item(i);
											if(node.getNodeType()==Node.ELEMENT_NODE){
												AttributeType attribute = new AttributeType();
												
												System.out.println(node.getTextContent());
												System.out.println(node.getLocalName());
												
												if(node.getLocalName().equals(BulkOrderConstants.ADDONS)){
													
													NodeList addons = node.getChildNodes();
													if(addons!=null){														
														
														String addonList = null;
														for(int j=0; j<addons.getLength();j++){
															
															if(addons.item(j).getNodeType()==Node.ELEMENT_NODE){
																if(addonList==null)
																	addonList = addons.item(j).getTextContent();
																else
																	addonList = addonList+":"+addons.item(j).getTextContent();
															}
														}
														
														attribute.setName(BulkOrderConstants.ADDONS);
														attribute.setValue(addonList);
														
													}
												}
												
												else {
													
													System.out.println("Attribute Name --> "+node.getLocalName());
													System.out.println("Attribute value --> "+node.getTextContent());
													attribute.setName(node.getLocalName());
													if(node.getLocalName().equalsIgnoreCase("msisdn"))
														
														if(node.getTextContent()==null
														||"NA".equalsIgnoreCase(node.getTextContent())
														||"".equalsIgnoreCase(node.getTextContent()))
															attribute.setValue(order.getServiceNo());
														else
															attribute.setValue(node.getTextContent());
													
													else
														attribute.setValue(node.getTextContent());
												}
												
												orderType.getAttribute().add(attribute);
											
											}
										}
									}
								
								}
								/*Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
								PrePaidStarterKitOrder pskItem = (PrePaidStarterKitOrder) jaxbUnmarshaller
										.unmarshal(new StreamSource(new StringReader(order.getPayloadXml())));
								orderType = new OrderType();
								orderType.setOrderId(order.getOrderId());
								orderType.setStatusCode(order.getStatusCode());
								orderType.setStatusDescription(order.getStatusDescription());
		
								AttributeType imsiAttrType = new AttributeType();
								imsiAttrType.setName(BulkOrderConstants.IMSI);
								imsiAttrType.setValue(pskItem.getImsi());
								orderType.getAttribute().add(imsiAttrType);
		
								AttributeType msisdnAttrType = new AttributeType();
								msisdnAttrType.setName(BulkOrderConstants.MSISDN);
								msisdnAttrType.setValue(pskItem.getMsisdn());
								orderType.getAttribute().add(msisdnAttrType);
		
								AttributeType puk1AttrType = new AttributeType();
								puk1AttrType.setName(BulkOrderConstants.PUK1);
								puk1AttrType.setValue(pskItem.getPuk1());
								orderType.getAttribute().add(puk1AttrType);
		
								AttributeType puk2AttrType = new AttributeType();
								puk2AttrType.setName(BulkOrderConstants.PUK2);
								puk2AttrType.setValue(pskItem.getPuk2());
								orderType.getAttribute().add(puk2AttrType);
		
								AttributeType pin1AttrType = new AttributeType();
								pin1AttrType.setName(BulkOrderConstants.PIN1);
								pin1AttrType.setValue(pskItem.getPin1());
								orderType.getAttribute().add(pin1AttrType);
		
								AttributeType pin2AttrType = new AttributeType();
								pin2AttrType.setName(BulkOrderConstants.PIN2);
								pin2AttrType.setValue(pskItem.getPin2());
								orderType.getAttribute().add(pin2AttrType);
		
								AttributeType iccidAttrType = new AttributeType();
								iccidAttrType.setName(BulkOrderConstants.ICCID);
								iccidAttrType.setValue(pskItem.getIccid());
								orderType.getAttribute().add(iccidAttrType);
		
								AttributeType admAttrType = new AttributeType();
								admAttrType.setName(BulkOrderConstants.ADM);
								admAttrType.setValue(pskItem.getAdm());
								orderType.getAttribute().add(admAttrType);
		
								AttributeType kiAttrType = new AttributeType();
								kiAttrType.setName(BulkOrderConstants.KI);
								kiAttrType.setValue(pskItem.getKi());
								orderType.getAttribute().add(kiAttrType);
								
								AttributeType basePlanAttrType = new AttributeType();
								basePlanAttrType.setName(BulkOrderConstants.BASE_PLAN);
								basePlanAttrType.setValue(pskItem.getBasePlan());
								orderType.getAttribute().add(basePlanAttrType);
		
								AttributeType addOnsAttrType = new AttributeType();
								addOnsAttrType.setName(BulkOrderConstants.ADDONS);
								addOnsAttrType.setValue(pskItem.getAddOnList());
								orderType.getAttribute().add(addOnsAttrType);*/
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
				exchange.getOut().setBody(response);
			}

			if (request.getQueryFailedRecordsRequest() != null) {
				processFailedRecordsRequest(request, exchange);
			}

		} catch (Exception e) {

			e.printStackTrace();
			BulkOrderEntryResponse response = new BulkOrderEntryResponse();
			ProcessBulkOrderResponseType type = new ProcessBulkOrderResponseType();
			if (request.getProcessBulkOrderRequest() != null) {
				type.setFileOrderId(request.getProcessBulkOrderRequest().getBulkOrderParams().getFileOrderId());
			}
			type.setStatusCode(BulkOrderConstants.OEP_ORDER_FAILED_STATUS);
			type.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_SENDING_FAILED + "-->" + e.getMessage());
			response.setProcessBulkOrderResponse(type);
			exchange.getOut().setBody(response);
		}

	}

	private void processFailedRecordsRequest(BulkOrderEntryRequest request, Exchange exchange) {
		// TODO Auto-generated method stub

		QueryFailedBatchesType batches = ((BulkOrderEntryRequest) request).getQueryFailedRecordsRequest()
				.getQueryFailedBatches();

		Vector<OepOrder> orderList = null;

		if (batches.getBatchId() != null) {


			QueryFailedBatchesResponseType failedBatchResponseType = new QueryFailedBatchesResponseType();
			Map<String, Integer> batchMasterMap = new HashMap<String, Integer>();

			batchMasterMap.put("param", new Integer(batches.getBatchId()));
			orderList = (Vector) template.requestBody("direct:queryOrdersByBatchId", batchMasterMap);


			if (orderList.size() > 0) {
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(PrePaidStarterKitOrder.class);
					for (OepOrder order : orderList) {

						if (order.getStatusCode().equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_STATUS)) {
							FailOrderType orderType = new FailOrderType();
							Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
							if (order.getStatusDescription()
									.equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_RECORD)) {
								orderType = new FailOrderType();
								orderType.setOrderId(order.getOrderId());
								orderType.setFailureCode(order.getStatusCode());
								orderType.setFailureReason(order.getStatusDescription());
							} else {
								
								Document payloadDoc = (Document) template.requestBody("direct:convertBodyToDOM",order.getPayloadXml());
								
								System.out.println("After converting to Document ");
								
								if(payloadDoc!=null){
									System.out.println("Inside Payload Doc Check ");
									
									Element rootElement = payloadDoc.getDocumentElement(); 
									orderType = new FailOrderType();
									orderType.setOrderId(order.getOrderId());
									orderType.setFailureCode(order.getStatusCode());
									orderType.setFailureCode(order.getStatusDescription());									
									
									if(rootElement!=null){
										System.out.println("Inside root element Check ");
										NodeList childNodes = rootElement.getChildNodes();
										
										int length = rootElement.getChildNodes().getLength();
										
										for(int i=0;i<length;i++){
											Node node = childNodes.item(i);
											if(node.getNodeType()==Node.ELEMENT_NODE){
												AttributeType attribute = new AttributeType();
												
												System.out.println(node.getTextContent());
												System.out.println(node.getLocalName());
												
												if(node.getLocalName().equals(BulkOrderConstants.ADDONS)){
													
													NodeList addons = node.getChildNodes();
													if(addons!=null){														
														
														String addonList = null;
														for(int j=0; j<addons.getLength();j++){
															
															if(addons.item(j).getNodeType()==Node.ELEMENT_NODE){
																if(addonList==null)
																	addonList = addons.item(j).getTextContent();
																else
																	addonList = addonList+":"+addons.item(j).getTextContent();
															}
														}
														
														attribute.setName(BulkOrderConstants.ADDONS);
														attribute.setValue(addonList);
														
													}
												}
												
												else {
													
													System.out.println("Attribute Name --> "+node.getLocalName());
													System.out.println("Attribute value --> "+node.getTextContent());
													attribute.setName(node.getLocalName());
													
													if(node.getLocalName().equalsIgnoreCase("msisdn"))
														attribute.setValue(order.getServiceNo());
													
													else
														attribute.setValue(node.getTextContent());
												}
												
												orderType.getAttribute().add(attribute);
											
											}
										}
									}
								
								}
								
								/*PrePaidStarterKitOrder pskItem = (PrePaidStarterKitOrder) jaxbUnmarshaller
										.unmarshal(new StreamSource(new StringReader(order.getPayloadXml())));
								orderType = new FailOrderType();
								orderType.setOrderId(order.getOrderId());
								orderType.setFailureCode(order.getStatusCode());
								orderType.setFailureReason(order.getStatusDescription());
								AttributeType imsiAttrType = new AttributeType();
								imsiAttrType.setName(BulkOrderConstants.IMSI);
								imsiAttrType.setValue(pskItem.getImsi());
								orderType.getAttribute().add(imsiAttrType);
								AttributeType msisdnAttrType = new AttributeType();
								msisdnAttrType.setName(BulkOrderConstants.MSISDN);
								msisdnAttrType.setValue(pskItem.getMsisdn());
								orderType.getAttribute().add(msisdnAttrType);
								AttributeType puk1AttrType = new AttributeType();
								puk1AttrType.setName(BulkOrderConstants.PUK1);
								puk1AttrType.setValue(pskItem.getPuk1());
								orderType.getAttribute().add(puk1AttrType);
								AttributeType puk2AttrType = new AttributeType();
								puk2AttrType.setName(BulkOrderConstants.PUK2);
								puk2AttrType.setValue(pskItem.getPuk2());
								orderType.getAttribute().add(puk2AttrType);
								AttributeType pin1AttrType = new AttributeType();
								pin1AttrType.setName(BulkOrderConstants.PIN1);
								pin1AttrType.setValue(pskItem.getPin1());
								orderType.getAttribute().add(pin1AttrType);
								AttributeType pin2AttrType = new AttributeType();
								pin2AttrType.setName(BulkOrderConstants.PIN2);
								pin2AttrType.setValue(pskItem.getPin2());
								orderType.getAttribute().add(pin2AttrType);
								AttributeType iccidAttrType = new AttributeType();
								iccidAttrType.setName(BulkOrderConstants.ICCID);
								iccidAttrType.setValue(pskItem.getIccid());
								orderType.getAttribute().add(iccidAttrType);
								AttributeType admAttrType = new AttributeType();
								admAttrType.setName(BulkOrderConstants.ADM);
								admAttrType.setValue(pskItem.getAdm());
								orderType.getAttribute().add(admAttrType);
								AttributeType kiAttrType = new AttributeType();
								kiAttrType.setName(BulkOrderConstants.KI);
								kiAttrType.setValue(pskItem.getKi());
								orderType.getAttribute().add(kiAttrType);
								AttributeType basePlanAttrType = new AttributeType();
								basePlanAttrType.setName(BulkOrderConstants.BASE_PLAN);
								basePlanAttrType.setValue(pskItem.getBasePlan());
								orderType.getAttribute().add(basePlanAttrType);
								AttributeType addOnsAttrType = new AttributeType();
								addOnsAttrType.setName(BulkOrderConstants.ADDONS);
								addOnsAttrType.setValue(pskItem.getAddOnList());
								orderType.getAttribute().add(addOnsAttrType);*/
							}
							failedBatchResponseType.getItem().add(orderType);
						}
					}

				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				BulkOrderEntryResponse response = new BulkOrderEntryResponse();
				QueryFailedRecordsResponseType failedRecsRespType = new QueryFailedRecordsResponseType();
				if (failedBatchResponseType.getItem() != null && failedBatchResponseType.getItem().size() > 0) {
					failedRecsRespType.setQueryFailedBatchesResponse(failedBatchResponseType);
					failedRecsRespType.setStatusCode(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
					failedRecsRespType.setStatusDescription(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
				} else {
					failedRecsRespType.setStatusCode(BulkOrderConstants.OEP_ORDER_NOT_FOUND_STATUS);
					failedRecsRespType.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_DATA_NOT_FOUND);
				}
				response.setQueryFailedRecordsResponse(failedRecsRespType);
				exchange.getOut().setBody(response);
			} else {
				BulkOrderEntryResponse response = new BulkOrderEntryResponse();
				QueryFailedRecordsResponseType failedRecsRespType = new QueryFailedRecordsResponseType();
				failedRecsRespType.setStatusCode(BulkOrderConstants.OEP_ORDER_NOT_FOUND_STATUS);
				failedRecsRespType.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_DATA_NOT_FOUND);
				response.setQueryFailedRecordsResponse(failedRecsRespType);
				exchange.getOut().setBody(response);
			}
		

		} else if (batches.getFileOrderId() != null) {


			Map<String, String> jobMap = new HashMap<String, String>();

			jobMap.put("param", batches.getFileOrderId());

			Vector<BulkJobMaster> jobMasterList = (Vector) template.requestBody("direct:queryBulkJobMaster", jobMap);

			if(jobMasterList.size() > 0) {
				for (BulkJobMaster jobMaster : jobMasterList) {
	
					Map<String, Long> batchMap = new HashMap<>();
	
					batchMap.put("param", jobMaster.getJobId());
	
					Vector<BulkBatchMaster> batchMasterList = (Vector) template
							.requestBody("direct:queryBulkBatchMasterByJobId", batchMap);
	
					QueryFailedBatchesResponseType failedBatchResponseType = new QueryFailedBatchesResponseType();
					for (BulkBatchMaster batchMaster : batchMasterList) {
	
						Map<String, Integer> batchMasterMap = new HashMap<String, Integer>();
	
						batchMasterMap.put("param", new Integer(batchMaster.getBatchId()));
						orderList = (Vector) template.requestBody("direct:queryOrdersByBatchId", batchMasterMap);
	
	
						try {
							JAXBContext jaxbContext = JAXBContext.newInstance(PrePaidStarterKitOrder.class);
							for (OepOrder order : orderList) {
								
								if (order.getStatusCode().equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_STATUS)) {
									FailOrderType orderType = new FailOrderType();
									Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
									if(order.getStatusDescription().equalsIgnoreCase(BulkOrderConstants.OEP_ORDER_FAILED_RECORD)) {
										orderType = new FailOrderType();
										orderType.setOrderId(order.getOrderId());
										orderType.setFailureCode(order.getStatusCode());
										orderType.setFailureReason(order.getPayloadXml());
									} else {
										PrePaidStarterKitOrder pskItem = (PrePaidStarterKitOrder) jaxbUnmarshaller
												.unmarshal(new StreamSource(new StringReader(order.getPayloadXml())));
										orderType = new FailOrderType();
										orderType.setOrderId(order.getOrderId());
										orderType.setFailureCode(order.getStatusCode());
										orderType.setFailureReason(order.getStatusDescription());
										AttributeType imsiAttrType = new AttributeType();
										imsiAttrType.setName(BulkOrderConstants.IMSI);
										imsiAttrType.setValue(pskItem.getImsi());
										orderType.getAttribute().add(imsiAttrType);
										AttributeType msisdnAttrType = new AttributeType();
										msisdnAttrType.setName(BulkOrderConstants.MSISDN);
										msisdnAttrType.setValue(pskItem.getMsisdn());
										orderType.getAttribute().add(msisdnAttrType);
										AttributeType puk1AttrType = new AttributeType();
										puk1AttrType.setName(BulkOrderConstants.PUK1);
										puk1AttrType.setValue(pskItem.getPuk1());
										orderType.getAttribute().add(puk1AttrType);
										AttributeType puk2AttrType = new AttributeType();
										puk2AttrType.setName(BulkOrderConstants.PUK2);
										puk2AttrType.setValue(pskItem.getPuk2());
										orderType.getAttribute().add(puk2AttrType);
										AttributeType pin1AttrType = new AttributeType();
										pin1AttrType.setName(BulkOrderConstants.PIN1);
										pin1AttrType.setValue(pskItem.getPin1());
										orderType.getAttribute().add(pin1AttrType);
										AttributeType pin2AttrType = new AttributeType();
										pin2AttrType.setName(BulkOrderConstants.PIN2);
										pin2AttrType.setValue(pskItem.getPin2());
										orderType.getAttribute().add(pin2AttrType);
										AttributeType iccidAttrType = new AttributeType();
										iccidAttrType.setName(BulkOrderConstants.ICCID);
										iccidAttrType.setValue(pskItem.getIccid());
										orderType.getAttribute().add(iccidAttrType);
										AttributeType admAttrType = new AttributeType();
										admAttrType.setName(BulkOrderConstants.ADM);
										admAttrType.setValue(pskItem.getAdm());
										orderType.getAttribute().add(admAttrType);
										AttributeType kiAttrType = new AttributeType();
										kiAttrType.setName(BulkOrderConstants.KI);
										kiAttrType.setValue(pskItem.getKi());
										orderType.getAttribute().add(kiAttrType);
										AttributeType basePlanAttrType = new AttributeType();
										basePlanAttrType.setName(BulkOrderConstants.BASE_PLAN);
										basePlanAttrType.setValue(pskItem.getBasePlan());
										orderType.getAttribute().add(basePlanAttrType);
										AttributeType addOnsAttrType = new AttributeType();
										addOnsAttrType.setName(BulkOrderConstants.ADDONS);
										addOnsAttrType.setValue(pskItem.getAddOnList());
										orderType.getAttribute().add(addOnsAttrType);
									}
									
									failedBatchResponseType.getItem().add(orderType);
								}
							}
	
						} catch (JAXBException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
	
					}
					BulkOrderEntryResponse response = new BulkOrderEntryResponse();
					QueryFailedRecordsResponseType failedRecsRespType = new QueryFailedRecordsResponseType();
					if(failedBatchResponseType.getItem() != null
							&& failedBatchResponseType.getItem().size() > 0) {
						failedRecsRespType.setQueryFailedBatchesResponse(failedBatchResponseType);
						failedRecsRespType.setStatusCode(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
						failedRecsRespType.setStatusDescription(BulkOrderConstants.OEP_ORDER_SUCCESS_STATUS);
					} else {
						failedRecsRespType.setStatusCode(BulkOrderConstants.OEP_ORDER_NOT_FOUND_STATUS);
						failedRecsRespType.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_DATA_NOT_FOUND);
					}
					response.setQueryFailedRecordsResponse(failedRecsRespType);
					exchange.getOut().setBody(response);
				}
			} else {
				BulkOrderEntryResponse response = new BulkOrderEntryResponse();
				QueryFailedRecordsResponseType failedRecsRespType = new QueryFailedRecordsResponseType();
				failedRecsRespType.setStatusCode(BulkOrderConstants.OEP_ORDER_NOT_FOUND_STATUS);
				failedRecsRespType.setStatusDescription(BulkOrderConstants.OEP_ORDER_MESSAGE_DATA_NOT_FOUND);
				response.setQueryFailedRecordsResponse(failedRecsRespType);
				exchange.getOut().setBody(response);
			}
		}

	}

}
