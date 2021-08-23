package com.oracle.oep.order.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.plaf.metal.MetalBorders.PaletteBorder;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.oracle.oep.brm.opcode.root.Opcode;
import com.oracle.oep.brm.opcodes.CUSFLDADDONPKG;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.PARAMS;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.ebm.transform.ProcessBarringOrderEbmHelper;
import com.oracle.oep.order.ebm.transform.ProcessBarringOrderEbmMapping;
import com.oracle.oep.order.model.BarringServicesType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ServiceTypeEnum;
import com.oracle.oep.order.presistence.model.BulkBatchMaster;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.CreateOrder;

public class ProcessBarringOrderFulfillmentProcessor {

	public static final Logger log = LogManager.getLogger(ProcessBarringOrderFulfillmentProcessor.class);

	OepOrder oepOrderDO = null;
	ProducerTemplate template = null;

	/*
	 * 
	 * This method processes the add order for all services incliding Add
	 * Mobile, BB, Short code and Bulk SMS
	 * 
	 */
	public void processBarringOrders(final OrderEntryRequest request, final Exchange exchange,
			final String oepOrderAction, final String orderPayload, final String oepOrderNumber) throws Exception {

		template = exchange.getContext().createProducerTemplate();
		log.info("OEP order number : " + oepOrderNumber + " with action " + oepOrderAction + " started processing");

		try {

			List<BarringServicesType> orderedPlans = null;
			List<BarringServicesType> basePlans = null;
			String baseBRMPlan = null;
			Map<String, OEPConfigPlanAttributesT> productSpecMap = new HashMap<String, OEPConfigPlanAttributesT>();
			CUSOPSEARCHOutputFlist searchResult = null;

			String serviceIdentifier = null;
			String serviceNo = null;
			String oepServiceType = null;
			String channelName = ProcessBarringOrderEbmHelper.getChannelName(request, oepOrderAction);

			String userName = ProcessBarringOrderEbmHelper.getCsrid(request, oepOrderAction);

		
			String serviceId = null;
			String billingAccountId = null;
			String customerAccountId = null;
			String accountId = null;
			Map<Integer, String> serviceNoMap = new HashMap<Integer, String>();
			Map<Integer, String> serviceIdMap = new HashMap<Integer, String>();
			Map<Integer, String> barringTypeMap = new HashMap<Integer, String>();
			
			log.info("OEP order number : " + oepOrderNumber + ". Before Fetching plans from order");

			orderedPlans = ProcessBarringOrderEbmHelper.getPlansFromOrder(request, oepOrderAction);

			log.info("OEP order number : " + oepOrderNumber + ". Fetched plans from order");

			String planType = null;
			int index = 0;

		
			for (BarringServicesType plan : orderedPlans) {
				

				serviceNo = ProcessBarringOrderEbmHelper.getServiceNo(plan, oepOrderAction);
				serviceIdentifier = ProcessBarringOrderEbmHelper.getServiceIdentifier(plan, oepOrderAction);
				oepServiceType = ProcessBarringOrderEbmHelper.getServiceType(plan, channelName);

				if (channelName.equals("OAP")) {

					log.info("Reqesut received from OAP" + oepOrderNumber + "  " + oepServiceType );

					searchResult = getAccountDataFromBRM(template, serviceNo, serviceIdentifier, channelName, userName,
							oepOrderNumber, oepServiceType);

					RESULTS result = getActiveServiceFromSearchResult(searchResult);

					if (result == null) {

						throw new RuntimeException("No Active Service is Found in BRM");
					}
					log.info("Response from BRM : "
							+ (String) template.requestBody("direct:convertxmlToString", result));
					
					if (oepServiceType.equalsIgnoreCase(OEPConstants.OEP_BULK_SMS)
							|| oepServiceType.equalsIgnoreCase(OEPConstants.OEP_SC))					
					{
						log.info("inside If service type");
//						baseBRMPlan = result.getPLAN().getNAME();
						
						Iterator<RESULTS.PLAN> incompatibleAddonIterator= result.getPLAN().iterator();
						
						while (incompatibleAddonIterator.hasNext()) {
							RESULTS.PLAN allplanList = (RESULTS.PLAN)incompatibleAddonIterator.next();
							Byte planType1 = allplanList.getCUSFLDPLANTYPE();
							if(planType1.equals((byte)1))
							{
								baseBRMPlan = allplanList.getNAME();
							}
						}
						
						serviceId = serviceIdentifier;
					}
					
					else
					{
						log.info("inside else service type");
						baseBRMPlan = result.getCUSFLDPLANNAME();
						serviceId = result.getLOGIN();

					}
					
				
					accountId = result.getACCOUNTNO();
					billingAccountId = result.getCUSFLDBAACCOUNTNO();
					customerAccountId = result.getCUSFLDCAACCOUNTNO();
					BarringServicesType servicesPlan = new BarringServicesType();
					

					OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(baseBRMPlan, template,
							oepOrderNumber);

					if (planAttrbiutes == null) {

						throw new RuntimeException("PS is not found");
					}
					planType = planAttrbiutes.getPackageType();
					log.info("OEP order number : " + oepOrderNumber + ". Fetched package type is " + planType
							+ ", product spec is " + planAttrbiutes.getPs() + ", and COS is " + planAttrbiutes.getCos()
							+ "plan Attribute" + (String) template.requestBody("direct:convertxmlToString", result));
				
					if (basePlans == null) {

						basePlans = new ArrayList<BarringServicesType>();

					}
					
					servicesPlan.setPlanName(baseBRMPlan);
					servicesPlan.setServiceType(planAttrbiutes.getServiceType());
					servicesPlan.setAction(plan.getAction());
					servicesPlan.setServiceIdentifier(serviceIdentifier);
					servicesPlan.setServiceNo(serviceNo);

					basePlans.add(servicesPlan);


					productSpecMap.put(baseBRMPlan, planAttrbiutes);
					serviceNoMap.put(index, serviceNo);
					serviceIdMap.put(index, serviceId);
					barringTypeMap.put(index, plan.getBarringType());
				} else {
					log.info("Request Received from BRM");
					planType = null;

					// queryplan and initialize base plan and addOnPlan

					OEPConfigPlanAttributesT planAttrbiutes = queryBRMForPlanAttributes(plan.getPlanName(), template,
							oepOrderNumber);

					if (planAttrbiutes == null) {

						throw new RuntimeException("PS is not found");
					}
					planType = planAttrbiutes.getPackageType();
					log.info("OEP order number : " + oepOrderNumber + ". Fetched package type is " + planType
							+ ", product spec is " + planAttrbiutes.getPs() + ", and COS is "
							+ planAttrbiutes.getCos());

					if (planType.equals(OEPConstants.OEP_BASE_PLAN_TYPE)) {
						if (basePlans == null) {

							basePlans = new ArrayList<BarringServicesType>();

						}
						
						basePlans.add(plan);

					}

					productSpecMap.put(plan.getPlanName(), planAttrbiutes);
					serviceNoMap.put(index, serviceNo);
					serviceIdMap.put(index, serviceIdentifier);
					barringTypeMap.put(index, plan.getBarringType());
				}
				index++;
			}

			// generate Order and OrderLine DataObject's
			oepOrderDO = getOepOrderDO(request, oepOrderAction, oepOrderNumber, orderPayload, basePlans, productSpecMap,
					channelName, baseBRMPlan, serviceNo, oepServiceType);
			
			oepOrderDO.setBaAccountNo(billingAccountId);

			ProcessBarringOrderEbmMapping addOrderEbmMapping = new ProcessBarringOrderEbmMapping();
			CreateOrder salesOrderEbm = addOrderEbmMapping.createOSMOrderEBM(request, oepOrderDO, oepOrderAction,
					oepOrderNumber, productSpecMap, searchResult,serviceNoMap, serviceIdMap, barringTypeMap);

			log.info("OEP Order Number : " + oepOrderNumber + ".Generated OepOrder Entity Bean. Generating EBM");

			// Save Order Entiry to DB
			if(salesOrderEbm!=null){
				
				oepOrderDO.setOsmPayloadXml(template.requestBody("direct:convertToString", salesOrderEbm).toString());

			}
			saveOepOrderDO(oepOrderDO, template, oepOrderNumber);

			log.info("OEP Order Number : " + oepOrderNumber + ".OepOrder Entity Bean is persisted");

			// send sales order to OSM queue

			
			sendSalesOrderEBM(salesOrderEbm, template, oepOrderNumber,request);
			log.info("OEP Order Number : " + oepOrderNumber + ".Order sent to OSM queue"
					+ (String) template.requestBody("direct:convertxmlToString", salesOrderEbm));

		} catch (Exception e) {

			e.printStackTrace();
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			errorMessage = errorMessage!=null?(errorMessage.length()>200?errorMessage.substring(0, 200):errorMessage) : null;
			log.error("OEP Order Number : " + oepOrderNumber + ". Exception caught while processing the order.. "
					+ errorMessage);			

			GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();
			oepOrderDO = entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);

			oepOrderDO.setStatusCode(OEPConstants.OEP_ORDER_STATUS_FAILED);
			oepOrderDO.setStatusDescription(errorMessage);

			saveOepOrderDO(oepOrderDO, template, oepOrderNumber);

			throw e;
		}

	}

	/*
	 * 
	 * This method generate OepOrder entity bean and OepOrderLine entity bean
	 * associate OepOrderLine with OepOrder
	 * 
	 */
	private OepOrder getOepOrderDO(final OrderEntryRequest request, final String oepOrderAction,
			final String oepOrderNumber, final String orderPayload, List<BarringServicesType> basePlans,
			final Map<String, OEPConfigPlanAttributesT> productSpecMap, final String channelName,
			final String brmBasePlan, final String serviceNo, final String oepServiceType) {

		log.info("OEP Order Number : " + oepOrderNumber
				+ ".Started Initializing Oep Order Entity Bean .Order payload is " + orderPayload);

		GenerateOrderEntityBean entityBeanGenerator = new GenerateOrderEntityBean();

		String csrId = ProcessBarringOrderEbmHelper.getCsrid(request, oepOrderAction);
		Timestamp orderStartDate = new Timestamp(System.currentTimeMillis());
		String accountId = ProcessBarringOrderEbmHelper.getAccountId(request, oepOrderAction);
		
		log.info("OEP Order Number : " + oepOrderNumber + ". Before calling entityBeanGenerator.getOepOrderDO()");

		OepOrder oepOrderDO = entityBeanGenerator.queryOepOrderDO(oepOrderNumber, template);

		entityBeanGenerator.getOepOrderDO(oepOrderDO, serviceNo, csrId, orderStartDate, oepOrderAction, oepOrderNumber,
				orderPayload, null, channelName);

		List<OepOrderLine> oepOrderLines = new ArrayList<OepOrderLine>();
		if (basePlans != null) {

			for (BarringServicesType basePlan : basePlans) {

				log.info("OEP Order Number : " + oepOrderNumber + ".Adding base plan," + oepOrderAction
						+ " to  Order Entity Bean ");

				if (channelName.equals("OAP")) {

					OepOrderLine addOnOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction,
							basePlan.getPlanName(), productSpecMap.get(basePlan.getPlanName()).getPs(),
							ProcessBarringOrderEbmHelper.getPlanAction(basePlan, basePlan.getPlanName(), channelName),
							accountId, basePlan.getServiceType(), null,
							oepOrderNumber);
					

					addOnOrderLine.setOepOrder(oepOrderDO);

					oepOrderLines.add(addOnOrderLine);
					

				} else {
					OepOrderLine addOnOrderLine = entityBeanGenerator.getOepOrderLineDO(oepOrderAction,
							basePlan.getPlanName(), productSpecMap.get(basePlan.getPlanName()).getPs(),
							ProcessBarringOrderEbmHelper.getPlanAction(basePlan, basePlan.getPlanName(), channelName),
							accountId, productSpecMap.get(basePlan.getPlanName()).getServiceType(), null, oepOrderNumber);
					

					addOnOrderLine.setOepOrder(oepOrderDO);

					oepOrderLines.add(addOnOrderLine);

				}

			}
		}
		oepOrderDO.setOepOrderLines(oepOrderLines);

		log.info("OEP Order Number : " + oepOrderNumber + ".Oep Order Entity Bean is set");

		return oepOrderDO;

	}

	/*
	 * 
	 * This is the helper method to query BRM for config plan attributes for
	 * given system plan name.
	 * 
	 */
	private OEPConfigPlanAttributesT queryBRMForPlanAttributes(final String planName, final ProducerTemplate template,
			final String oepOrderNumber) {

		log.info("OEP Order Number : " + oepOrderNumber + ".Querying plan attributes from BRM DB for " + planName);

		Map<String, String> planMap = new HashMap<String, String>();

		planMap.put("param", planName);

		Vector<OEPConfigPlanAttributesT> planAttributesList = (Vector) template.requestBody("direct:queryPlanFromBRM",
				planMap);

		Iterator<OEPConfigPlanAttributesT> iterator = planAttributesList.listIterator();

		OEPConfigPlanAttributesT planAttributeObject = null;

		while (iterator.hasNext()) {

			planAttributeObject = iterator.next();

		}

		log.info("OEP Order Number : " + oepOrderNumber + ". Returning for Querying plan attributes");
		return planAttributeObject;

	}

	/*
	 * 
	 * This is the helper method to persist OepOrder enity bean to OEP DB
	 * 
	 */

	private void saveOepOrderDO(final OepOrder oepOrder, final ProducerTemplate template, final String oepOrderNumber) {

		template.sendBody("direct:saveOrderToRespository", oepOrder);

	}

	/*
	 * 
	 * This is the helper method to send process order ebm to OSM sales order
	 * queue
	 * 
	 */
	private void sendSalesOrderEBM(final CreateOrder salesOrder, final ProducerTemplate template,
			final String oepOrderNumber,OrderEntryRequest request) throws SOAPException {

		Document soapBodyDoc = (Document) template.requestBody("direct:convertToDocument", salesOrder);
		SOAPMessage soapMessage = OrderDataUtils.createSOAPRequest(soapBodyDoc, oepOrderNumber, template);

		Map<String, Object> orderHeaders = new HashMap<String, Object>();
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_URI, OEPConstants.OSM_WEBSERVICE_URI);
		orderHeaders.put(OEPConstants.OSM_ORDER_HEADER_WLS_CONTENT_TYPE, OEPConstants.OSM_WEBSERVICE_CONTETNT_TYPE);
		
		if(request.getProcessCollectionBarring() !=null && request.getProcessCollectionBarring().getBarringAction() != null 
				&& request.getProcessCollectionBarring().getBarringAction().equals(OEPConstants.ORDER_UNBARRINGACTION_VALUE)){
			log.info("OEP Order Number : " + oepOrderNumber + " Setting Priority 9");
			template.sendBodyAndHeaders("direct:sendSalesOrderEBMToOSMWithPriority", soapMessage.getSOAPPart().getEnvelope(),
					orderHeaders);
			
		}else{
			log.info("OEP Order Number : " + oepOrderNumber + " Setting Priority 4");
		template.sendBodyAndHeaders("direct:sendSalesOrderEBMToOSM", soapMessage.getSOAPPart().getEnvelope(),
				orderHeaders);
		}

	}

	/*
	 * 
	 * This is the helper method to create SOAP request from supplied
	 * SoapBodyDoc.
	 * 
	 */
	private SOAPMessage createSOAPRequest(Document soapBodyDoc, String oepOrderNumber) throws SOAPException {

		MessageFactory messageFactory = null;
		SOAPMessage soapMessage = null;

		messageFactory = MessageFactory.newInstance();
		soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("ord", "http://xmlns.oracle.com/communications/ordermanagement");

		javax.xml.soap.SOAPBody soapBody = envelope.getBody();
		soapBody.addDocument(soapBodyDoc);

		log.info("OEP Order Number : " + oepOrderNumber + ". SOAP Body is set. ");

		SOAPHeader header = envelope.getHeader();

		SOAPElement securityElement = header.addChildElement("Security", "wsse",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
		securityElement.addAttribute(new QName("xmlns:wsu"),
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
		QName mustUnderstand = new QName("mustUnderstand");
		securityElement.addAttribute(mustUnderstand, "1");

		SOAPElement usernameToken = securityElement.addChildElement("UsernameToken", "wsse");
		usernameToken.addAttribute(new QName("wsu:Id"), "UsernameToken-8514627E9D7C3AD6E814914032601551");

		SOAPElement username = usernameToken.addChildElement("Username", "wsse");
		username.addTextNode("admin");

		SOAPElement password = usernameToken.addChildElement("Password", "wsse");
		password.addTextNode("c1g2b3u4");

		password.addAttribute(new QName("Type"),
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");

		log.info("OEP Order Number : " + oepOrderNumber + ". SOAP Headers are set. ");

		soapMessage.saveChanges();

		return soapMessage;
	}

	private CUSOPSEARCHOutputFlist getAccountDataFromBRM(final ProducerTemplate template, final String msisdn,
			final String uimServiceIdentifer, final String channelName, final String userName,
			final String oepOrderNumber, final String oepServiceType) {

		if (oepServiceType.equalsIgnoreCase((ServiceTypeEnum.BULK_SMS).value())
				|| oepServiceType.equalsIgnoreCase((ServiceTypeEnum.SC).value()))
			return getAccountDataFromBRMByFlag(template, uimServiceIdentifer, channelName, userName, oepOrderNumber,
					oepServiceType);
		else
			return getAccountDataFromBRMForMSISDN(template, msisdn, channelName, userName, oepOrderNumber,
					oepServiceType);
	}

	private CUSOPSEARCHOutputFlist getAccountDataFromBRMForMSISDN(final ProducerTemplate template, final String msisdn,
			final String channelName, final String userName, final String oepOrderNumber, final String oepServiceType) {

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

	private CUSOPSEARCHOutputFlist getAccountDataFromBRMByFlag(final ProducerTemplate template,
			final String uimServiceIdentifer, final String channelName, final String userName,
			final String oepOrderNumber, final String serviceType) {

		CUSOPSEARCHInputFlist opcodeSearch = new CUSOPSEARCHInputFlist();
		PARAMS param = new PARAMS();
		opcodeSearch.setPOID(OEPConstants.BRM_POID);
		opcodeSearch.setPROGRAMNAME(channelName);
		opcodeSearch.setUSERNAME(userName);
		opcodeSearch.setFLAGS(new BigInteger("201"));
		param.setVALUE(uimServiceIdentifer);
		param.setElem(new BigInteger("0"));
		opcodeSearch.getPARAMS().add(param);

		String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);

		return queryBRMForAccountData(template, inputXML, oepOrderNumber);

	}

	private RESULTS getActiveServiceFromSearchResult(CUSOPSEARCHOutputFlist searchResult) {

		for (RESULTS result : searchResult.getRESULTS()) {

			if ((result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_ACTIVE_STATUS_CODE)
					|| (result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_SUSPENDED_STATUS_CODE)) {

				return result;
			}
		}
		return null;
	}

	private CUSOPSEARCHOutputFlist queryBRMForAccountData(final ProducerTemplate template, final String inputXML,
			final String oepOrderNumber) {

		CUSOPSEARCHOutputFlist searchResult = null;

		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_CUS_SEARCH_OPCODE);
		opcode.setInputXml(inputXML);

		try {

			log.info("OEP order number : " + oepOrderNumber
					+ ". Calling BRM webservice to fetch Account data. Input xml : \n" + inputXML);

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

			if (searchResult.getERRORCODE() != null) {

				log.info("OEP order number : " + oepOrderNumber + ". Error while fetching Account data from BRM "
						+ searchResult.getERRORDESCR());
				throw new RuntimeException(searchResult.getERRORDESCR());

			}

		} catch (Exception e) {

			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			log.info("OEP order number : " + oepOrderNumber + ". Exception while fetching Account data from BRM "
					+ errorMessage);
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return searchResult;

	}

}
