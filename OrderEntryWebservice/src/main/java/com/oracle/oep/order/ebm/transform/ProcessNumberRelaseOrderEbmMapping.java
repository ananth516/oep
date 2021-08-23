package com.oracle.oep.order.ebm.transform;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.presistence.model.OepOrder;
import com.oracle.oep.order.utils.OrderDataUtils;
import com.oracle.oep.osm.order.model.CreateOrder;
import com.oracle.oep.osm.order.model.DataArea;
import com.oracle.oep.osm.order.model.Identification;
import com.oracle.oep.osm.order.model.ProcessSalesOrderFulfillment;
import com.oracle.oep.osm.order.model.ProcessSalesOrderFulfillmentEBM;
import com.oracle.oep.osm.order.model.Revision;
import com.oracle.oep.osm.order.model.Status;

public class ProcessNumberRelaseOrderEbmMapping {
	public static final Logger log = LogManager.getLogger(ProcessNumberRelaseOrderEbmMapping.class);
	
	/**
	 * This method returns create order EBM object
	 * 
	 * Input arguments:  OrderEntryRequest request, OepOrder oepOrderDO, String orderAction, String oepOrderNumber, Map<String,OEPConfigPlanAttributesT> productSpecMap
	 * 
	 * Output argument : CreateOrder createSalesOrder
	 * 
	 *  
	 * **/
	
	public CreateOrder createOSMOrderEBM(final OrderEntryRequest request,final OepOrder oepOrderDO,final String orderAction,final String oepOrderNumber){
		
		
		log.info("OEP Order number : "+oepOrderNumber+". Started EBM tranformation");
		
		CreateOrder createSalesOrder = new CreateOrder();		
		ProcessSalesOrderFulfillmentEBM salesOrderEBM = new ProcessSalesOrderFulfillmentEBM();
		DataArea dataArea = new DataArea();
		ProcessSalesOrderFulfillment salesOrderFulfillment = new ProcessSalesOrderFulfillment();		
		
		
		try{			
			//set headers values under ProcessSalesOrderFulfillment structure 
			setSalesOrderFulFillmentHeaders(oepOrderDO, orderAction, oepOrderNumber, salesOrderFulfillment);
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
		Status osmOrderHeaderStatus = new Status();
			
		orderLevelIdentification.setID(oepOrderNumber);

		osmOrderHeaderStatus.setCode(OEPConstants.OEP_ORDER_STATUS_OPEN);
		
		salesOrderFulfillment.setIdentification(orderLevelIdentification);
		salesOrderFulfillment.setOrderDateTime(OrderDataUtils.getXMLGregorianCalendarFromTimeStamp(new Date()));
		salesOrderFulfillment.setTypeCode(OEPConstants.OEP_MNP_NUMBER_RELEASE);
		salesOrderFulfillment.setFulfillmentPriorityCode(new BigInteger(OEPConstants.OSM_ORDER_PRIORITY_CODE_DEFAULT));
		salesOrderFulfillment.setFulfillmentSuccessCode(OEPConstants.FULFILLMENT_SUCCESS_CODE);
		salesOrderFulfillment.setFulfillmentModeCode(OEPConstants.FULFILLMENT_MODE_CODE_DELIVER);
		salesOrderFulfillment.setNumberToBeReleased(oepOrderDO.getServiceNo());
		salesOrderFulfillment.setStatus(osmOrderHeaderStatus);

		
	}		
}
