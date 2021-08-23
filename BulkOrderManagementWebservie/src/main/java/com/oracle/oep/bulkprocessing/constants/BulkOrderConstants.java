package com.oracle.oep.bulkprocessing.constants;

public class BulkOrderConstants {
	
	public static final String OEP_ORDER_INITIAL_STATUS = "UNPROCESSED";
	public static final String OEP_ORDER_SCHEDULED_STATUS = "SCHEDULED";
	public static final String OEP_ORDER_INPROGRESS_STATUS = "INPROGRESS";
	public static final String OEP_ORDER_PROCESSED_STATUS = "PROCESSED";
	public static final String OEP_ORDER_SUCCESS_STATUS = "SUCCESS";
	public static final String OEP_ORDER_FAILED_RECORD = "FAILED_RECORD";
	public static final String OEP_ORDER_MESSAGE_SENT = "Order Sent";
	public static final String OEP_ORDER_FAILED_STATUS = "FAILED";
	public static final String OEP_ORDER_MESSAGE_SENDING_FAILED = "Unable to process the request";
	public static final String OEP_ORDER_NOT_FOUND_STATUS = "DATA_NOT_FOUND";
	public static final String OEP_ORDER_MESSAGE_DATA_NOT_FOUND = "Query returned no results";
	
	public static final String MSISDN = "msisdn";
	public static final String NICE_CATEGORY =  "niceCategory";
	public static final String IMSI = "imsi";
	public static final String PUK1 = "puk1";
	public static final String PUK2 = "puk2";
	public static final String PIN1 = "pin1";
	public static final String PIN2 = "pin2";
	public static final String ICCID = "iccid";
	public static final String KI = "ki";
	public static final String ADM = "adm";
	public static final String ADDONS = "addons";
	public static final String BASE_PLAN = "basePlan";
	public static final String SERVICE_TYPE = "serviceType";
	
	public static final String OEP_ADD_PRE_PAID_STARTER_KIT_ACTION = "PrePaidStarterKit";
	public static final String OEP_ADD_PLAN_ACTION = "AddPlan";
	public static final String OEP_REMOVE_PLAN_ACTION = "RemovePlan";
	public static final String OEP_CHANGE_PLAN_ACTION = "ChangePlan";
	public static final String OEP_SUSPEND_SERVICE_ACTION = "SuspendService";
	public static final String OEP_RECONNECT_SERVICE_ACTION = "ReconnectService";
	public static final String OEP_TERMINATE_SERVICE_ACTION = "TerminateService";
	public static final String OEP_PRE_PAID_PROVISIONING_ACTION = "PrePaidProvisioning";
	public static final String OEP_POST_PAID_PROVISIONING_ACTION = "PostPaidProvisioning";

	
	//Enumeration Constants
	public static enum OEP_POLLING_STATUS { 
		UNPROCESSED,SCHEDULED,LOCKED,PROCESSED,INPROGRESS 
		};

}
