package com.oracle.oep.order.utils;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brm.opcode.root.Opcode;
import com.oracle.oep.brm.opcodes.CUSOPCUSTINCOMPATIBLEADDONInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTINCOMPATIBLEADDONOutputFlist;
import com.oracle.oep.brm.opcodes.CUSOPCUSTVALIDATECHANGEPLANInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.exceptions.OEPException;
import com.oracle.oep.order.constants.OEPConstants;
import com.oracle.oep.order.ebm.transform.ProcessModifyOrderEbmHelper;
import com.oracle.oep.order.model.OrderEntryRequest;

public class OEPRequestValidator {
	
	private static final Logger log = LogManager.getLogger(OEPRequestValidator.class);
	/**
	 * 
	 * This method calls BRM CUS_OP_CUST_VALIDATE_CHANGE_PLAN to validate change plan for non OAP/Selfcare channels
	 * @throws Exception 
	 * 
	 **/
	public static boolean validateRequest(final OrderEntryRequest request, final String orderAction,final ProducerTemplate template, final String channelName) throws Exception{
		
		boolean validation = true;
		
		log.info("Validate Reuest Called");
		log.info("OrderAction : "+orderAction);
		log.info("ChannelName : "+channelName);
		
		if(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION.equalsIgnoreCase(orderAction)){
			
			validation = validateChangePlanRequest(request, orderAction, template);
		}
		
		else if(OEPConstants.OEP_ADDON_MANAGEMENT_ACTION.equalsIgnoreCase(orderAction) && 
				!(OEPConstants.OSM_REMOVE_ACTION_CODE.equalsIgnoreCase(ProcessModifyOrderEbmHelper.getPlanAction(request, orderAction)))){
			
			log.info("Validate Add plan Called");
			
			validation = validateAddonPlanRequest(request, orderAction, template);
		}
					
		
		return validation;
	}
	
	/**
	 * 
	 * This method calls BRM CUS_OP_CUST_VALIDATE_CHANGE_PLAN to validate change plan for non OAP/Selfcare channels
	 * @throws Exception 
	 * 
	 **/
	public static boolean validateChangePlanRequest(final OrderEntryRequest request, final String orderAction,final ProducerTemplate template) throws Exception{
		
		boolean validation = true;
		
		if(orderAction.equalsIgnoreCase(OEPConstants.OEP_DHIO_CHANGEPLAN_ACTION)){
			
			String msisdn = ProcessModifyOrderEbmHelper.getServiceNo(request, orderAction);
			String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, orderAction) ;
			String userName = ProcessModifyOrderEbmHelper.getCsrId(request, orderAction);
			String channelTrackingId = ProcessModifyOrderEbmHelper.getChannelTrackingId(request, orderAction);
			String newBasePlan = ProcessModifyOrderEbmHelper.getBasePackageName(request, null, orderAction);
			
			CUSOPSEARCHOutputFlist searchResult = OrderDataUtils.getAccountDataFromBRMForMSISDN(template, msisdn, channelName, userName, channelTrackingId);
			
			RESULTS result = null;
			
			for (RESULTS item: searchResult.getRESULTS()){
				
				if((item.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_ACTIVE_STATUS_CODE)
					||(item.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_SUSPENDED_STATUS_CODE)){
					
					result = item;
				}
			}
			
			if(result == null){
				
				throw new RuntimeException("No Active Service is Found in BRM");
			}
			
			CUSOPCUSTVALIDATECHANGEPLANInputFlist validateChangePlanRequest =  new CUSOPCUSTVALIDATECHANGEPLANInputFlist();
			
			validateChangePlanRequest.setSERVICEID(searchResult.getRESULTS().get(0).getLOGIN());
			validateChangePlanRequest.setACCOUNTNO(searchResult.getRESULTS().get(0).getACCOUNTNO());
			validateChangePlanRequest.setPOID(OEPConstants.BRM_POID);
			validateChangePlanRequest.setPROGRAMNAME(channelName);
			validateChangePlanRequest.setUSERNAME(userName);
			validateChangePlanRequest.setNAME(newBasePlan);
			
			String inputXML = (String) template.requestBody("direct:convertToString", validateChangePlanRequest);
			
			validation = OrderDataUtils.validateChangePlanRequest(template, inputXML, channelTrackingId);
			
		}		
		
		return validation;
	}
	
	
	public static boolean validateAddonPlanRequest(final OrderEntryRequest request, final String orderAction,final ProducerTemplate template) throws Exception{
		

		String serviceNumber = ProcessModifyOrderEbmHelper.getServiceNo(request, orderAction);
		String channelName = ProcessModifyOrderEbmHelper.getChannelName(request, orderAction);
		String uimServiceIdentifer = ProcessModifyOrderEbmHelper.getUIMServiceIdentifier(request, orderAction);
		String addOnPlanName = ProcessModifyOrderEbmHelper.getAddonPackageName(request, orderAction);

		String userName = ProcessModifyOrderEbmHelper.getCsrId(request, orderAction);
		log.info("Incompatible Addon's called,MSISDN--"+serviceNumber+"-- Plan Name--"+addOnPlanName);
		
		CUSOPCUSTINCOMPATIBLEADDONOutputFlist incompatibleAddonResult = getIncompatibleAddonForMSISDN(template, serviceNumber, channelName, userName,addOnPlanName,(byte) 2);
	
		if(incompatibleAddonResult != null && incompatibleAddonResult.getERRORDESCR()!=null && incompatibleAddonResult.getERRORCODE()!=null){
			
			throw new Exception(incompatibleAddonResult.getERRORDESCR());
		}
		return true;
		
	}
	public static CUSOPCUSTINCOMPATIBLEADDONOutputFlist getIncompatibleAddonForMSISDN(final ProducerTemplate template, 
			final String msisdn, final String channelName, final String userName,String planname, Byte planType){
		
		
		CUSOPCUSTINCOMPATIBLEADDONInputFlist opcodeSearch = new CUSOPCUSTINCOMPATIBLEADDONInputFlist();
		
		opcodeSearch.setPOID(OEPConstants.BRM_POID_GSM);
		opcodeSearch.setPROGRAMNAME(channelName);
		opcodeSearch.setUSERNAME(userName);
		opcodeSearch.setNAME(planname);
		opcodeSearch.setMSISDN(msisdn);
		opcodeSearch.setCUSFLDPLANTYPE(planType);
		
		String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);
		log.info("inputXML---->"+inputXML);

		return queryBRMForIncompatibleAddon(template, inputXML);
		
	}
	
	public static CUSOPCUSTINCOMPATIBLEADDONOutputFlist queryBRMForIncompatibleAddon(final ProducerTemplate template, final String inputXML){
		
		CUSOPCUSTINCOMPATIBLEADDONOutputFlist searchResult = null;
		
		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_CUS_INCOMPATIBLEADDON_OPCODE);
		opcode.setInputXml(inputXML);		
		
		try{
		
		 log.info(". Calling BRM webservice to fetch Incompatible Addon. Input xml : \n"+inputXML);
		 
		 Exchange syncExchange = template.request("direct:getIncompatibleAddonFromBRM", new Processor() {
				
				@Override
				public void process(Exchange exchange) throws Exception {
					// TODO Auto-generated method stub
					Message in = exchange.getIn();
					in.setBody(opcode);
				}
			});
			 
			 Exception cause = syncExchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
			
			 if(cause!=null){
				 
				 throw cause;
				 
			}
			 
			searchResult = (CUSOPCUSTINCOMPATIBLEADDONOutputFlist) syncExchange.getOut().getBody();
		 
		
		 
		if(searchResult.getERRORCODE()!=null){
			 
			 log.info("Error while fetching Incompatibie Addon from BRM "+searchResult.getERRORDESCR());
			 throw new RuntimeException(searchResult.getERRORDESCR()); 
			 
			 
		 }
		 
		}
		catch (Exception e) {
			
			String errorMessage = e.getCause()!=null?e.getCause().getLocalizedMessage():e.getMessage();
			log.info("Exception while fetching Account data from BRM "+errorMessage);
			e.printStackTrace();
			
			throw new RuntimeException(e);
		}
		
		
		return searchResult;
		
	}

}
