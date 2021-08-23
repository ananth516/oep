package com.oracle.oep.order.abm.transform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brmorder.model.TerminateService;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.model.DisconnectOrderMobileServiceType;
import com.oracle.oep.order.model.DisconnectOrderServiceType;
import com.oracle.oep.order.model.ListOfOEPOrderDisconnectType;
import com.oracle.oep.order.model.OrderEntryRequest;
import com.oracle.oep.order.model.ProcessDisconnectOrderType;
import com.oracle.oep.order.model.SuspendDisconnectOrderAllServiceType;

public class ProcessTerminateServiceAbmMapping {
	public static final Logger log = LogManager.getLogger(ProcessTerminateServiceAbmMapping.class);

	public OrderEntryRequest createOrderEntryRequest(final TerminateService request, String brmOrderAction,
			final String brmJmsTimeStamp) {

		log.info("createOrderEntryRequest() for FCA." + request);

		OrderEntryRequest oeprequest = new OrderEntryRequest();
		
		DisconnectOrderMobileServiceType  disconnectOrderMobileServiceType = new DisconnectOrderMobileServiceType();
		log.info("New object" + ProcessTerminateServiceAbmHelper.getAccountno(request));
		disconnectOrderMobileServiceType.setEarlyTerminationFlag(OEPConstants.OEP_CONSTANT_Y);
		disconnectOrderMobileServiceType.setPortType("1");
		disconnectOrderMobileServiceType.setTerminationCharge("0");
		disconnectOrderMobileServiceType.setWaveOff("1");
		disconnectOrderMobileServiceType.setWaveOffReason("TerminationService");
		
		
		SuspendDisconnectOrderAllServiceType suspendDisconnectOrderAllServiceType = new SuspendDisconnectOrderAllServiceType();
		suspendDisconnectOrderAllServiceType.setAccountNo(ProcessTerminateServiceAbmHelper.getAccountno(request));
		suspendDisconnectOrderAllServiceType.setChannelTrackingId(brmJmsTimeStamp);
		suspendDisconnectOrderAllServiceType.setOrderAction(OEPConstants.OEP_DISCONNECT_MOBILE_SERVICE_ACTION);
		suspendDisconnectOrderAllServiceType.setOrderName(OEPConstants.BRM_PREPAID_LIFECYCLE_TERMINATE_SERVICE_ACTION);
		suspendDisconnectOrderAllServiceType.setOrderUserId(OEPConstants.BRM_USER_ID);
		suspendDisconnectOrderAllServiceType.setReason(OEPConstants.BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION);
		suspendDisconnectOrderAllServiceType.setServiceIdentifier(ProcessTerminateServiceAbmHelper.getServiceIdentifier(request));
	
		
		disconnectOrderMobileServiceType.setServiceDetails(suspendDisconnectOrderAllServiceType);
		
		// Setting current date to OrderStartDate.
		try {
			
			DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			Date date = null;
			try {
				date = (Date)formatter.parse(ProcessTerminateServiceAbmHelper.getOrderStartDate(request));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			GregorianCalendar gregOrderStartDate = new GregorianCalendar();
			gregOrderStartDate.setTime(date);
			XMLGregorianCalendar xmlDate;
			xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregOrderStartDate);
			suspendDisconnectOrderAllServiceType.setOrderStartDate(xmlDate);
			
		} catch (DatatypeConfigurationException e) {
			log.error("Error setting current date to OrderStartDate for BRM FCA request.");
			e.printStackTrace();
		}
		

		// MobileService Disconnect
		DisconnectOrderServiceType disconnectOrderServiceType=new DisconnectOrderServiceType();
		disconnectOrderServiceType.setMobileService(disconnectOrderMobileServiceType);
		
		ListOfOEPOrderDisconnectType listOfOEPOrderDisconnectType = new ListOfOEPOrderDisconnectType();
		listOfOEPOrderDisconnectType.setOEPOrder(disconnectOrderServiceType);
		
		ProcessDisconnectOrderType mobileDisconnectOrderType = new ProcessDisconnectOrderType();
		mobileDisconnectOrderType.setListOfOEPOrder(listOfOEPOrderDisconnectType);
		
		oeprequest.setProcessDisconnectOrder(mobileDisconnectOrderType);
		
		return oeprequest;

	}

}
