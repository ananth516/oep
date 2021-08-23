package com.oracle.oep.order.ebm.transform;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brm.opcodes.CUSFLDADDONPKG;
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
import com.oracle.oep.order.model.SimTypeEnum;
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


public class ProcessModifyOrderEbmMapping {
	
	public static final Logger log = LogManager.getLogger(ProcessModifyOrderEbmMapping.class);
	
	/**
	 * This method returns create order EBM object
	 * 
	 * Input arguments:  OrderEntryRequest request, OepOrder oepOrderDO, String orderAction, String oepOrderNumber, Map<String,OEPConfigPlanAttributesT> productSpecMap
	 * 
	 * Output argument : CreateOrder createSalesOrder
	 * 
	 * 
	 * **/
	
	public CreateOrder createOSMOrderEBM(final OrderEntryRequest request,final OepOrder oepOrderDO,final String orderAction,final String oepOrderNumber, 
			final Map<String,OEPConfigPlanAttributesT> productSpecMap, final RESULTS searchResult,ProducerTemplate template){
		
		
		log.info("OEP Order number : "+oepOrderNumber+". Started EBM tranformation");
		
		CreateOrder createSalesOrder = new CreateOrder();		
		ProcessSalesOrderFulfillmentEBM salesOrderEBM = new ProcessSalesOrderFulfillmentEBM();
		DataArea dataArea = new DataArea();
		ProcessSalesOrderFulfillment salesOrderFulfillment = new ProcessSalesOrderFulfillment();		
		
		
		try{			
			//set headers values under ProcessSalesOrderFulfillment structure 
			setSalesOrderFulFillmentHeaders(request, oepOrderDO, orderAction, oepOrderNumber, salesOrderFulfillment);
			setSalesOrderLines(oepOrderDO, request, orderAction, oepOrderNumber, salesOrderFulfillment, productSpecMap, searchResult,template);
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
	private void setSalesOrderFulFillmentHeaders( final OrderEntryRequest request,final OepOrder oepOrderDO, final String orderAction, final String oepOrderNumber, 
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
		String priorityCode=ProcessModifyOrderEbmHelper.getPriorityCode(orderAction);
		salesOrderFulfillment.setFulfillmentPriorityCode(new BigInteger(priorityCode));
		//salesOrderFulfillment.setFulfillmentPriorityCode(new BigInteger(OEPConstants.OSM_ORDER_PRIORITY_CODE_DEFAULT));
		salesOrderFulfillment.setFulfillmentSuccessCode(OEPConstants.FULFILLMENT_SUCCESS_CODE);
		salesOrderFulfillment.setFulfillmentModeCode(OEPConstants.FULFILLMENT_MODE_CODE_DELIVER);
		salesOrderFulfillment.setStatus(osmOrderHeaderStatus);
		salesOrderFulfillment.setOrderSource(ProcessModifyOrderEbmHelper.getChannelName(request, orderAction));
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
		
		
		if(request.getProcessAddOnManagment()!=null){
			if(request.getProcessAddOnManagment().getBillingIndicator()!=null && request.getProcessAddOnManagment().getBillingIndicator().equals("1")){
				
				salesOrderLine.setBillableLineItem(false);
			}
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
			final String oepOrderNumber, final ProcessSalesOrderFulfillment salesOrderFulfillment, final Map<String,OEPConfigPlanAttributesT> productSpecMap, final RESULTS result,ProducerTemplate template){
		
		List<SalesOrderLine> salesOrderLinesList = salesOrderFulfillment.getSalesOrderLine();

		/*
		 * This is introduced to store packageid and plan combination as part of Power, PowerPlus scenario
		 */
		
		List<String> planPackage = new ArrayList<String>();
		
		for    (CUSFLDADDONPKG addOnPackage : result.getCUSFLDADDONPKG() ){
			
			planPackage.add(addOnPackage.getCUSFLDADDONPLANNAME()+"^"+addOnPackage.getPACKAGEID());
            
		}
		// End of Function
		
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
			
			
			salesOrderLine.setBillingAccountId(ProcessModifyOrderEbmHelper.getBillingAccountId(request, orderAction, result));
			salesOrderLine.setCustomerAccountId(ProcessModifyOrderEbmHelper.getCustomerAccountId(request, orderAction, result));
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
			
			
			log.info("OEP Order Number : "+oepOrderNumber+". Product Specififcation is "+productSpec);
			
			classificationCodeList.add(fulfillmentItemCode);
			classificationCodeList.add(permittedTypeCode);
			
			itemReference.setServiceIndicator(true);
			itemReference.setTypeCode(OEPConstants.OSM_TYPE_CODE_PRODUCT);

			
			itemReference.setOverridenPrice(ProcessModifyOrderEbmHelper.getOverridenPrice(request, orderAction, planName));
			itemReference.setPlanStartT(ProcessModifyOrderEbmHelper.getPlanStartDate(request, orderAction, planName));
			itemReference.setPlanEndT(ProcessModifyOrderEbmHelper.getPlanEndDate(request, orderAction, planName));
			
			if(isBasePlan(oepOrderLineDO)){
				log.info("OEP Order Number : "+oepOrderNumber+". Setting PlanType for BasePlan");
				itemReference.setPlanType(new BigInteger("1"));
				
				if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)
				   ||orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION) ||orderAction.equals(OEPConstants.OEP_MODIFY_BB_CHANGE_PLAN)){
					
//					RESULTS result = searchResult.getRESULTS().get(0);
										
					String oldbasePlan = result.getCUSFLDPLANNAME();
					itemReference.setOldPlanName(oldbasePlan);
				
				
				}
				
				else if(orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION) || orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)){
							
//							RESULTS result = searchResult.getRESULTS().get(0);
														
//					String oldbasePlan = result.getPLAN().getNAME();
					String oldbasePlan = null;
					Iterator<RESULTS.PLAN> incompatibleAddonIterator= result.getPLAN().iterator();
					
					while (incompatibleAddonIterator.hasNext()) {
						RESULTS.PLAN allplanList = (RESULTS.PLAN)incompatibleAddonIterator.next();
						Byte planType = allplanList.getCUSFLDPLANTYPE();
						if(planType.equals((byte)1))
						{
							oldbasePlan = allplanList.getNAME();
						}
					}
					
					itemReference.setOldPlanName(oldbasePlan);
						
						
				}
				
				List<DepositPaymentType> depositPaymets = ProcessModifyOrderEbmHelper.getDepositsFromOrder(request, orderAction);
					
				if(depositPaymets!=null){

					setDeposits(itemReference, depositPaymets, request, orderAction,productSpecMap);

				}               
					
				setCreditScores(itemReference, request, orderAction);
				
			}
			else {
				
				log.info("OEP Order Number : "+oepOrderNumber+". Setting PlanType for AddonPlan");
				itemReference.setPlanType(new BigInteger("2"));				
			}
			
			
			
			if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE) || orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION)
					|| orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)||orderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION)
					||orderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN)
					)
					{			
					itemReference.setServiceIdentifier(ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, orderAction));
					}
			
			else
			{
			itemReference.setServiceIdentifier(result.getLOGIN());			
			}
			
			itemReference.setCsrId(oepOrderDO.getCsrid());
			itemReference.setNetworkIndicator(false);	
			
			
			if(isBasePlan(oepOrderLineDO)){
				setHandSetDetails(itemReference,request, orderAction);
			}
			
			
			log.info("OEP Order Number : "+oepOrderNumber+". Initializing Specification Group for "+planName);
			
			SpecificationGroup specificationGroup = new SpecificationGroup();
			setSpecificationGroupDetails(salesOrderLine,specificationGroup, orderAction, request, oepOrderDO, oepOrderLineDO, oepOrderNumber, productSpec, productSpecMap,result,planPackage,template);
			
			itemReference.setSpecificationGroup(specificationGroup);	
			
			salesOrderLine.setItemReference(itemReference);
			salesOrderLinesList.add(salesOrderLine);			
			
			log.info("OEP Order Number : "+oepOrderNumber+". Order Line with id "+ lineId+" is set.");				
			
		}
	}
	
	/**
	 * 
	 * @param itemReference
	 * @param request
	 * @param orderAction
	 */
	private void setHandSetDetails(ItemReference itemReference, OrderEntryRequest request, String orderAction) {
		// TODO Auto-generated method stub
		
		if(orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION) && request.getProcessAddOnManagment().getDeviceMake()!=null){
			List<CUSFLDHANDSET> ebmHandSetList = itemReference.getCUSFLDHANDSET();
			int i=0;
			
		
			CUSFLDHANDSET handSetDetails = new CUSFLDHANDSET();
			handSetDetails.setCUSFLDDEVICEMODEL(request.getProcessAddOnManagment().getDeviceMake());
			handSetDetails.setCUSFLDDEVICETYPE(new BigInteger(request.getProcessAddOnManagment().getDeviceType()));
			handSetDetails.setElem(new BigInteger(i+""));
			
			ebmHandSetList.add(handSetDetails);
			log.info("Handset Details are set.");
		}
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN) && 
				!(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getHandSetDetails().isEmpty())){
			
			List<CUSFLDHANDSET> ebmHandSetList = itemReference.getCUSFLDHANDSET();
			int i=0;
			
		
			CUSFLDHANDSET handSetDetails = new CUSFLDHANDSET();
			if(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getHandSetDetails().get(0)!=null){
				handSetDetails.setCUSFLDDEVICEMODEL(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getHandSetDetails().get(0).getDeviceMake());
				handSetDetails.setCUSFLDDEVICETYPE(new BigInteger(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getMobileService().getAddRemoveAddon().getHandSetDetails().get(0).getDeviceType()));
				handSetDetails.setElem(new BigInteger(i+""));
				ebmHandSetList.add(handSetDetails);
			}
			
			
			
			log.info("Handset Details are set.");
		}
		
		
		
		
	}


	/**
	 * This method sets specification extensible attribute  on the order line
	 * 
	 * Input arguments :  SpecificationGroup specificationGroup, String orderAction, OrderEntryRequest request, 
			OepOrder oepOrderDO, OepOrderLine oepOrderLineDO, String oepOrderNumber, String productSpec, Map<String,OEPConfigPlanAttributesT> productSpecMap			
	 * 
	 * Output arguments : void
	 * @param searchResult 
	 * 
	 * 
	 **/
	
	private void setSpecificationGroupDetails(final SalesOrderLine salesOrderLine,final SpecificationGroup specificationGroup, final String orderAction, final OrderEntryRequest request, 
			final OepOrder oepOrderDO, final OepOrderLine oepOrderLineDO, final String oepOrderNumber, final String productSpec, final Map<String,OEPConfigPlanAttributesT> productSpecMap, RESULTS result,List<String> planPackage,ProducerTemplate template){
		
		specificationGroup.setName(OEPConstants.OSM_EXTENSIBLE_ATTRIBUTE);
		
		List<Specification> specificationsList = specificationGroup.getSpecification(); 
	
		String planAction = oepOrderLineDO.getPlanAction();
		String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, orderAction);
		
		if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)
				|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)){
					
			String oldServiceNo = ProcessModifyOrderEbmHelper.getOldServiceNo(request, orderAction);
			String serviceNo = ProcessModifyOrderEbmHelper.getServiceNo(request, orderAction);
			String msisdnChangeReason = ProcessModifyOrderEbmHelper.getMsisdnChangeReason(request, orderAction);
			String options = ProcessModifyOrderEbmHelper.getWaiverOptions(request, orderAction);
			String waiverReason = ProcessModifyOrderEbmHelper.getWaiverReason(request, orderAction);
			
			String niceCategory = ProcessModifyOrderEbmHelper.getNiceCategory(request, orderAction);
			
			if(niceCategory!=null && niceCategory.equalsIgnoreCase("1")){	
							
				Specification niceCategorySpec = new Specification();
				niceCategorySpec.setName(OEPConstants.NICE_CATEGORY);
				niceCategorySpec.setValue(ProcessModifyOrderEbmHelper.getNiceCategoryValue(request, orderAction));				
				specificationsList.add(niceCategorySpec);
			}
			
						
			Specification oldMsisdnSpec = new Specification();
			oldMsisdnSpec.setName(OEPConstants.OLD_MSISDN);
			oldMsisdnSpec.setValue(oldServiceNo);				
			specificationsList.add(oldMsisdnSpec);
			
			Specification msisdnChangeReasonSpec = new Specification();
			msisdnChangeReasonSpec.setName(OEPConstants.REASON_MSISDN_CHANGE);
			msisdnChangeReasonSpec.setValue(msisdnChangeReason);				
			specificationsList.add(msisdnChangeReasonSpec);
			
			Specification waiverOptionsSpec = new Specification();
			waiverOptionsSpec.setName(OEPConstants.WAIVER_OPTIONS);
			waiverOptionsSpec.setValue(options);				
			specificationsList.add(waiverOptionsSpec);
			
			if(options.equalsIgnoreCase(OptionsEnumType.WAIVE_OFF.value())) {
				Specification waiverReasonSpec = new Specification();
				waiverReasonSpec.setName(OEPConstants.WAIVER_REASON);
				waiverReasonSpec.setValue(waiverReason);				
				specificationsList.add(waiverReasonSpec);
			}
			

			
		} 
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_SIM_CHANGE)
				|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_SIM_CHANGE)){
			
			SIMDetailsType simdetails = ProcessModifyOrderEbmHelper.getSimDetails(request, orderAction);
			String imsi = null;
			String newSim = null;
			String simType = null;
			
			if(simdetails != null) {
				imsi = simdetails.getImsi();
				newSim = simdetails.getSimNo();
				if(simdetails.getSimType()!=null)
					simType = simdetails.getSimType().value();
				else
					simType = SimTypeEnum.USIM.value();
			}
			
			String oldSim = ProcessModifyOrderEbmHelper.getOldSim(request, orderAction);
			String options = ProcessModifyOrderEbmHelper.getWaiverOptions(request, orderAction);
			String waiverReason = ProcessModifyOrderEbmHelper.getWaiverReason(request, orderAction);
			String resonSimChange = ProcessModifyOrderEbmHelper.getSimChangeReason(request, orderAction);
			
			
			Specification imsiSpec = new Specification();
			imsiSpec.setName(OEPConstants.IMSI);
			imsiSpec.setValue(imsi);				
			specificationsList.add(imsiSpec);
			
			Specification oldSimSpec = new Specification();
			oldSimSpec.setName(OEPConstants.OLD_ICCID);
			oldSimSpec.setValue(oldSim);				
			specificationsList.add(oldSimSpec);
			
			Specification newSimSpec = new Specification();
			newSimSpec.setName(OEPConstants.ICCID);
			newSimSpec.setValue(newSim);				
			specificationsList.add(newSimSpec);
			
			Specification simTypeSpec = new Specification();
			simTypeSpec.setName(OEPConstants.SIMTYPE);
			simTypeSpec.setValue(simType);				
			specificationsList.add(simTypeSpec);
			
			Specification resonSimChangeSpec = new Specification();
			resonSimChangeSpec.setName(OEPConstants.REASON_SIM_CHANGE);
			resonSimChangeSpec.setValue(resonSimChange);				
			specificationsList.add(resonSimChangeSpec);
			
			Specification waiverOptionsSpec = new Specification();
			waiverOptionsSpec.setName(OEPConstants.WAIVER_OPTIONS);
			waiverOptionsSpec.setValue(options);				
			specificationsList.add(waiverOptionsSpec);
			
			if(options.equalsIgnoreCase(OptionsEnumType.WAIVE_OFF.value())) {
				Specification waiverReasonSpec = new Specification();
				waiverReasonSpec.setName(OEPConstants.WAIVER_REASON);
				waiverReasonSpec.setValue(waiverReason);				
				specificationsList.add(waiverReasonSpec);
			}
			
			
		}
		
		
		if(orderAction.equals(OEPConstants.OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION)) {
			
		
		Specification planId = new Specification();				
		planId.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_PLAN_ID);		
		planId.setValue(productSpecMap.get(oepOrderLineDO.getPlanName()).getCos());
		specificationsList.add(planId);
		}
		if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION)
				   ||orderAction.equals(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)){
			
			
			Specification isTouristKit = new Specification();			
			OEPConfigPlanAttributesT config = productSpecMap.get(oepOrderLineDO.getPlanName());
			isTouristKit.setName(OEPConstants.IS_TOURIST);
			if (config.getTouristKit().equals(BigDecimal.ZERO)) {
				isTouristKit.setValue(OEPConstants.OEP_FALSE);
			} else if (config.getTouristKit().equals(BigDecimal.ONE)) {
				isTouristKit.setValue(OEPConstants.OEP_TRUE);
			} else {
				isTouristKit.setValue(OEPConstants.OEP_FALSE);
			}
			
			if(!oepOrderLineDO.getServiceType().equalsIgnoreCase(OEPConstants.OEP_BB_SERVICE_TYPE))
				specificationsList.add(isTouristKit);
		}
		
		if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE))
		{
			
			Specification shortCodeNumber = new Specification();				
			shortCodeNumber.setName(OEPConstants.SHORT_CODE_NUMBER);		
			shortCodeNumber.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getShortCodeNumber());
			specificationsList.add(shortCodeNumber);
			
			Specification shortCodeType = new Specification();				
			shortCodeType.setName(OEPConstants.SHORT_CODE_TYPE);		
			shortCodeType.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getShortCodeType());
			specificationsList.add(shortCodeType);				
			
			Specification shortCodeCategory = new Specification();				
			shortCodeCategory.setName(OEPConstants.SHORT_CODE_CATEGORY);		
			shortCodeCategory.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangeShortCodeNumber().getShortCodeCategory());
			specificationsList.add(shortCodeCategory);	
		}
		else if(orderAction.equals(OEPConstants.OEP_SHORTCODE_CHANGEPLAN_ACTION))
		{
			
			Specification shortCodeNumber = new Specification();				
			shortCodeNumber.setName(OEPConstants.SHORT_CODE_NUMBER);		
			shortCodeNumber.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getShortCodeNumber());
			specificationsList.add(shortCodeNumber);
			
			Specification shortCodeType = new Specification();				
			shortCodeType.setName(OEPConstants.SHORT_CODE_TYPE);		
			shortCodeType.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getShortCodeType());
			specificationsList.add(shortCodeType);				
			
			Specification shortCodeCategory = new Specification();				
			shortCodeCategory.setName(OEPConstants.SHORT_CODE_CATEGORY);		
			shortCodeCategory.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getChangePlan().getShortCodeCategory());
			specificationsList.add(shortCodeCategory);	
		}
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_SCS_ADD_REMOVE_PLAN))
		{
			
			Specification shortCodeNumber = new Specification();				
			shortCodeNumber.setName(OEPConstants.SHORT_CODE_NUMBER);		
			shortCodeNumber.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getShortCodeService().getAddRemoveAddon().getShortCodeNumber());
			specificationsList.add(shortCodeNumber);
			
		}
		
		else if(orderAction.equals(OEPConstants.OEP_MODIFY_MOBILE_MSISDN_CHANGE)
				|| orderAction.equals(OEPConstants.OEP_MODIFY_BB_MSISDN_CHANGE)){
			
			Specification msisdnSpec = new Specification();
			msisdnSpec.setName(OEPConstants.MSISDN);
			msisdnSpec.setValue(ProcessModifyOrderEbmHelper.getServiceNo(request, orderAction));				
			specificationsList.add(msisdnSpec);
		}
		else
		{
		Specification msisdnSpec = new Specification();
		msisdnSpec.setName(OEPConstants.MSISDN);
		msisdnSpec.setValue(oepOrderDO.getServiceNo());				
		specificationsList.add(msisdnSpec);
		}
		
		log.info("OEP Order Number : "+oepOrderNumber+". MSISDN Specification with value "+oepOrderDO.getServiceNo()+" is set.");
		
		if(orderAction.equals(OEPConstants.OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION))
		{
			if(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getContactNo()!=null)
			{
				Specification contactNo = new Specification();
				contactNo.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_CONTACT_NUMBER);
				contactNo.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getContactNo());				
				specificationsList.add(contactNo);
				
			}
			if(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getEmail()!=null)
			{
				Specification emailId = new Specification();
				emailId.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_EMAIL_ID);
				emailId.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getEmail());				
				specificationsList.add(emailId);
				
			}
			if(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getId()!=null)
			{
				Specification id = new Specification();
				id.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_ID);
				id.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getId());				
				specificationsList.add(id);
				
			}
			if(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getIdType()!=null)
			{
				Specification idType = new Specification();
				idType.setName(OEPConstants.OEP_BULK_SMS_SPEC_PARAM_ID_TYPE);
				idType.setValue(request.getProcessModifyOrder().getListOfOEPOrder().getOEPOrder().getBulkSMSService().getChangeDetails().getIdType().value().toString());				
				specificationsList.add(idType);
				
			}
		
		
		}
		
		
		
		if(result.getPAYTYPE() != null
				&& !orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)
				&& !orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
			
			log.info("Pay Type of the Service -- "+result.getPAYTYPE());
			Specification serviceTypeSpec= new Specification();
			serviceTypeSpec.setName(OEPConstants.SERVICE_TYPE);
			
			if(result.getPAYTYPE().equals(OEPConstants.SERVICE_TYPE_PREPAID_BRM)){
				
				serviceTypeSpec.setValue(OEPConstants.SERVICE_TYPE_PREPAID);
			} 
			
			else if(result.getPAYTYPE().equals(OEPConstants.SERVICE_TYPE_POSTPAID_BRM)){
				
				serviceTypeSpec.setValue(OEPConstants.SERVICE_TYPE_POSTPAID);
				
			}
			
			else if(result.getPAYTYPE().equals(OEPConstants.SERVICE_TYPE_POSTPAID_FLEX_BRM)){
				
				serviceTypeSpec.setValue(OEPConstants.SERVICE_TYPE_POSTPAID);
			}
			
			else{
				
					serviceTypeSpec.setValue(result.getPAYTYPE());
			}
			
				specificationsList.add(serviceTypeSpec);
		}
		
			
		if(isBasePlan(oepOrderLineDO) && !(oepOrderLineDO.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_MODIFY_ACTION_CODE))){		
			
					
			if(orderAction.equals(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION)){
				
				Specification package_id= new Specification();				
				package_id.setName(OEPConstants.PACKAGE_ID);
				package_id.setValue(result.getPACKAGEID());

				specificationsList.add(package_id);
			}	
		}
		
		if(oepOrderLineDO.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE)
				&& !orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)){			
					
				Specification package_id= new Specification();				
				package_id.setName(OEPConstants.PACKAGE_ID);
				package_id.setValue(ProcessModifyOrderEbmHelper.getAddOnPackageId(request, result, oepOrderLineDO.getPlanName(),planPackage));
				specificationsList.add(package_id);
			
		}
		
		if((oepOrderLineDO.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE) 
			|| oepOrderLineDO.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_ADD_ACTION_CODE))
			&& productSpecMap.get(oepOrderLineDO.getPlanName()).getConfigFeaturesTs().size()>0 ){			
						
			for (OEPConfigFeaturesT feature : productSpecMap.get(oepOrderLineDO.getPlanName()).getConfigFeaturesTs()){
				
				if(feature.getParamName().equals(OEPConstants.VAS_NAME)){
					Specification vasType = new Specification();
					vasType.setName(OEPConstants.VAS_NAME);	
					vasType.setValue(feature.getParamValue());
					specificationsList.add(vasType);
				}
			}			
			
		}
		
		log.info("Before Flex Plan Check --");
		if((productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BASEPLAN_PS)
				||productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BB_PS)) && (planAction.equalsIgnoreCase(OEPConstants.OSM_CHANGEPLAN_ACTION_CODE))){
			
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
		
		
		log.info("Before COS ID spec need to set --");
		if (((productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BASEPLAN_PS)
				|| productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BB_PS))
				&& (planAction.equalsIgnoreCase(OEPConstants.OSM_CHANGEPLAN_ACTION_CODE)))
				|| productSpec.equalsIgnoreCase(OEPConstants.MOBILE_DATA_PS)
				|| productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BB_ADDON_PS)
				|| (productSpec.equalsIgnoreCase(OEPConstants.MOBILE_BASEPLAN_PS)
						&& ((orderAction.equalsIgnoreCase(OEPConstants.OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN)
								|| orderAction.equalsIgnoreCase(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION) ))
						&& isOnlyPowerRemovePlan(oepOrderDO, productSpecMap))) {
			log.info("Entered ---COS ID spec need to set --");
			if(!orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)) {
				

				
				log.info("Inside block of COS ID spec need to set --");
				
				
				
				ListIterator<Map<String, Object>> cosIdlistItr =queryCosIdParams(oepOrderLineDO.getPlanName(),template);

		        while(cosIdlistItr.hasNext()){              
		   				 
		            		Map<String, Object> cosmap = cosIdlistItr.next();            					
		            						 
		            		
								 
		            			Object cosIdentifierName=cosmap.get("TYPE");
								 
		            			Object cosIdValue=cosmap.get("UNIQUE_ID");
								 
								 
								 
		            			log.debug(cosIdentifierName +" : "+cosIdValue);							 

		            			Specification cosIDSpec = new Specification();

		            			if(cosIdentifierName!=null && cosIdValue!=null)
		            			{	
		            				if (cosIdentifierName.equals("PRIMARY")){
		            					cosIDSpec.setName(OEPConstants.PCOS_ID);
		            					cosIDSpec.setValue(cosIdValue.toString());
		            					specificationsList.add(cosIDSpec);
		            					log.debug("PCOS Set --");
		            					/*
		            					 * If Power/Power Plus Add On and Action is Remove then set provisioning flag to false
		            					 */		            				
		            					
		            					if(oepOrderLineDO.getPlanAction().equalsIgnoreCase(OEPConstants.OSM_REMOVE_ACTION_CODE))
		            					{
		            						salesOrderLine.setProvisionableLineItem("false");
		            					}
		            				}else if (cosIdentifierName.equals("SECONDARY")){
		            					cosIDSpec.setName(OEPConstants.SCOS_ID);
		            					cosIDSpec.setValue(cosIdValue.toString());
		            					specificationsList.add(cosIDSpec);
		            					log.debug("SCOS Set --");
		            				}else{
		            					log.debug("No COS Set --");
		            				}
		            			}
		            			else
		            			{
		            				log.debug("No COS Set --");
		            			} 				
							 
		        }
				
			
			}
		}
		
		// Manage CUG Services
				if(orderAction.equals(OEPConstants.OEP_MOBILE_SERVICE_CUG)) {
					log.info("Generating Specification Group for "+OEPConstants.OEP_MOBILE_SERVICE_CUG);
					// Cug Action
					Specification cugAction = new Specification();				
					cugAction.setName(OEPConstants.OEP_MOBILE_SERVICE_CUG_ACTION_PARAM);	
					cugAction.setValue(request.getProcessCugOrder().getCugAction());
					
					
					// CUG ID
					Specification cugId = new Specification();
					cugId.setName(OEPConstants.OEP_MOBILE_SERVICE_CUG_REQUEST_PARAM_CUG_ID);	
					cugId.setValue(request.getProcessCugOrder().getCugId());
					
					// CUG Add
					log.info("Generating Specification Group for "
							+OEPConstants.OEP_MOBILE_SERVICE_CUG+" Action "
							+OEPConstants.OEP_MOBILE_SERVICE_CUG_ADD_ACTION);
					specificationsList.add(cugAction);
					specificationsList.add(cugId);
					
					
					// Cug Short Number Actions
					if(request.getProcessCugOrder().getShortNumberAction()!=null 
							&& request.getProcessCugOrder().getShortNumber()!=null) {
						
						log.info("Generating Specification Group for "
								+OEPConstants.OEP_MOBILE_SERVICE_CUG+" ShortNumber Action "
								+request.getProcessCugOrder().getShortNumberAction());
					
						
						// Cug ShortNumber Action
						Specification cugShortNumberAction = new Specification();				
						cugShortNumberAction.setName(OEPConstants.OEP_MOBILE_SERVICE_CUG_REQUEST_PARAM_SHORT_NUMBER_ACTION);	
						cugShortNumberAction.setValue(request.getProcessCugOrder().getShortNumberAction());
						specificationsList.add(cugShortNumberAction);
						
						// Cug Short Number
						Specification cugShortNumber = new Specification();				
						cugShortNumber.setName(OEPConstants.OEP_MOBILE_SERVICE_CUG_REQUEST_PARAM_SHORT_NUMBER);	
						cugShortNumber.setValue(request.getProcessCugOrder().getShortNumber());
						specificationsList.add(cugShortNumber);
						
						// Cug OldShortNumber
						if(request.getProcessCugOrder().getShortNumberAction().equals(OEPConstants.OEP_MOBILE_SERVICE_CUG_SHORT_NUMBER_MODIFY_ACTION)
								&& request.getProcessCugOrder().getOldShortNumber()!=null) {
							Specification cugOldShortNumber = new Specification();				
							cugOldShortNumber.setName(OEPConstants.OEP_MOBILE_SERVICE_CUG_REQUEST_PARAM_OLD_SHORT_NUMBER);	
							cugOldShortNumber.setValue(request.getProcessCugOrder().getOldShortNumber());
							specificationsList.add(cugOldShortNumber);
						}
						
					}
					
				} // MobileService-CUG
				
				// FCA
				if(orderAction.equals(OEPConstants.OEP_FCA_MOBILE_ACTION)){
					Specification firstUsageActivation = new Specification();				
					firstUsageActivation.setName(OEPConstants.OEP_FCA_MOBILE_PARAM_FIRST_USAGE_ACTIVATION);		
					firstUsageActivation.setValue(OEPConstants.TRUE);
					specificationsList.add(firstUsageActivation);
				}
				
		
		
				
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
	 * This method sets deposit details on the order line
	 * 
	 * Input arguments :  ItemReference itemReference, List<DepositPaymentType> depositPaymets, OrderEntryRequest request, String orderAction, Map<String,OEPConfigPlanAttributesT> productSpecMap				
	 * 
	 * Output arguments : void
	 * 
	 * 
	 **/
	private void setDeposits(final ItemReference itemReference, final List<DepositPaymentType> depositPaymets, final OrderEntryRequest request, final String orderAction,final Map<String,OEPConfigPlanAttributesT> productSpecMap){
		  
				log.info("---------set Deposits"+itemReference);	
		List<DEPOSITS> ebmDeposistsList = itemReference.getDEPOSITS();
		
		int i=0;
		
		for (DepositPaymentType depositPaymet : depositPaymets){
			
			String elem=i+"";
			String planName = depositPaymet.getPlanName();			
			DEPOSITS deposits = new DEPOSITS();
			
			deposits.setRECEIPTNO(depositPaymet.getDepositReceiptNo());
			
			deposits.setCUSFLDSERVICETYPE(depositPaymet.getServiceType().toString());
			log.info("---------Collection date"+depositPaymet.getCollectedOn());
			deposits.setCUSFLDCOLLECTEDON(OrderDataUtils.getTimeStampFromCalender(depositPaymet.getCollectedOn()).getTime()/1000L);			
			deposits.setCUSFLDWAIVERREASON(depositPaymet.getWaveOffReason());			
			deposits.setAMOUNT(depositPaymet.getDepositAmount());
			if(depositPaymet.getWaveOff()!=null){
				
				deposits.setOPTIONS(new BigInteger(depositPaymet.getWaveOff()));
			}
			deposits.setTYPE(new BigInteger(depositPaymet.getDepositType()));	
			deposits.setTYPESTR(planName);
			deposits.setElem(new BigInteger(elem));
			i++;
			if(depositPaymet.getDepositWaiver() != null)
			{			
			deposits.setCUSFLDDEPOSITWAIVER(new BigDecimal(depositPaymet.getDepositWaiver()));
			}			
			ebmDeposistsList.add(deposits);
			//Could not able to map options and wave off reason
		}
		
		log.info("Deposits are set.");
		
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
		
		BigDecimal spendingCap = ProcessModifyOrderEbmHelper.getSpendingCap(request, orderAction);
		BigDecimal creditScore = ProcessModifyOrderEbmHelper.getCreditScores(request, orderAction);
		BigDecimal spendingCapMax = ProcessModifyOrderEbmHelper.getSpendingCapMax(request, orderAction);
		BigDecimal spendingCapMin = ProcessModifyOrderEbmHelper.getSpendingCapMin(request, orderAction);
			
		if(spendingCap!= null && creditScore != null && spendingCapMax !=null  && spendingCapMin !=null){
		  
			CUSFLDCREDITSCORES creditScores = new CUSFLDCREDITSCORES();
			creditScores.setCUSFLDCREDITSCORE(ProcessModifyOrderEbmHelper.getCreditScores(request, orderAction));
			creditScores.setCUSFLDSCMINVAL(ProcessModifyOrderEbmHelper.getSpendingCapMin(request, orderAction));
			creditScores.setCUSFLDSCMAXVAL(ProcessModifyOrderEbmHelper.getSpendingCapMax(request, orderAction));
			creditScores.setCUSFLDSCACTUALVAL(ProcessModifyOrderEbmHelper.getSpendingCap(request, orderAction));
			creditScores.setElem(new BigInteger("0"));
			
			itemReference.setCUSFLDCREDITSCORES(creditScores);
			
			
			log.info("Credit scores are set.");
		
		} 
		else{
			
			log.info("Credit scores are not set.");
		}
		
		
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
		
	private boolean isOnlyPowerRemovePlan(OepOrder oepOrderDO, Map<String,OEPConfigPlanAttributesT> productSpecMap){
		
		List<OepOrderLine> lines = new ArrayList<OepOrderLine>();
		
		
		
		for(OepOrderLine line : oepOrderDO.getOepOrderLines()){
			
			if(line.getParentOrderLineId()!=line.getOrderLineId()){
				
				lines.add(line);
			}
			
		}
		
		int count = 0;
		
		for(OepOrderLine line:lines){
			
			String planName= line.getPlanName();
			
			if(line.getPlanAction().equals(OEPConstants.OSM_ADD_ACTION_CODE)){
				
				if(("POWER" .equalsIgnoreCase(productSpecMap.get(planName).getAddonPkgSubType()) 
						||"POWERPLUS" .equalsIgnoreCase(productSpecMap.get(planName).getAddonPkgSubType())))
				
					return false;				
				
			}
			
			else {
				
				
				if("POWER" .equalsIgnoreCase(productSpecMap.get(planName).getAddonPkgSubType()) 
						||"POWERPLUS" .equalsIgnoreCase(productSpecMap.get(planName).getAddonPkgSubType())){
					
					count++;
				}
				
			}
		}
		
		if(count>0){
			
			return true;
		}
		return false;
	}
}
