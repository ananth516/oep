package com.oracle.oep.order.ebm.transform;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.brm.persistence.model.OEPConfigFeaturesT;
import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.DepositPaymentType;
import com.oracle.oep.order.model.HandSetDetailsType;
import com.oracle.oep.order.model.OptionsEnumType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.SIMDetailsType;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.CUSFLDCREDITSCORES;
import com.oracle.oep.osm.order.model.CUSFLDHANDSET;
import com.oracle.oep.osm.order.model.ClassificationCode;
import com.oracle.oep.osm.order.model.CreateOrder;
import com.oracle.oep.osm.order.model.DEPOSITS;
import com.oracle.oep.osm.order.model.DataArea;
import com.oracle.oep.osm.order.model.ID;
import com.oracle.oep.osm.order.model.Identification;
import com.oracle.oep.osm.order.model.ItemReference;
import com.oracle.oep.osm.order.model.MilestoneCode;
import com.oracle.oep.osm.order.model.ProcessSalesOrderFulfillment;
import com.oracle.oep.osm.order.model.ProcessSalesOrderFulfillmentEBM;
import com.oracle.oep.osm.order.model.Revision;
import com.oracle.oep.osm.order.model.SalesOrderLine;
import com.oracle.oep.osm.order.model.SalesOrderSchedule;
import com.oracle.oep.osm.order.model.Specification;
import com.oracle.oep.osm.order.model.SpecificationGroup;
import com.oracle.oep.osm.order.model.Status;


public class ProcessAddOrderEbmMapping {
	
	public static final Logger log = LogManager.getLogger(ProcessAddOrderEbmMapping.class);
	
	/**
	 * This method returns create order EBM object
	 * 
	 * Input arguments:  OrderEntryRequest request, OepOrder oepOrderDO, String orderAction, String oepOrderNumber, Map<String,OEPConfigPlanAttributesT> productSpecMap
	 * 
	 * Output argument : CreateOrder createSalesOrder
	 * 
	 * 
	 * **/
	
	public CreateOrder createOSMOrderEBM(final OrderEntryRequest request,final OepOrder oepOrderDO,final String orderAction,final String oepOrderNumber, final Map<String,OEPConfigPlanAttributesT> productSpecMap,final RESULTS searchResult,ProducerTemplate template){
		
		
		log.info("OEP Order number : "+oepOrderNumber+". Started EBM tranformation");
		
		CreateOrder createSalesOrder = new CreateOrder();		
		ProcessSalesOrderFulfillmentEBM salesOrderEBM = new ProcessSalesOrderFulfillmentEBM();
		DataArea dataArea = new DataArea();
		ProcessSalesOrderFulfillment salesOrderFulfillment = new ProcessSalesOrderFulfillment();		
		
		
		try{			
			//set headers values under ProcessSalesOrderFulfillment structure 
			setSalesOrderFulFillmentHeaders(oepOrderDO, orderAction, oepOrderNumber, salesOrderFulfillment);
			setSalesOrderLines(oepOrderDO, request, orderAction, oepOrderNumber, salesOrderFulfillment, productSpecMap,searchResult,template);
			dataArea.setProcessSalesOrderFulfillment(salesOrderFulfillment);
			salesOrderEBM.setDataArea(dataArea);
			createSalesOrder.setProcessSalesOrderFulfillmentEBM(salesOrderEBM);
			
		}
		
		catch(Exception e){
			
			e.printStackTrace();
			log.info("OEP Order Number :  "+oepOrderNumber+". Caught Exception when transforming order into EBM. "+e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		
		return createSalesOrder;
		
		
	} 
	

	/**
	 * This method sets sales order header
	 * 
	 * Input arguments:  OepOrder oepOrderDO, String orderAction, String oepOrderNumber, ProcessSalesOrderFulfillment salesOrderFulfillment
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	private void setSalesOrderFulFillmentHeaders( final OepOrder oepOrderDO, final String orderAction, final String oepOrderNumber, 
								 final ProcessSalesOrderFulfillment salesOrderFulfillment) throws DatatypeConfigurationException{
		
		log.info("OEP Order number : "+oepOrderNumber+" with order Action "+orderAction+". Setting sales order fulFillment headers.");
		Identification orderLevelIdentification = new Identification(); 
		
		Revision revision = new Revision();
		
		Status osmOrderHeaderStatus = new Status();
		
			
		orderLevelIdentification.setID(oepOrderNumber);
		
		//set Revision Number to 1. Hard coded value;
		revision.setNumber(new BigInteger("1"));
		
		orderLevelIdentification.setRevision(revision);
		
		osmOrderHeaderStatus.setCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		
		salesOrderFulfillment.setIdentification(orderLevelIdentification);
		salesOrderFulfillment.setOrderDateTime(OrderDataUtils.getXMLGregorianCalendarFromTimeStamp(new Date()));
		salesOrderFulfillment.setRequestedDeliveryDateTime(OrderDataUtils.getXMLGregorianCalendarFromTimeStamp(oepOrderDO.getOrderStartDate()));
		salesOrderFulfillment.setTypeCode(OEPConstants.SALES_ORDER_TYPE_CODE);
		salesOrderFulfillment.setFulfillmentPriorityCode(new BigInteger(OEPConstants.OSM_ORDER_PRIORITY_CODE_DEFAULT));
		salesOrderFulfillment.setFulfillmentSuccessCode(OEPConstants.FULFILLMENT_SUCCESS_CODE);
		salesOrderFulfillment.setFulfillmentModeCode(OEPConstants.FULFILLMENT_MODE_CODE_DELIVER);
		salesOrderFulfillment.setStatus(osmOrderHeaderStatus);
		salesOrderFulfillment.setOrderSource(OEPConstants.OAP_CHANNEL_NAME);
		salesOrderFulfillment.setOrderName(orderAction);
		
		
		
	}		
	
	/**
	 * This method determines whether given order line is parent order line or child order line. 
	 *  
	 * Input arguments :   OepOrderLine oepOrderLineDOO
	 * 
	 * Output arguments : boolean true or false
	 * 
	 * 
	 **/
	private boolean isBasePlan(final OepOrderLine oepOrderLineDO){
		
		String lineId= oepOrderLineDO.getOrderLineId();
		String parentLineId = oepOrderLineDO.getParentOrderLineId();
		
		if(lineId.equals(parentLineId)){
			
			return true;
		}
		
		return false;
		
	}
	
	/**
	 * This method sets common order line parameters
	 * 
	 * Input arguments:  OepOrder oepOrderDO, String orderAction, String oepOrderNumber, ProcessSalesOrderFulfillment salesOrderFulfillment
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	private void setSalesOrderLineCommonParams(final SalesOrderLine salesOrderLine, final OrderEntryRequest request, 
			final OepOrderLine oepOrderLineDO, final OepOrder oepOrderDO, final String lineId, final String orderAction){		
		
		Identification lineIdentification = new Identification();
		ID parentLineIdentification = new ID();
		Status status = new Status();			
		
		lineIdentification.setID(oepOrderLineDO.getOrderLineId());
		salesOrderLine.setIdentification(lineIdentification);
		
		
		if(oepOrderLineDO.getParentOrderLineId()!=null){
			parentLineIdentification.setID(oepOrderLineDO.getParentOrderLineId());
		}
		else {
			
			parentLineIdentification.setID(lineId);
		}
		
		salesOrderLine.setParentLineIdentification(parentLineIdentification);		
		
		salesOrderLine.setAccountId(oepOrderLineDO.getAccountId());		
		
		salesOrderLine.setStartBillingOnFirstServiceUsageIndicator(true);
		salesOrderLine.setFulfillmentModeCode(OEPConstants.FULFILLMENT_MODE_CODE_DO);
		salesOrderLine.setMilestoneCode(new MilestoneCode());
				
		status.setCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		salesOrderLine.setStatus(status);
			
			
	}	
	
	/**
	 * This method sets order line parameters on the order line
	 * 
	 * Input arguments :  OepOrder oepOrderDO, OrderEntryRequest request, String orderAction, String oepOrderNumber, 
	 * 					ProcessSalesOrderFulfillment salesOrderFulfillment, Map<String,OEPConfigPlanAttributesT> productSpecMap
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	private void setSalesOrderLines(final OepOrder oepOrderDO, final OrderEntryRequest request, final String orderAction, 
			final String oepOrderNumber, final ProcessSalesOrderFulfillment salesOrderFulfillment, final Map<String,OEPConfigPlanAttributesT> productSpecMap,final RESULTS searchResult,ProducerTemplate template){
		
		List<SalesOrderLine> salesOrderLinesList = salesOrderFulfillment.getSalesOrderLine();
		
		for(OepOrderLine oepOrderLineDO : oepOrderDO.getOepOrderLines() ){	
			
			
			String lineId = oepOrderLineDO.getOrderLineId();
			String planName = oepOrderLineDO.getPlanName();			
			String productSpec = oepOrderLineDO.getProductSpecification();
			
			if(productSpec==null)
			{
				throw new RuntimeException("Product Specification (PS) for plan :"+ planName +" is empty");
			}
			
			
			log.info("OEP Order Number : "+oepOrderNumber+". Started setting orderLine with LineID : "+lineId);			
			
			SalesOrderLine salesOrderLine =  new SalesOrderLine();
			
			salesOrderLine.setAccountName(ProcessAddOrderEbmHelper.getAccountName(request, orderAction));	
			if (orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
				if (searchResult != null) {
					salesOrderLine.setBillingAccountId(searchResult.getCUSFLDBAACCOUNTNO());
				}
			} else {
				salesOrderLine.setBillingAccountId(ProcessAddOrderEbmHelper.getBillingAccountId(request, orderAction));
			}
			if (orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) {
				if (searchResult != null) {
					salesOrderLine.setCustomerAccountId(searchResult.getCUSFLDCAACCOUNTNO());

				}
			} else {
				salesOrderLine.setCustomerAccountId(ProcessAddOrderEbmHelper.getCustomerAccountId(request, orderAction));
			}
			
			
				if((orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)) && isBasePlan(oepOrderLineDO)){
				salesOrderLine.setServiceActionCode(OEPConstants.OSM_PREPAIDTOPOSTPAID_ACTION_CODE);
			}else if((orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
					|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)) && isBasePlan(oepOrderLineDO)){
				salesOrderLine.setServiceActionCode(OEPConstants.OSM_POSTPAIDTOPREPAID_ACTION_CODE);	
			}else
			salesOrderLine.setServiceActionCode(oepOrderLineDO.getPlanAction());
			
			//Call setSalesOrderLineCommonParams to set common order parameters on SalesOrderLine
			
			setSalesOrderLineCommonParams(salesOrderLine, request, oepOrderLineDO, oepOrderDO, lineId, orderAction);
			
			//SalesOrderSchedule
			
			SalesOrderSchedule salesOrderSchedule = new SalesOrderSchedule();
			
			try {
				salesOrderSchedule.setRequestedDeliveryDateTime(OrderDataUtils.getXMLGregorianCalendarFromTimeStamp((oepOrderDO.getOrderStartDate())));
				
			} 
			catch (DatatypeConfigurationException e) {
				
				throw new RuntimeException(e.getMessage());
			}
			
			salesOrderLine.setSalesOrderSchedule(salesOrderSchedule);
			//Set Item Reference
			
			ItemReference  itemReference = new ItemReference();				
			
			itemReference.setName(planName);			
			
			List<ClassificationCode> classificationCodeList = itemReference.getClassificationCode();
			
			ClassificationCode fulfillmentItemCode = new ClassificationCode();			
			fulfillmentItemCode.setListID(OEPConstants.FIC_ATTRIBUTE_NAME);
			fulfillmentItemCode.setContent(oepOrderLineDO.getProductSpecification());
			
			ClassificationCode permittedTypeCode = new ClassificationCode();
			permittedTypeCode.setListID(OEPConstants.PTC_ATTRIBUTE_NAME);
			permittedTypeCode.setContent(OrderDataUtils.getPermittedTypeCode(productSpec));
			
			
			ClassificationCode billingProductTypeCode = new ClassificationCode();
			billingProductTypeCode.setListID(OEPConstants.BPT_ATTRIBUTE_NAME);
			billingProductTypeCode.setContent(OEPConstants.BPT_ATTRIBUTE_VALUE);
			classificationCodeList.add(billingProductTypeCode);
			
			
			log.info("OEP Order Number : "+oepOrderNumber+". Product Specififcation is "+productSpec);
			
			classificationCodeList.add(fulfillmentItemCode);
			classificationCodeList.add(permittedTypeCode);
			
			itemReference.setServiceIndicator(true);
			itemReference.setTypeCode(OEPConstants.OSM_TYPE_CODE_PRODUCT);
						
			itemReference.setPlanStartT(ProcessAddOrderEbmHelper.getPlanStartDate(request, orderAction, planName));
			itemReference.setPlanEndT(ProcessAddOrderEbmHelper.getPlanEndDate(request, orderAction, planName));
			
			if(isBasePlan(oepOrderLineDO)){
				itemReference.setPlanType(new BigInteger("1"));
			}
			else
				itemReference.setPlanType(new BigInteger("2"));
			
			itemReference.setCsrId(oepOrderDO.getCsrid());
			itemReference.setNetworkIndicator(false);
			if(searchResult != null){
			itemReference.setServiceIdentifier(searchResult.getLOGIN());
			}
			log.info("OEP Order Number : "+oepOrderNumber+". Initializing Specification Group for "+planName);
			
			SpecificationGroup specificationGroup = new SpecificationGroup();
			setSpecificationGroupDetails(specificationGroup, orderAction, request, oepOrderDO, oepOrderLineDO, oepOrderNumber, productSpec, productSpecMap,template);
			
			itemReference.setSpecificationGroup(specificationGroup);	
			log.info("OEP Order Number : "+oepOrderNumber+". SpecificationGroup is set.");
			log.info("	set old plan name");				
			//Set HandSet details, Credit scores, De
			if(isBasePlan(oepOrderLineDO)){	
				
				if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
						|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)
						||	orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)
						|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)
						
						){
					log.info("	Entered set old plan name");
					String oldbasePlan = searchResult.getCUSFLDPLANNAME();
					itemReference.setOldPlanName(oldbasePlan);
					log.info("	Exit set old plan name :: with :: "+oldbasePlan);
				}
				
				if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION)||orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
					
					List<HandSetDetailsType> handSetList = ProcessAddOrderEbmHelper.getHandSetDetailsFromOrder(request, orderAction);
					setHandSetDetails(itemReference, handSetList, request, orderAction);
				}
				
				if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION)||orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION)|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
					
					setCreditScores(itemReference, request, orderAction);
				}
				
				if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION) ||orderAction.equals(OEPConstants.OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION)
						|| orderAction.equals(OEPConstants.OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION) ||orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION)||orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION)){
					List<DepositPaymentType> depositPaymets = ProcessAddOrderEbmHelper.getDepositsFromOrder(request, orderAction);
					
					if(depositPaymets!=null){
						setDeposits(itemReference, depositPaymets, request, orderAction,productSpecMap);
					}
				}	
				
				if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION))
				{
				String notificationLanguage = ProcessAddOrderEbmHelper.getNotificationLanguageFromOrder(request, orderAction);
				
				if(notificationLanguage!=null){
					setNotificationLanguage(itemReference, notificationLanguage);
				}
			}
				
			}
			
									
			
			itemReference.setOverridenPrice(ProcessAddOrderEbmHelper.getOverridenPrice(request, orderAction, planName));
			salesOrderLine.setItemReference(itemReference);
			salesOrderLinesList.add(salesOrderLine);
			
			
			log.info("OEP Order Number : "+oepOrderNumber+". Order Line with id "+ lineId+" is set.");				
			
		}
	}
	
	/**
	 * This method sets specification extensible attribute  on the order line
	 * 
	 * Input arguments :  SpecificationGroup specificationGroup, String orderAction, OrderEntryRequest request, 
			OepOrder oepOrderDO, OepOrderLine oepOrderLineDO, String oepOrderNumber, String productSpec, Map<String,OEPConfigPlanAttributesT> productSpecMap			
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	
	private void setSpecificationGroupDetails(final SpecificationGroup specificationGroup, final String orderAction, final OrderEntryRequest request, 
			final OepOrder oepOrderDO, final OepOrderLine oepOrderLineDO, final String oepOrderNumber, final String productSpec, final Map<String,OEPConfigPlanAttributesT> productSpecMap,ProducerTemplate template){
		
		specificationGroup.setName(OEPConstants.OSM_EXTENSIBLE_ATTRIBUTE);
		
		List<Specification> specificationsList = specificationGroup.getSpecification(); 	
		
		if(!orderAction.equals(OEPConstants.OEP_ADD_SHORT_CODE_SERVICE_ACTION)) {
			Specification msisdnSpec = new Specification();
			msisdnSpec.setName(OEPConstants.MSISDN);
			msisdnSpec.setValue(oepOrderDO.getServiceNo());				
			specificationsList.add(msisdnSpec);
		}
		
		log.info("OEP Order Number : "+oepOrderNumber+". MSISDN Specification with value "+oepOrderDO.getServiceNo()+" is set.");
		
		//Set SIM Spec Group for base plan
		if(isBasePlan(oepOrderLineDO)){			
			
			
			String niceCategory = ProcessAddOrderEbmHelper.getNiceCategory(request, orderAction);
			String options = ProcessAddOrderEbmHelper.getWaiverOptions(request, orderAction);
			String waiverReason = ProcessAddOrderEbmHelper.getWaiverReason(request, orderAction);
			
			if(niceCategory!=null && niceCategory.equalsIgnoreCase("1")){	
				
				
				Specification niceCategorySpec = new Specification();
				niceCategorySpec.setName(OEPConstants.NICE_CATEGORY);
				niceCategorySpec.setValue(ProcessAddOrderEbmHelper.getNiceCategoryValue(request, orderAction));				
				specificationsList.add(niceCategorySpec);
			}
			
			if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_ADD_MOBILE_BB_SERVICE_ACTION) || orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
				
				SIMDetailsType simDetails = ProcessAddOrderEbmHelper.getSIMDetails(request, orderAction);						
				setSIMDetailsOnSpecGroup(specificationsList, simDetails);
				
				log.info("OEP Order Number : "+oepOrderNumber+". SIMDetails are set");
			}
			
			if(orderAction.equals(OEPConstants.OEP_ADD_MOBILE_SERVICE_ACTION) && options!=null)
			{
				Specification waiverOptionsSpec = new Specification();
				waiverOptionsSpec.setName(OEPConstants.WAIVER_OPTIONS);
				waiverOptionsSpec.setValue(options);				
				specificationsList.add(waiverOptionsSpec);
				
				if(options.equalsIgnoreCase(OptionsEnumType.WAIVE_OFF.value()) && waiverReason!=null) {
					Specification waiverReasonSpec = new Specification();
					waiverReasonSpec.setName(OEPConstants.WAIVER_REASON);
					waiverReasonSpec.setValue(waiverReason);				
					specificationsList.add(waiverReasonSpec);
				}
			
			}
			
			if(orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_PORTIN_ACTION)){
				
				
				Specification portType = new Specification();
				portType.setName(OEPConstants.PORT_TYPE);
				portType.setValue(ProcessAddOrderEbmHelper.getPortType(request, orderAction));				
				specificationsList.add(portType);
				
				log.info("OEP Order Number : "+oepOrderNumber+". PortType are set");
			}	
			
			
			String portTypePortIN = ProcessAddOrderEbmHelper.getPortType(request, orderAction);
			
			if(portTypePortIN!=null)
			{
				if(portTypePortIN.equalsIgnoreCase(OEPConstants.OEP_PORTIN)){
					
					Specification portType = new Specification();
					portType.setName(OEPConstants.PORT_TYPE);
					portType.setValue(portTypePortIN);				
					specificationsList.add(portType);
					
					log.info("OEP Order Number : "+oepOrderNumber+". PortType are set");
				}
			}
			
			Specification isTouristKit = new Specification();
			
			OEPConfigPlanAttributesT config = productSpecMap.get(oepOrderLineDO.getPlanName());
			isTouristKit.setName(OEPConstants.IS_TOURIST);
			if ((BigDecimal.ZERO).equals(config.getTouristKit())) {
				isTouristKit.setValue(OEPConstants.OEP_FALSE);
			} else if ((BigDecimal.ONE).equals(config.getTouristKit())) {
				isTouristKit.setValue(OEPConstants.OEP_TRUE);
			} else {
				isTouristKit.setValue(OEPConstants.OEP_FALSE);
			}
			
			if(!oepOrderLineDO.getServiceType().equalsIgnoreCase(OEPConstants.OEP_BB_SERVICE_TYPE))
				specificationsList.add(isTouristKit);
			
			Specification serviceTypeSpec= new Specification();
			serviceTypeSpec.setName(OEPConstants.SERVICE_TYPE);
			serviceTypeSpec.setValue(ProcessAddOrderEbmHelper.getServiceType(request, orderAction));				
			specificationsList.add(serviceTypeSpec);
			
			
		}		
			
		if(productSpecMap.get(oepOrderLineDO.getPlanName()).getConfigFeaturesTs().size()>0 ){			

			log.info("Config Features size for "+ oepOrderLineDO.getPlanName() +": " + productSpecMap.get(oepOrderLineDO.getPlanName()).getConfigFeaturesTs().size() );			
						
			for (OEPConfigFeaturesT feature : productSpecMap.get(oepOrderLineDO.getPlanName()).getConfigFeaturesTs()){
				
				if(feature.getParamName().equals(OEPConstants.VAS_NAME)){
					Specification vasType = new Specification();
					vasType.setName(OEPConstants.VAS_NAME);
					vasType.setValue(feature.getParamValue());
					specificationsList.add(vasType);
				}
			}			
		}
		
		if(productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BASEPLAN_PS)
				||productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BB_PS)){
			
			String planServiceSubType = productSpecMap.get(oepOrderLineDO.getPlanName()).getServiceSubType();
			
			log.info("Plan Service SubType --"+planServiceSubType);
			
			if(planServiceSubType.equals(OEPConstants.OEP_PLAN_FLEXTYPE)){
				
				Specification cosIDSpec = new Specification();				
				cosIDSpec.setName(OEPConstants.OEP_PLAN_SPEC_FLEXATTRIBUTE);		
				cosIDSpec.setValue(OEPConstants.OEP_PLAN_SPEC_FLEXATTRIBUTE_VALUE);
				specificationsList.add(cosIDSpec);
				
				log.info("Flex Plan Service SubType has been set --"+oepOrderLineDO.getPlanName());
			}
			
		}
			
					
		
		if(productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BASEPLAN_PS)
				||productSpec.equalsIgnoreCase(OEPConstants.MOBILE_DATA_PS)
				||productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BB_PS)
				||productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BB_ADDON_PS)){
			
			log.debug("COS ID spec need to set --");
			
			/*Specification cosIDSpec = new Specification();				
			cosIDSpec.setName(OEPConstants.COS_ID);		
			cosIDSpec.setValue(productSpecMap.get(oepOrderLineDO.getPlanName()).getCos());
			specificationsList.add(cosIDSpec);*/
			
			ListIterator<Map<String, Object>> cosIdlistItr =queryCosIdParams(oepOrderLineDO.getPlanName(),template);

	        while(cosIdlistItr.hasNext()){              
	   				 
	            		Map<String, Object> cosmap = cosIdlistItr.next();            					
	            						 
	            		
							 
	            			 Object cosIdentifierName= cosmap.get("TYPE");
							 
	            			 Object cosIdValue= cosmap.get("UNIQUE_ID");
							 
							 
							 
							 log.debug(cosIdentifierName +" : "+cosIdValue);							 
							 							 
							 Specification cosIDSpec = new Specification();
							 
							 if(cosIdentifierName!=null && cosIdValue!=null)
							 {	 
								 if (cosIdentifierName.equals("PRIMARY")){
									cosIDSpec.setName(OEPConstants.PCOS_ID);
									cosIDSpec.setValue(cosIdValue.toString());
									specificationsList.add(cosIDSpec);
									log.debug("PCOS Set --");
								 }
								 else if (cosIdentifierName.equals("SECONDARY")){
										cosIDSpec.setName(OEPConstants.SCOS_ID);
										cosIDSpec.setValue(cosIdValue.toString());
										specificationsList.add(cosIDSpec);
										log.debug("SCOS Set --");
									 }
								 else{
										 log.debug("No COS Set --");
									 }
									
							 }
							 else
							 {
								 log.debug("No COS Set --");
							 } 
	        }
			
		}  else if(productSpec.equalsIgnoreCase(OEPConstants.SHORT_CODE_PS)
				|| productSpec.equalsIgnoreCase(OEPConstants.SHORT_CODE_ADDON_PS)){
			
			Specification shortCodeNumSpec = new Specification();				
			shortCodeNumSpec.setName(OEPConstants.SHORT_CODE_NUMBER);		
			shortCodeNumSpec.setValue(ProcessAddOrderEbmHelper.getShortCodeNumber(request, orderAction));
			specificationsList.add(shortCodeNumSpec);
			
			Specification shortCodeTypeSpec = new Specification();				
			shortCodeTypeSpec.setName(OEPConstants.SHORT_CODE_TYPE);		
			shortCodeTypeSpec.setValue(ProcessAddOrderEbmHelper.getShortCodeType(request, orderAction));
			specificationsList.add(shortCodeTypeSpec);
			
			Specification shortCodeCategorySpec = new Specification();				
			shortCodeCategorySpec.setName(OEPConstants.SHORT_CODE_CATEGORY);		
			shortCodeCategorySpec.setValue(ProcessAddOrderEbmHelper.getShortCodeCategory(request, orderAction));
			specificationsList.add(shortCodeCategorySpec);
			
			Specification emailIdSpec = new Specification();				
			emailIdSpec.setName(OEPConstants.EMAIL_ID);		
			emailIdSpec.setValue(ProcessAddOrderEbmHelper.getEmailId(request, orderAction));
			specificationsList.add(emailIdSpec);
			
		}
		
		if(orderAction.equals(OEPConstants.OEP_ADD_BULK_SMS_ACTION)) {
			
			Specification planId = new Specification();				
			planId.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_PLAN_ID);		
			planId.setValue(productSpecMap.get(oepOrderLineDO.getPlanName()).getCos());
			specificationsList.add(planId);
			
			Specification userId = new Specification();				
			userId.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_USER_ID);		
			userId.setValue(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getOrderUserId());
			specificationsList.add(userId);
			
			Specification contactNumber = new Specification();				
			contactNumber.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_CONTACT_NUMBER);		
			contactNumber.setValue(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getContactNo());
			specificationsList.add(contactNumber);
			
			Specification emailId = new Specification();				
			emailId.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_EMAIL_ID);		
			emailId.setValue(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getEmail());
			specificationsList.add(emailId);
			
			Specification id = new Specification();				
			id.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_ID);		
			id.setValue(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getId());
			specificationsList.add(id);
			
			Specification idType = new Specification();				
			idType.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_ID_TYPE);		
			idType.setValue(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getIdType().value().toString());
			specificationsList.add(idType);
			
			Specification type = new Specification();				
			type.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_TYPE);
			type.setValue(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getBulkSMSType().value().toString());
			specificationsList.add(type);
			
			Specification customerName = new Specification();				
			customerName.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_CUSTOMER_NAME);		
			customerName.setValue(request.getProcessAddOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getCustomerName());
			specificationsList.add(customerName);
			
		}
				
	}
	
	
	
	/**
	 * This method sets hand set details on the order line
	 * 
	 * Input arguments :  ItemReference itemReference, List<HandSetDetailsType> inputHandSetList, OrderEntryRequest request, String orderAction			
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	
	private void setHandSetDetails(final ItemReference itemReference, final List<HandSetDetailsType> inputHandSetList,  final OrderEntryRequest request, final String orderAction){
		  
		
		List<CUSFLDHANDSET> ebmHandSetList = itemReference.getCUSFLDHANDSET();
		int i=0;
		for (HandSetDetailsType handSet : inputHandSetList){
		
			CUSFLDHANDSET handSetDetails = new CUSFLDHANDSET();
			handSetDetails.setCUSFLDDEVICEMODEL(handSet.getDeviceMake());
			handSetDetails.setCUSFLDDEVICETYPE(new BigInteger(handSet.getDeviceType()));
			handSetDetails.setElem(new BigInteger(i+""));
			i++;
			ebmHandSetList.add(handSetDetails);
		}
		
		
		log.info("Handset Details are set.");
		
	}
	
	/**
	 * This method sets credit score details on the order line
	 * 
	 * Input arguments :  ItemReference itemReference, OrderEntryRequest request, String orderAction				
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	private void setCreditScores(final ItemReference itemReference, final OrderEntryRequest request, final String orderAction){
		  
		CUSFLDCREDITSCORES creditScores = new CUSFLDCREDITSCORES();
		creditScores.setCUSFLDCREDITSCORE(ProcessAddOrderEbmHelper.getCreditScores(request, orderAction));
		creditScores.setCUSFLDSCMINVAL(ProcessAddOrderEbmHelper.getSpendingCapMin(request, orderAction));
		creditScores.setCUSFLDSCMAXVAL(ProcessAddOrderEbmHelper.getSpendingCapMax(request, orderAction));
		creditScores.setCUSFLDSCACTUALVAL(ProcessAddOrderEbmHelper.getSpendingCap(request, orderAction));
		creditScores.setElem(new BigInteger("0"));
		
		itemReference.setCUSFLDCREDITSCORES(creditScores);
		
		
		log.info("Credit scores are set.");
	}
	
	/**
	 * This method sets deposit details on the order line
	 * 
	 * Input arguments :  ItemReference itemReference, List<DepositPaymentType> depositPaymets, OrderEntryRequest request, String orderAction, Map<String,OEPConfigPlanAttributesT> productSpecMap				
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	private void setDeposits(final ItemReference itemReference, final List<DepositPaymentType> depositPaymets, final OrderEntryRequest request, final String orderAction,final Map<String,OEPConfigPlanAttributesT> productSpecMap){
		  
					
		List<DEPOSITS> ebmDeposistsList = itemReference.getDEPOSITS();
		
		int i=0;
		
		for (DepositPaymentType depositPaymet : depositPaymets){
			
			String elem=i+"";
			String planName = depositPaymet.getPlanName();			
			DEPOSITS deposits = new DEPOSITS();
			deposits.setRECEIPTNO(depositPaymet.getDepositReceiptNo());
			deposits.setCUSFLDSERVICETYPE(depositPaymet.getServiceType().toString());
			deposits.setCUSFLDCOLLECTEDON(OrderDataUtils.getTimeStampFromCalender(depositPaymet.getCollectedOn()).getTime()/1000L);
			deposits.setCUSFLDWAIVERREASON(depositPaymet.getWaveOffReason());
			deposits.setAMOUNT(depositPaymet.getDepositAmount());			
			if(depositPaymet.getWaveOff() != null)
			{
			deposits.setOPTIONS(new BigInteger(depositPaymet.getWaveOff()));
			}
			if(depositPaymet.getDepositType() != null)
			{
			deposits.setTYPE(new BigInteger(depositPaymet.getDepositType()));
			}
			deposits.setTYPESTR(planName);
			deposits.setElem(new BigInteger(elem));
			
			if(depositPaymet.getDepositWaiver() != null)
			{			
			deposits.setCUSFLDDEPOSITWAIVER(new BigDecimal(depositPaymet.getDepositWaiver()));
			}
			
			i++;
			
			ebmDeposistsList.add(deposits);
 			//Could not able to map options and wave off reason
		}
		
		log.info("Deposits are set.");
		
	}
	
	/**
	 * This method sets SIM details on the specification group
	 * 
	 * Input arguments :  List<Specification> specificationsList,SIMDetailsType simDetails				
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	private void setSIMDetailsOnSpecGroup(final List<Specification> specificationsList,final SIMDetailsType simDetails){
		
		Specification simSpec = new Specification();
		simSpec.setName(OEPConstants.ICCID);
		simSpec.setValue(simDetails.getSimNo());				
		specificationsList.add(simSpec);
		
		Specification imsiSpec= new Specification();
		imsiSpec.setName(OEPConstants.IMSI);
		imsiSpec.setValue(simDetails.getImsi());				
		specificationsList.add(imsiSpec);
		
	}
	
	/**
	 * This method sets Notification Language on the order line
	 * 
	 * Input arguments :  ItemReference itemReference, String notificaitonlanguage				
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	private void setNotificationLanguage(final ItemReference itemReference, final String notificationLanguage){
		  

		itemReference.setNotificationLanguage(notificationLanguage);
		
		
		log.info("Notification Language is set.");
	}
	
	
	/**
	 * 
	 * @return
	 */
	private ListIterator<Map<String, Object>> queryCosIdParams(String planName,ProducerTemplate template){
		
 
		
		log.debug("Entered queryCosIdParams ---"+planName);
		
		ListIterator<Map<String, Object>> coslitr= null;
		 
		 Map<String,Object> params = new HashMap<String,Object>();
		 params.put("planName", planName);

		
		 List<Map<String, Object>>  resultSelt = (List<Map<String, Object>>) template.requestBody("direct:queryCosFromBRM", params);
		 
		 if(resultSelt!=null){
			 
			 System.out.println("CoS ID resultSelt is not null----");
			 
			 coslitr =resultSelt.listIterator();

		       		 
			 log.info("Cos ID ResultSet size --- "+resultSelt.size());  
			 log.info("Returning the Cos Result Set");
			 
		 }
		 
		 
		 
		
		return coslitr;
	}
		
}
