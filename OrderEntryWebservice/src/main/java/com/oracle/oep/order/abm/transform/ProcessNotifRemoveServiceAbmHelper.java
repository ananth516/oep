package com.oracle.oep.order.abm.transform;

import com.oracle.oep.brmorder.model.NotifRemoveServiceNotification;

public class ProcessNotifRemoveServiceAbmHelper {

	public static String getAccountno(final NotifRemoveServiceNotification request) {

		return request.getAccountNo();

	}
	
	
	public static String getServiceIdentifier(final NotifRemoveServiceNotification request) {
		
		return request.getLogin();
	}
	
public static String getBasePackageName(final NotifRemoveServiceNotification request) {
		
		return request.getRemoveServiceEvents().getBasePackage();
	}

public static String getServiceNo(final NotifRemoveServiceNotification request) {
	
	return request.getServiceNo();
}

}
