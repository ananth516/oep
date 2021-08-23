package com.oracle.oep.order.abm.transform;

import com.oracle.oep.brmorder.model.FCA;

public class ProcessFcaAbmHelper {

	public static String getAccountno(final FCA request) {

		String accountno = null;

		accountno = request.getAccountNo();

		return accountno;

	}
	
	public static String getAccountObj(final FCA request) {

		String accountno = null;

		accountno = request.getAccountObj();

		return accountno;

	}

	public static String getServiceIdentifier(final FCA request) {

		String serviceIdentifier = null;

		serviceIdentifier = request.getFcaInfo().getServiceIdentifier();

		return serviceIdentifier;
	}

	public static String getMsisdn(final FCA request) {

		String msisdn = null;

		msisdn = request.getFcaInfo().getMSISDN();

		return msisdn;
	}

	public static String getPlanName(final FCA request) {

		String planName = null;

		planName = request.getFcaInfo().getPlanName();

		return planName;
	}

	
	public static String getOrderAction(final FCA request) {
		
		String actionName = null;

		actionName = request.getFcaInfo().getActionName();

		return actionName;

	}

}
