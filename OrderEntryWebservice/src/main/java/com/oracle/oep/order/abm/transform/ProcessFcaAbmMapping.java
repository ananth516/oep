package com.oracle.oep.order.abm.transform;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brmorder.model.FCA;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.FcaOrderType;
import com.oracle.oep.order.model.ListOfOEPOrderModifyType;
import com.oracle.oep.order.model.ModifyOrderMobileServiceType;
import com.oracle.oep.order.model.ModifyOrderServiceType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ProcessModifyOrderType;
import com.oracle.oep.order.utils.OrderDataUtils;

public class ProcessFcaAbmMapping {
	public static final Logger log = LogManager.getLogger(ProcessFcaAbmMapping.class);

	public OrderEntryRequest createOrderEntryRequest(final FCA request, String brmOrderAction,
			final String brmJmsTimeStamp) {

		log.info("createOrderEntryRequest() for FCA." + request);

		OrderEntryRequest oeprequest = new OrderEntryRequest();
		
		FcaOrderType fcaOrderType = new FcaOrderType();
		log.info("New object" + ProcessFcaAbmHelper.getAccountno(request));
		fcaOrderType.setAccountNo(ProcessFcaAbmHelper.getAccountno(request));
		fcaOrderType.setPlanName(ProcessFcaAbmHelper.getPlanName(request));
		fcaOrderType.setServiceIdentifier(ProcessFcaAbmHelper.getServiceIdentifier(request));
		fcaOrderType.setMsIsdn(ProcessFcaAbmHelper.getMsisdn(request));
		fcaOrderType.setOrderAction(ProcessFcaAbmHelper.getOrderAction(request));
		
		// Setting current date to OrderStartDate.
		try {
			GregorianCalendar currentDate = new GregorianCalendar();
			currentDate.setTime(new Date());
			XMLGregorianCalendar xmlDate;
			xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(currentDate);
			fcaOrderType.setOrderStartDate(xmlDate);
		} catch (DatatypeConfigurationException e) {
			log.error("Error setting current date to OrderStartDate for BRM FCA request.");
			e.printStackTrace();
		}
		
		fcaOrderType.setChannelTrackingId(brmJmsTimeStamp);
		fcaOrderType.setChannelName(OEPConstants.BRM_CHANNEL_NAME);

		// MobileService
		ModifyOrderMobileServiceType mobileServiceType = new ModifyOrderMobileServiceType();
		mobileServiceType.setFca(fcaOrderType);
		
		ModifyOrderServiceType oepOrder=new ModifyOrderServiceType();
		oepOrder.setMobileService(mobileServiceType);
		
		ListOfOEPOrderModifyType listOfOepOrderType = new ListOfOEPOrderModifyType();
		listOfOepOrderType.setOEPOrder(oepOrder);
		
		ProcessModifyOrderType modifyOrderType = new ProcessModifyOrderType();
		modifyOrderType.setListOfOEPOrder(listOfOepOrderType);
		
		oeprequest.setProcessModifyOrder(modifyOrderType);
		
		return oeprequest;

	}

}
