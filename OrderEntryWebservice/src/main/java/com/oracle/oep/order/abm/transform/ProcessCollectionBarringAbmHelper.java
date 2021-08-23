package com.oracle.oep.order.abm.transform;

import java.util.List;

import com.oracle.oep.brmorder.model.CollectionsActionBarring;
import com.oracle.oep.brmorder.model.Collservices;
import com.oracle.oep.order.model.BarringServicesType;

public class ProcessCollectionBarringAbmHelper {
	

	public static String getAccountno(final CollectionsActionBarring request) {

		String accountno = null;

		accountno = request.getAccountNo();

		return accountno;

	}

	public static String getBarringAction(final CollectionsActionBarring request) {

		String barringAction = null;

		barringAction = request.getBarringAction();

		return barringAction;

	}

	public static String getBarringLevel(final CollectionsActionBarring request) {

		String barringLevel = null;

		barringLevel = request.getBarringLevel();

		return barringLevel;
	}

	

	
	
	public static String getNotifyContactsEmailAddress(final CollectionsActionBarring request) {

		String contactEmailAddress = null;

		contactEmailAddress = request.getCollectionNotifyContacts().get(0).getEmailAddress();

		return contactEmailAddress;
	}

	public static String getNotifyContactsPhone(final CollectionsActionBarring request) {

		String contactPhone = null;

		contactPhone = request.getCollectionNotifyContacts().get(0).getPhone();

		return contactPhone;
		
	}
	public static String getNotifyContactsFirstName(final CollectionsActionBarring request) {

		String contactPhone = null;

		contactPhone = request.getCollectionNotifyContacts().get(0).getFirstName();

		return contactPhone;
		
	}
	public static String getNotifyContactsLastName(final CollectionsActionBarring request) {

		String contactPhone = null;

		contactPhone = request.getCollectionNotifyContacts().get(0).getLastName();

		return contactPhone;
		
	}
	
//	public static BarringServicesType getBarringSerivce(final List<Collservices> collservices) {
//		
//		BarringServicesType barringServicesType = null;
//		
////		List<Collservices> collservices = request.getCollservices();
//		
//		//for (Collservices collServ : collservices)	{
//			
//			barringServicesType.setPlanName(collServ.getPlanName());
//			barringServicesType.setAction(collServ.getBarringAction());
//			barringServicesType.setServiceIdentifier(collServ.getMsisdn());
//			
//			
//		//}
//			
		
//		return barringServicesType;
	//}

}
