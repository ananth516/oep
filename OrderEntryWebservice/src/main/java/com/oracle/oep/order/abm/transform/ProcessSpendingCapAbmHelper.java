package com.oracle.oep.order.abm.transform;

import com.oracle.oep.brmorder.model.SpendingCapAction;

public class ProcessSpendingCapAbmHelper {

	public static String getAccountno(final SpendingCapAction request) {

		String accountno = null;

		accountno = request.getAccountNo();

		return accountno;

	}

	public static String getBarringAction(final SpendingCapAction request) {

		String barringAction = null;

		barringAction = request.getBarringAction();

		return barringAction;

	}

	public static String getBarringLevel(final SpendingCapAction request) {

		String barringLevel = null;

		barringLevel = request.getBarringLevel();

		return barringLevel;
	}

	public static String getServiceIdentifier(final SpendingCapAction request) {

		String serviceIdentifier = null;

		serviceIdentifier = request.getServiceIdentifier();

		return serviceIdentifier;
	}

	public static String getMsisdn(final SpendingCapAction request) {

		String msisdn = null;

		msisdn = request.getMSISDN();

		return msisdn;
	}

	public static String getplanName(final SpendingCapAction request) {

		String planName = null;

		planName = request.getPlanName();

		return planName;
	}

	public static String getBarringType(final SpendingCapAction request) {

		String barringType = null;

		barringType = request.getBarringType();

		return barringType;
	}

	public static String getActionCode(final SpendingCapAction request) {
		String actionCode = null;

		actionCode = request.getActionCode();

		return actionCode;

	}

	public static String getNotifyContactsEmailAddress(final SpendingCapAction request) {

		String contactEmailAddress = null;

		contactEmailAddress = request.getSpendingNotifyContacts().get(0).getEmailAddress();

		return contactEmailAddress;
	}

	public static String getNotifyContactsPhone(final SpendingCapAction request) {

		String contactPhone = null;

		contactPhone = request.getSpendingNotifyContacts().get(0).getPhone();

		return contactPhone;
	}

}
