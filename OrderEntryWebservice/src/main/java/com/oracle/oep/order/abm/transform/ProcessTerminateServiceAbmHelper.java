package com.oracle.oep.order.abm.transform;

import com.oracle.oep.brmorder.model.TerminateService;

public class ProcessTerminateServiceAbmHelper {

	public static String getAccountno(final TerminateService request) {

		return request.getAccountNo();

	}
	
	
	public static String getServiceIdentifier(final TerminateService request) {
		
		return request.getTerminateServiceInfo().getMSISDN();
	}
	
	public static String getOrderStartDate(final TerminateService request) {
		
		return request.getTerminateServiceInfo().getOrderStartDate();
	}
	
}
