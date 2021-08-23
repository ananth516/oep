package com.oracle.oep.order.constants;

public final class OEPConstants {
	
	
	public static final String OEP_SUCCESS_STATUS_CODE = "00";
	public static final String OEP_SUCCESS_STATUS_DESCRIPTION = "Order Submitted Successfully";
	public static final String OEP_FAILURE_STATUS_CODE = "01";
	public static final String OEP_FAILURE_STATUS_DESCRIPTION = "Failed to submit Order";
	
	//MileStones
	
	public static final String OSM_ORDER_COMPLETE_MILESTONE_CODE="COMPLETE";
	public static final String OSM_ORDER_INPROGRESS_MILESTONE_CODE="IN_PROGRESS";
	
	
	//Queue Names
	public static final String OEP_SALESORDER_JMS_QUEUE_JNDI = "OEP_SALESORDER_JMS_QUEUE";
	public static final String OEP_OSM_WS_REQUEST_QUEUE_JNDI = "OEP_OSM_WS_REQUEST_QUEUE";
	public static final String BRM_SALESORDER_JMS_QUEUE_JNDI = "BRM_SALESORDER_JMS_QUEUE";
	
	
	//Header Constants
	public static final String ORDER_ACTION_HEADER = "ORDER_ACTION";
	public static final String SERVICE_TYPE_HEADER = "SERVICE_TYPE";
	public static final String OEP_ORDER_NUMBER_HEADER = "OEP_ORDER_NUMBER";
	
	//Message Header Names	
	public static final String CHANNEL_NAME_HEADER = "CHANNEL_NAME";
	public static final String JMS_CORRELATION_ID = "JMSCorrelationID"; 
	public static final String EVENT_NAME = "EventName";
	public static final String EVENT_FILTER_NAME = "EventFilterName";
	public static final String EVENT_FILTER_VALUE = "EventFilterValue";
	public static final String NOTIFICATION_SOURCE = "NOTIFICATION_SOURCE";

	
	//OrderActions	
	public static final String OEP_ADD_MOBILE_SERVICE_ACTION = "AddMobileService";
	public static final String OEP_ADD_MOBILE_BB_SERVICE_ACTION = "AddMobileBroadbandService";
	public static final String OEP_ADD_SHORT_CODE_SERVICE_ACTION = "AddShortCodeService";
	public static final String OEP_MOBILE_SERVICE_PORTIN_ACTION = "MobileService-PortIn";
	public static final String OEP_ADDON_MANAGEMENT_ACTION = "DHIOAddOnManagement";
	public static final String OEP_MODIFY_MOBILE_ADD_REMOVE_PLAN = "ModifyMobileService-Add-Remove-Plan";
	public static final String OEP_MODIFY_MOBILE_SIM_CHANGE = "ModifyMobileService-SimChange";
	public static final String OEP_MODIFY_BB_SIM_CHANGE = "ModifyMobileBroadbandService-SimChange";
	public static final String OEP_MODIFY_BB_ADD_REMOVE_PLAN= "ModifyMobileBroadbandService-Add-Remove-Plan";
	public static final String OEP_DISCONNECT_MOBILE_SERVICE_ACTION = "MobileService-Disconnect";
	public static final String OEP_DISCONNECT_MOBILE_BB_SERVICE_ACTION = "MobileBroadbandService-Disconnect";
	public static final String OEP_DISCONNECT_SHORTCODE_SERVICE_ACTION = "MobileShortCodeService-Disconnect";	
	public static final String OEP_PORTOUT_MOBILE_SERVICE_ACTION = "MobileService-Portout";
	public static final String OEP_MODIFY_MOBILE_MSISDN_CHANGE = "ModifyMobileService-MSISDNChange";
	public static final String OEP_MODIFY_BB_MSISDN_CHANGE = "ModifyMobileBroadbandService-MSISDNChange";
	public static final String OEP_ADD_MOBILE_PREPAIDTOPOSTPAID_SERVICE_ACTION = "MobileService-PrepaidToPostpaid";
	public static final String OEP_ADD_MOBILE_POSTPAIDTOPREPAID_SERVICE_ACTION = "MobileService-PostpaidToPrepaid";
	public static final String OEP_MODIFY_MOBILE_CHANGEPLAN_ACTION = "ModifyMobileService-ChangePlan";
	public static final String OEP_DHIO_CHANGEPLAN_ACTION = "DHIOChangePlan";
	public static final String OEP_MODIFY_BB_CHANGE_PLAN = "ModifyMobileBroadbandService-ChangePlan";
	public static final String OEP_MODIFY_SCS_SHORT_CODE_NUMBER_CHANGE = "ModifyShortCodeService-ShortCodeNumberChange";
	public static final String OEP_CHANGE_OWNERSHIP_TERMINATE = "DisconnectChangeOwnership";
	public static final String OEP_ADD_MOBILE_BB_PREPAIDTOPOSTPAID_SERVICE_ACTION = "MobileBroadbandService-PrepaidToPostpaid";
	public static final String OEP_ADD_MOBILE_BB_POSTPAIDTOPREPAID_SERVICE_ACTION = "MobileBroadbandService-PostpaidToPrepaid";
	public static final String OEP_SHORTCODE_CHANGEPLAN_ACTION="ModifyShortCodeService-ChangePlan";
	public static final String OEP_CHANGEPLAN_BULKSMS_SERVICE_ACTION="ModifyBulkSMSService-ChangePlan";
	public static final String OEP_CHANGEDETAIL_BULKSMS_SERVICE_ACTION="ModifyBulkSMSService-ChangeDetails";
	public static final String OEP_MOBILEBB_CHANGE_OWNERSHIP_TERMINATE = "DisconnectMobileBBChangeOwnership";
	public static final String OEP_MODIFY_SCS_ADD_REMOVE_PLAN = "ModifyShortCodeService-Add-Remove-Plan";	
	public static final String OEP_BULK_ADD_PLAN_ACTION = "AddPlan";
	public static final String OEP_BULK_REMOVE_PLAN_ACTION = "RemovePlan";
	public static final String OEP_MOBILE_DISCONNECT_FUTURE_DATE_SERVICE_ACTION = "MobileService-Terminate-FutureDate";

	
	//Suspend Order Actions
	public static final String OEP_SUSPEND_RESUME_ORDER = "SuspendResumeOrder";
	public static final String OEP_SUSPEND_MOBILE_SERVICE_LOST_SIM_ACTION = "MobileService-Suspend-LostSIM";
	public static final String OEP_SUSPEND_MOBILE_SERVICE_TRAVEL_REASONS_ACTION = "MobileService-Suspend-TravelReasons";
	public static final String OEP_RESUME_MOBILE_SERVICE_ACTION = "MobileService-Resume";
	public static final String OEP_RESUME_MOBILE_SERVICE_TRAVEL_REASONS_ACTION = "MobileService-Resume-TravelReasons";
	public static final String OEP_MNP_NUMBER_RELEASE = "MNP Number Release";
	public static final String OEP_ACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION = "MobileService-CallDivert-Activate";
	public static final String OEP_DEACTIVATE_MOBILE_SERVICE_CALLDIVERT_ACTION = "MobileService-CallDivert-DeActivate";
	public static final String OEP_NUMBER_RELEASE_MOBILE_SERVICE = "MobileService-NumberRelease";
	public static final String OEP_CALL_DIVERT_TYPE_CODE = "Call Divert";
	public static final String OEP_RESUME_BULKSMS_SERVICE_ACTION = "BulkSMSService-Resume";
	public static final String OEP_SUSPEND_BULKSMS_SERVICE_ACTION = "BulkSMSService-Suspend";
	public static final String OEP_SUSPEND_SHORTCODE_SERVICE_ACTION = "MobileShortCodeService-Suspend";	
	public static final String OEP_SUSPEND_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION = "MobileService-Suspend-Government-RA";
	public static final String OEP_RESUME_MOBILE_SERVICE_GOVERNMENT_RA_REASONS_ACTION = "MobileService-Resume-Government-RA";
	
	
	//Start Cancel Update Actions
	public static final String OEP_START_ACTION = "Start";
	public static final String OEP_CANCEL_ACTION = "Cancel";
	public static final String OEP_UPDATE_ACTION = "Update";
	
	
	//Order Entity Status Constants
	public static final String OEP_ORDER_STATUS_OPEN = "OPEN";
	public static final String OEP_ORDER_STATUS_Description = "Sent to Fulfillment";
	public static final String OEP_ORDER_CREATED_STATUS_DESCRIPTION = "Order Created";
	public static final String OEP_ORDER_STATUS_CANCELLED = "CANCELLED";
	public static final String OEP_ORDER_STATUS_FAILED = "FAILED";
	public static final String OEP_ORDER_STATUS_FULFILL_BILLING = "FULFILL BILLING COMPLETE";
	public static final String OEP_ORDER_STATUS_COMPLETE = "COMPLETE";
	public static final String OEP_ORDER_STATUS_COMPLETE_DESCRIPTION = "All order lines are completed";
	public static final String OEP_ORDER_STATUS_IN_PROGRESS = "IN_PROGRESS";
	public static final String OEP_OPEN_ORDER_FAILURE_STATUS = "Your request cannot be completed at the moment as previous transaction is still open. Please try after some time.";

	//Plan Type constants
	public static final String OEP_BASE_PLAN_TYPE= "PLAN";
	public static final String OEP_ADDON_PLAN_TYPE= "ADDON";
	public static final String OEP_PORTIN = "PortIn";
	public static final String OEP_MOBILE_BB = "MobileBB";
	public static final String OEP_BB_SERVICE_TYPE =  "BB";
	
	//Flex Plan Constants
	
	public static final String OEP_PLAN_FLEXTYPE = "POSTPAID_FLEX";
	public static final String OEP_PLAN_SPEC_FLEXATTRIBUTE = "planType";
	public static final String OEP_PLAN_SPEC_FLEXATTRIBUTE_VALUE = "Flex";
	
	

	
	//RouteConstants	
	public static final String SALES_ORDER_JMS_ROUTE = "weblogicJMS:"+OEP_SALESORDER_JMS_QUEUE_JNDI+"exchangePattern=InOnly";
	
	//SpecificationGroup Constants for ShortCode
	public static final String SHORT_CODE_NUMBER = "shortCodeNumber";
	public static final String SHORT_CODE_TYPE = "shortCodeType";
	public static final String SHORT_CODE_CATEGORY = "shortCodeCategory";
	public static final String EMAIL_ID = "emailId";
	
	
	//Miscellaneous
	public static final String BRM_CHANNEL_NAME = "BRM";
	public static final String OAP_CHANNEL_NAME = "OAP";
	public static final String NPMS_CHANNEL_NAME = "NPMS";
	public static final String OEP_IS_FUTURE_DATE_N = "N";
	public static final String OEP_IS_PARK_N = "N";
	public static final String OEP_BATCH_ID_DUMMY_VALUE = "-5000";
	public static final String OEP_IS_PARK_Y = "Y";
	public static final String OEP_IS_FUTURE_DATE_Y = "Y";
	public static final String OEP_CONSTANT_Y = "Y";
	public static final String OEP_CONSTANT_N = "N";
	public static final String BRM_USER_ID = "BRM";
	public static final String BRM_COLLECTIONBARRING = "0";
	
	//OSM Sales Order Constants
	public static final String SALES_ORDER_TYPE_CODE = "SALES ORDER";
	public static final String FULFILLMENT_MODE_CODE_DELIVER = "Deliver";
	public static final String FULFILLMENT_MODE_CODE_DO = "DO";
	public static final String FULFILLMENT_SUCCESS_CODE = "DEFAULT";
	public static final String OSM_ORDER_PRIORITY_CODE_DEFAULT = "4";
	public static final String OSM_ORDER_HIGHPRIORITY_CODE_DEFAULT = "9";
	public static final String OSM_TYPE_CODE_PRODUCT = "PRODUCT";
	public static final String FIC_ATTRIBUTE_NAME = "FulfillmentItemCode";
	public static final String PTC_ATTRIBUTE_NAME = "PermittedTypeCode";
	public static final String BPT_ATTRIBUTE_NAME = "BillingProductTypeCode";
	public static final String BPT_ATTRIBUTE_VALUE = "SUBSCRIPTION";
	public static final String ORDER_BARRINGACTION_VALUE = "0";
	public static final String ORDER_UNBARRINGACTION_VALUE = "1";
	
	
	
	//SpecificationGroup Constants
	public static final String OSM_EXTENSIBLE_ATTRIBUTE = "ExtensibleAttributes";
	public static final String MSISDN = "msisdn";
	public static final String OLD_MSISDN = "old_msisdn";
	public static final String NICE_CATEGORY =  "niceCategory";
	public static final String IMSI = "imsi";
	public static final String PUK1 = "puk1";
	public static final String PUK2 = "puk2";
	public static final String ICCID = "iccid";
	public static final String SIMTYPE = "simType";
	public static final String OLD_ICCID = "old_iccid";
	public static final String COS_ID = "CosID";
	public static final String PCOS_ID = "pcos";
	public static final String SCOS_ID = "scos";
	public static final String SERVICE_TYPE = "serviceType";
	public static final String SERVICE_TYPE_PREPAID = "Prepaid";
	public static final String SERVICE_TYPE_POSTPAID = "Postpaid";
	public static final String SERVICE_TYPE_PREPAID_BRM = "1";
	public static final String SERVICE_TYPE_POSTPAID_BRM = "2";
	public static final String SERVICE_TYPE_POSTPAID_FLEX_BRM = "3";
	public static final String PACKAGE_ID = "PACKAGE_ID";
	public static final String EARLY_TERMINATION_FLAG = "EarlyTerminationFlag";
	public static final String TERMINATION_REASON = "TerminationReason";
	public static final String TERMINATION_CHARGE_REQUIRED = "CUS_FLD_CHARGE_TERMINATION";
	public static final String PORT_TYPE = "portType";
	public static final String REASON_SIM_CHANGE = "reason_simChange";
	public static final String REASON_MSISDN_CHANGE = "reason_msisdnChange";
	public static final String MSISDN_CHARGE_FLAG = "msisdnChargeableFlag";
	public static final String SERVICE_CHARGE_FLAG = "serviceChargeableFlag";
	public static final String WAIVER_REASON = "waiverReason";
	public static final String WAIVER_OPTIONS = "options";
	public static final String SUSPENSION_DURATION = "SuspensionDuration";
	public static final String SERVICE_SUSPENSION_DURATION = "ServiceSuspensionDescription";
	public static final String SERVICE_SUSPENSION_DEAL_NAME = "DealName";
	public static final String VAS_NAME = "VASName";
	public static final String REASON_CHANGEOWNERSHIP = "changeOwnershipReason";
	public static final String SHORTCODENUMBER = "shortCodeNumber";
	public static final String BULKSMS = "userID";
	public static final String TERMINATION_NOTES = "terminationNotes";
	public static final String IS_TOURIST = "isTouristKit";
	public static final String OEP_TRUE="true";
	public static final String OEP_FALSE="false";
	
	
	
	//Product Specifications	
	public static final String MOBILE_BASEPLAN_PS = "Mobile PS";
	public static final String MOBILE_ADDON_PS = "Mobile Addon PS";
	public static final String MOBILE_DATA_PS = "Mobile Data PS";
	public static final String MOBILE_BB_PS = "Mobile Broadband PS";
	public static final String MOBILE_VAS_PS = "Mobile VAS PS";
	public static final String MOBILE_BB_ADDON_PS = "Mobile BroadBand Addon PS";
	public static final String SHORT_CODE_PS = "Short Code PS";
	public static final String SHORT_CODE_ADDON_PS = "Short Code Addon PS";
	public static final String MOBILE_BULK_SMS_PS = "Bulk SMS PS";
	public static final String MOBILE_GSM_PERMITTED_TYPE_CODE = "/service/telco/gsm";	
	public static final String MOBILE_BB_PERMITTED_TYPE_CODE = "/service/telco/broadband";
	public static final String MOBILE_BULK_SMS_PERMITTED_CODE = "/service/telco/sms";
	public static final String MOBILE_SHORT_CODE_PERMITTED_TYPE_CODE = "/service/telco/shortcode";
	
	
	//Add On Package Type
	public static final String MOBILE_SMS_ADDON_PACKAGE_TYPE = "SMS";
	public static final String MOBILE_VOICE_ADDON_PACKAGE_TYPE = "VOICE";
	
	//OSM Service Action Code
	public static final String OSM_ADD_ACTION_CODE = "Add";
	public static final String OSM_MODIFY_ACTION_CODE = "Modify";
	public static final String OSM_REMOVE_ACTION_CODE = "Remove";
	public static final String OSM_DISCONNECT_ACTION_CODE = "Disconnect";
	public static final String OSM_PORTOUT_ACTION_CODE = "DisconnectForPortOut";
	public static final String OSM_SUSPEND_ACTION_CODE = "Suspend";
	public static final String OSM_RESUME_ACTION_CODE = "Resume";
	public static final String OSM_PREPAIDTOPOSTPAID_ACTION_CODE = "PrepaidToPostpaid";
	public static final String OSM_POSTPAIDTOPREPAID_ACTION_CODE = "PostpaidToPrepaid";
	public static final String OSM_CHANGEPLAN_ACTION_CODE="ChangePlan";
	public static final String OSM_ACTIVATE_ACTION_CODE = "Activate";
	public static final String OSM_DEACTIVATE_ACTION_CODE = "DeActivate";
	public static final String OSM_BARRING_STEP_ACTION_CODE = "StepBarring";
	public static final String OSM_BARRING_OCB_ACTION_CODE = "OCB";
	public static final String OSM_BARRING_TOS_ACTION_CODE = "TOS";
//	public static final String OSM_UNBARRING_ACTION_CODE = "UnbarResume";
	public static final String OSM_UNBARRING_ACTION_CODE = "Resume";

	//OSM Order Headers
	public static final String OSM_ORDER_HEADER_URI = "URI";
	public static final String OSM_ORDER_HEADER_WLS_CONTENT_TYPE = "_wls_mimehdrContent_Type";
	
	public static final String OSM_WEBSERVICE_URI = "/osm/wsapi";
	public static final String OSM_WEBSERVICE_CONTETNT_TYPE = "text/xml; charset=UTF-8";
	

	
	//Enumeration Constants
	public static enum OEP_POLLING_STATUS { 
		UNPROCESSED,LOCKED,PROCESSED,INPROGRESS 
		};
		
	//BRM Constants
	public static final String BRM_POID = "0.0.0.1 /account -1 0";
	public static final String BRM_POID_GSM = "0.0.0.1 /service/telco/gsm -1 0";
	public static final String BRM_CUS_SEARCH_OPCODE = "CUS_OP_SEARCH";
	public static final String BRM_CUS_INCOMPATIBLEADDON_OPCODE = "CUS_OP_CUST_INCOMPATIBLE_ADDON";
	public static final String BRM_UPDATE_SERVICE_OPCODE = "CUS_OP_CC_UPDATE_SERVICE";
	public static final String BRM_VALIDATE_CHANGE_PLAN_OPCODE = "CUS_OP_CUST_VALIDATE_CHANGE_PLAN";
	public static final String BRM_CUS_SEARCH_USER = "CSR";
	public static final String BRM_BARRING_ACTION = "0";
	public static final String BRM_UNBARRING_ACTION = "1";
	public static final String BRM_ACCOUNT_LEVEL_BARRING = "0";
	public static final String BRM_SERVICE_LEVEL_BARRING = "1";
	public static final String BRM_BARRING_DUNNING = "2";
	public static final String BRM_BARRING_LOST_SIM = "3";
	public static final String BRM_BARRING_VOLUNT_SUSPND = "4";
	public static final String BRM_BARRING_UNSUSPND = "5";
	public static final String BRM_STAGE1_BARRING_ACTION = "1";
	public static final String BRM_SERVICE_ACTIVE_STATUS_CODE = "10100";
	public static final String BRM_SERVICE_SUSPENDED_STATUS_CODE = "10102";
	public static final String BRM_ADD_PLAN_OPCODE = "CUS_OP_CUST_ADD_PLAN";
	public static final String BRM_CANCEL_PLAN_OPCODE = "CUS_OP_CUST_CANCEL_PLAN";
	public static final String BRM_ACCOUNT_OBJ = "0.0.0.1 /account 9456807 16";	
	public static final String BRM_CHANGE_HANDSET="CHANGE_HANDSET";	
	public static final String BRM_CUST_UPD_SERVICE_OPCODE = "CUS_OP_CUST_UPDATE_SERVICE";
	
	// Managed CUG Services
	public static final String OEP_MOBILE_SERVICE_TYPE="Mobile Service";
	public static final String OEP_MOBILE_SERVICE_CUG = "MobileService-CUG";
	public static final String OEP_MOBILE_SERVICE_CUG_SERVICE_ACTION_CODE_MODIFY = "Modify";
	public static final String OEP_MOBILE_SERVICE_CUG_ADD_ACTION = "Add";
	public static final String OEP_MOBILE_SERVICE_CUG_MODIFY_ACTION = "Modify";
	public static final String OEP_MOBILE_SERVICE_CUG_REMOVE_ACTION = "Remove";
	public static final String OEP_MOBILE_SERVICE_CUG_SHORT_NUMBER_ADD_ACTION = "Add";
	public static final String OEP_MOBILE_SERVICE_CUG_SHORT_NUMBER_MODIFY_ACTION = "Modify";
	public static final String OEP_MOBILE_SERVICE_CUG_SHORT_NUMBER_REMOVE_ACTION = "Remove";
	public static final String OEP_MOBILE_SERVICE_CUG_ACTION_PARAM = "cugAction";
	public static final String OEP_MOBILE_SERVICE_CUG_REQUEST_PARAM_SHORT_NUMBER_ACTION="shortNumberAction";
	public static final String OEP_MOBILE_SERVICE_CUG_REQUEST_PARAM_OLD_SHORT_NUMBER = "oldShortNumber";
	public static final String OEP_MOBILE_SERVICE_CUG_REQUEST_PARAM_SHORT_NUMBER = "shortNumber";
	public static final String OEP_MOBILE_SERVICE_CUG_REQUEST_PARAM_CUG_ID = "cugId";
	public static final String OEP_MOBILE_SERVICE_CUG_REQUEST_PARAM_MSISDN = "msisdn";
	
	// Mobile Broadband Suspend or Resume
	public static final String OEP_SUSPEND_MOBILE_BB_SERVICE_LOST_SIM_ACTION = "MobileBroadbandService-Suspend-LostSIM";
	public static final String OEP_SUSPEND_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION = "MobileBroadbandService-Suspend-TravelReasons";
	public static final String OEP_RESUME_MOBILE_BB_SERVICE_ACTION = "MobileBroadbandService-Resume";
	public static final String OEP_RESUME_MOBILE_BB_SERVICE_TRAVEL_REASONS_ACTION = "MobileBroadbandService-Resume-TravelReasons";
	public static final String OEP_MOBILE_BB_PRODUCT_SPECIFICATION = "Mobile Broadband PS";
	public static final String OEP_MOBILE_BB_SERVICE_TYPE = "Mobile BB Service";
	public static final String OEP_SUSPEND_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION = "MobileBroadbandService-Suspend-Government-RA";
	public static final String OEP_RESUME_MOBILE_BB_SERVICE_GOVERNMENT_RA_REASONS_ACTION = "MobileBroadbandService-Resume-Government-RA";

	// Bulk SMS
	public static final String OEP_ADD_BULK_SMS_ACTION = "AddBulkSMSService";
	public static final String OEP_BULK_SMS_SPEC_PARAM_USER_ID = "userID";
	public static final String OEP_BULK_SMS_SPEC_PARAM_CONTACT_NUMBER = "contactNumber";
	public static final String OEP_BULK_SMS_SPEC_PARAM_EMAIL_ID = "emailId";
	public static final String OEP_BULK_SMS_SPEC_PARAM_ID = "id";
	public static final String OEP_BULK_SMS_SPEC_PARAM_ID_TYPE = "idType";
	public static final String OEP_BULK_SMS_SPEC_PARAM_TYPE = "type";
	public static final String OEP_BULK_SMS_SPEC_PARAM_SERVICE_TYPE = "serviceType";
	public static final String OEP_BULK_SMS_SPEC_PARAM_CUSTOMER_NAME = "customerName";
	public static final String OEP_BULK_SMS_SPEC_PARAM_PLAN_ID = "planId";
	public static final String OEP_BULK_SMS = "Bulk SMS";	

	// Bulk SMS Terminate
	public static final String OEP_DISCONNECT_BULK_SMS_ACTION = "BulkSMSService-Disconnect";
	

	//BRM Order Actions
		public static final String BRM_ORDER_SPENDINGCAP_ACTION = "SpendingCapAction";
		public static final String BRM_ORDER_COLLECTION_ACTION_BARRING = "CollectionsActionBarring";
		
		public static final String BRM_ORDER_JMSCORRELATION_ID = "JMSCorrelationID";
		public static final String BRM_ORDER_JMSTIMESTAMP = "JMSTimestamp";
		
		// Mobile FCA
		public static final String BRM_FCA_MOBILE_ACTION = "FCA";
		public static final String OEP_FCA_MOBILE_ACTION = "FirstCallActivation";
		public static final String OEP_FCA_MOBILE_PARAM_FIRST_USAGE_ACTIVATION = "firstUsageActivation";
		public static final String TRUE = "true";
	
		//Prepaid Lifecycle Barring
		public static final String BRM_PREPAID_LIFECYCLE_BARRING_ACTION = "PrepaidLifecycleBarring";
		public static final String BRM_PREPAID_LIFECYCLE_NOTIF_REMOVE_SERVICE_ACTION = "NotifRemoveServiceNotification";
		public static final String BRM_PREPAID_LIFECYCLE_TERMINATE_SERVICE_ACTION = "TerminateService";
		public static final String BRM_ORDER_CANCEL_PLAN = "CancelPlan";
		
		public static final String OEP_SC = "SC";
		
		
		//Event Metadata
		public static final String PRE_CREATE_SERVICE_EVENT="PreCreateservice";
		public static final String PRE_CREATE_SERVICE_DESC="Pre-create Service";
		public static final String PRE_CREATE_SERVICE_REASON="Port-In";
		public static final String NOTIF_CANCEL_ADDON_FAILURE = "NotifCancelAddOnSubscriptionFailure";
		public static final String NOTIF_ADD_ADDON_FAILURE = "NotifAddOnSubscriptionFailure";
		public static final String NOTIF_CHANGE_RATEPLAN_FAILURE = "NotifChangeRatePlanFailure";
		
		
		
		//Channel Names
		public static final String NOTIF_CHANNEL_NAME="NOTIF";
		public static final String OEP_RELOAD_CHANNEL_NAME = "RELOAD";
		
		//TTConstants
		public static final String OEP_TT_USER = "BU10000011";
		public static final String OEP_TT_CALL_ACTION_TYPE = "FalloutTicketCreation";
		public static final String OEP_TT_SERVICE_GROUP = "FALLOUT";
		
		public static final String OEP_PROVISIONING_FAILURE_BODY_TEMPLATE = "Provisioning for $serviceNumber$ with Order Id $orderId$ failed. Please check CBS for more details.";
		
		public static final String OEP_PROVISIONING_FAILURE_SUBJECT_TEMPLATE = "CBS Provisioning failure";
		
		public static final String OEP_PROVISIONING_FAILURE_EVENT = "CBS_Provisioning_Failure";
		public static final String NO_RECORDS_FOUND = "No Records Found";
		public static final String UNABLE_TO_FETCH_RECORDS = "Unable to fetch records";
		public static final String UNABLE_TO_PROCESS_ORDER = "Unable to process Error";
		public static final String PARAM_NAME_MSISDN = "msisdn";
		public static final String PARAM_NAME_CSRID = "csrId";
		public static final String PARAM_NAME_ORDER_ACTION = "OrderAction";
		public static final String PARAM_NAME_CHANNEL_NAME = "ChannelName";
		public static final String PARAM_NAME_CHANNEL_TRACKING_ID = "ChannelTrackingId";
		public static final String PARAM_NAME_ACCOUNT_ID = "AccountId";
		public static final String OEP_ORDER_PREFIX = "01";
		
}
