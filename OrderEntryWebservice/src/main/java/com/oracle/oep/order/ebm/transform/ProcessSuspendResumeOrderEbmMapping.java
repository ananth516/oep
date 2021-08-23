package com.oracle.oep.order.ebm.transform;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.presistence.model.OepOrderLine;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.ClassificationCode;
import com.oracle.oep.osm.order.model.CreateOrder;
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


public class ProcessSuspendResumeOrderEbmMapping {
	
	public static final Logger log = LogManager.getLogger(ProcessSuspendResumeOrderEbmMapping.class);
	
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
			final Map<String,OEPConfigPlanAttributesT> productSpecMap, final RESULTS searchResult){
		
		
		log.info("OEP Order number : "+oepOrderNumber+". Started EBM tranformation");
		
		CreateOrder createSalesOrder = new CreateOrder();		
		ProcessSalesOrderFulfillmentEBM salesOrderEBM = new ProcessSalesOrderFulfillmentEBM();
		DataArea dataArea = new DataArea();
		ProcessSalesOrderFulfillment salesOrderFulfillment = new ProcessSalesOrderFulfillment();		
		
		
		try{			
			//set headers values under ProcessSalesOrderFulfillment structure 
			setSalesOrderFulFillmentHeaders(request, oepOrderDO, orderAction, oepOrderNumber, salesOrderFulfillment);
			setSalesOrderLines(oepOrderDO, request, orderAction, oepOrderNumber, salesOrderFulfillment, productSpecMap, searchResult);
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
		if(orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION))
		{
		salesOrderFulfillment.setRequestedDeliveryDateTime(OrderDataUtils.getXMLGregorianCalendarFromTimeStamp(new Date()));
		}
		salesOrderFulfillment.setTypeCode(OEPConstants.SALES_ORDER_TYPE_CODE);
		salesOrderFulfillment.setFulfillmentPriorityCode(new BigInteger(OEPConstants.OSM_ORDER_PRIORITY_CODE_DEFAULT));
		salesOrderFulfillment.setFulfillmentSuccessCode(OEPConstants.FULFILLMENT_SUCCESS_CODE);
		salesOrderFulfillment.setFulfillmentModeCode(OEPConstants.FULFILLMENT_MODE_CODE_DELIVER);
		salesOrderFulfillment.setStatus(osmOrderHeaderStatus);
		salesOrderFulfillment.setOrderSource(ProcessSuspendResumeOrderEbmHelper.getChannelName(request, orderAction));
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
			final OepOrderLine oepOrderLineDO, final OepOrder oepOrderDO, final String lineId, final String orderAction, final RESULTS searchResult){		
		
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
		
		salesOrderLine.setAccountId(searchResult.getACCOUNTNO());	
		salesOrderLine.setBillingAccountId(searchResult.getCUSFLDBAACCOUNTNO());
		salesOrderLine.setCustomerAccountId(searchResult.getCUSFLDCAACCOUNTNO());
		
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
			final String oepOrderNumber, final ProcessSalesOrderFulfillment salesOrderFulfillment, final Map<String,OEPConfigPlanAttributesT> productSpecMap, final RESULTS searchResult){
		
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
			
			salesOrderLine.setServiceActionCode(oepOrderLineDO.getPlanAction());
			
			//Call setSalesOrderLineCommonParams to set common order parameters on SalesOrderLine
			
			setSalesOrderLineCommonParams(salesOrderLine, request, oepOrderLineDO, oepOrderDO, lineId, orderAction, searchResult);
			
			//SalesOrderSchedule
			
			SalesOrderSchedule salesOrderSchedule = new SalesOrderSchedule();
			
			try {
				
				if(orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION)
						|| orderAction.equalsIgnoreCase(OEPConstants.OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION))
				{
					salesOrderSchedule.setRequestedDeliveryDateTime(OrderDataUtils.getXMLGregorianCalendarFromTimeStamp(new Date()));					
				}
				else
				{	
					salesOrderSchedule.setRequestedDeliveryDateTime(OrderDataUtils.getXMLGregorianCalendarFromTimeStamp((oepOrderDO.getOrderStartDate())));
				}
				
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
			
			String suspendReason = ProcessSuspendResumeOrderEbmHelper.getSuspendReason(request, orderAction);
			itemReference.setSuspendReason(suspendReason);
			
			if(isBasePlan(oepOrderLineDO)){
				
				log.info("OEP Order Number : "+oepOrderNumber+". Setting PlanType for BasePlan");
				itemReference.setPlanType(new BigInteger("1"));
				
			}
			else {
				
				log.info("OEP Order Number : "+oepOrderNumber+". Setting PlanType for AddonPlan");
				itemReference.setPlanType(new BigInteger("2"));
			}
			
			itemReference.setServiceIdentifier(searchResult.getLOGIN());
			
			
			itemReference.setCsrId(oepOrderDO.getCsrid());
			itemReference.setNetworkIndicator(false);
			
			
			log.info("OEP Order Number : "+oepOrderNumber+". Initializing Specification Group for "+planName);
			
			SpecificationGroup specificationGroup = new SpecificationGroup();
			setSpecificationGroupDetails(specificationGroup, orderAction, request, oepOrderDO, oepOrderLineDO, oepOrderNumber, productSpec, productSpecMap,searchResult);
			
			itemReference.setSpecificationGroup(specificationGroup);							
			
			
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
	 * @param searchResult 
	 * 
	 * 
	 **/
	
	private void setSpecificationGroupDetails(final SpecificationGroup specificationGroup, final String orderAction, final OrderEntryRequest request, 
			final OepOrder oepOrderDO, final OepOrderLine oepOrderLineDO, final String oepOrderNumber, final String productSpec, final Map<String,OEPConfigPlanAttributesT> productSpecMap, RESULTS searchResult){
		
		specificationGroup.setName(OEPConstants.OSM_EXTENSIBLE_ATTRIBUTE);
		
		List<Specification> specificationsList = specificationGroup.getSpecification(); 
	
		
		Specification msisdnSpec = new Specification();
		msisdnSpec.setName(OEPConstants.MSISDN);
		msisdnSpec.setValue(oepOrderDO.getServiceNo());				
		specificationsList.add(msisdnSpec);
		
		String planAction = oepOrderLineDO.getPlanAction();
		
		if(planAction.equals(OEPConstants.OSM_SUSPEND_ACTION_CODE)){
			
			String suspensionDuration = ProcessSuspendResumeOrderEbmHelper.getSuspensionDuration(request, orderAction);
			if(suspensionDuration!=null){
				
				Specification suspensionDurationSpec = new Specification();
				suspensionDurationSpec.setName(OEPConstants.SUSPENSION_DURATION);
				suspensionDurationSpec.setValue(suspensionDuration);				
				specificationsList.add(suspensionDurationSpec);
			}
			
			String suspensionDescription =  ProcessSuspendResumeOrderEbmHelper.getServiceSuspensionDescription(request, orderAction);
			if(suspensionDescription!=null){
				
				Specification suspensionDescriptionSpec = new Specification();
				suspensionDescriptionSpec.setName(OEPConstants.SERVICE_SUSPENSION_DURATION);
				suspensionDescriptionSpec.setValue(suspensionDescription);				
				specificationsList.add(suspensionDescriptionSpec);
			}
			
			String suspensionDealName =  ProcessSuspendResumeOrderEbmHelper.getSuspensionDealName(request, orderAction);
			if(suspensionDealName!=null){
				
				Specification suspensionDealNameSpec = new Specification();
				suspensionDealNameSpec.setName(OEPConstants.SERVICE_SUSPENSION_DEAL_NAME);
				suspensionDealNameSpec.setValue(suspensionDealName);				
				specificationsList.add(suspensionDealNameSpec);
			}
		}
		
		log.info("OEP Order Number : "+oepOrderNumber+". MSISDN Specification with value "+oepOrderDO.getServiceNo()+" is set.");	
		
				
	}	
		
}
