package com.oracle.oep.order.abm.transform;

import com.oracle.oep.brmorder.model.PrepaidLifecycleBarring;

public class ProcessPrepaidLifecycleBarringrAbmHelper {

	public static String getAccountno(final PrepaidLifecycleBarring request) {

		String accountno = null;

		accountno = request.getAccountNo();

		return accountno;

	}
	
	public static String getAccountObj(final PrepaidLifecycleBarring request) {

		String accountno = null;

		accountno = request.getAccountObj();

		return accountno;

	}
	
	public static String getBarringAction(final PrepaidLifecycleBarring request) {

		String barringAction = null;

		barringAction = String.valueOf(request.getBarringAction());

		return barringAction;

	}
	
	public static String getBarringLevel(final PrepaidLifecycleBarring request) {

		String barringLevel = null;

		barringLevel = String.valueOf(request.getBarringLevel());

		return barringLevel;

	}
	
	public static String getEmailAddr(final PrepaidLifecycleBarring request) {

		String emailAddr = null;

		emailAddr = request.getEmailAddr();

		return emailAddr;

	}
	
	public static String getContactNo(final PrepaidLifecycleBarring request) {

		String contactNo = null;

		contactNo = String.valueOf(request.getContactNo());

		return contactNo;

	}
	
	public static String getSuspendedIndicator(final PrepaidLifecycleBarring request) {

		String suspendedIndicator = null;

		suspendedIndicator = Byte.toString(request.getSuspendedIndicator());

		return suspendedIndicator;

	}
	
	public static String getPlanName(final PrepaidLifecycleBarring request) {

		String planName = null;

		planName = request.getPrepaidLifecycleBarringServices().getPlanName();

		return planName;

	}
	
	public static String getServiceIdentifier(final PrepaidLifecycleBarring request) {
		
		String serviceIdentifier = null;
		
		serviceIdentifier = request.getPrepaidLifecycleBarringServices().getServiceIdentifier();
		
		return serviceIdentifier;
	}
	
	public static String getMSISDN(final PrepaidLifecycleBarring request) {

		String MSISDN = null;

		MSISDN = String.valueOf(request.getPrepaidLifecycleBarringServices().getMSISDN());

		return MSISDN;

	}
	
	public static String getActionName(final PrepaidLifecycleBarring request) {

		String actionName = null;

		actionName = request.getPrepaidLifecycleBarringServices().getActionName();

		return actionName;

	}
	
	public static PrepaidLifecycleBarring.PrepaidLifecycleBarringServices getServices(final PrepaidLifecycleBarring request) {

		return request.getPrepaidLifecycleBarringServices();
	}

}
