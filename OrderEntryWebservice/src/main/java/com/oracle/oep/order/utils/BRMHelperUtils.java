package com.oracle.oep.order.utils;

import java.math.BigInteger;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oracle.oep.brm.opcode.root.Opcode;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHInputFlist;
import com.oracle.oep.brm.opcodes.CUSOPSEARCHOutputFlist;
import com.oracle.oep.brm.opcodes.PARAMS;
import com.oracle.oep.brm.opcodes.RESULTS;
import com.oracle.oep.order.constants.OEPConstants;

public class BRMHelperUtils {

	public static final Logger log = LogManager.getLogger(BRMHelperUtils.class);

	private BRMHelperUtils() {

	}

	public static CUSOPSEARCHOutputFlist getAccountDataFromBRM(final ProducerTemplate template, final String msisdn,
			final String uimServiceIdentifer, final String channelName, final String userName,
			final String oepOrderNumber) {

		if (uimServiceIdentifer != null)
			return getAccountDataFromBRMByFlag(template, uimServiceIdentifer, channelName, userName, oepOrderNumber);
		else if (msisdn != null)
			return getAccountDataFromBRMForMSISDN(template, msisdn, channelName, userName, oepOrderNumber);

		else
			return null;
	}

	private static CUSOPSEARCHOutputFlist getAccountDataFromBRMForMSISDN(final ProducerTemplate template,
			final String msisdn, final String channelName, final String userName, final String oepOrderNumber) {

		CUSOPSEARCHInputFlist opcodeSearch = new CUSOPSEARCHInputFlist();
		PARAMS param = new PARAMS();
		opcodeSearch.setPOID(OEPConstants.BRM_POID);
		opcodeSearch.setPROGRAMNAME(channelName);
		opcodeSearch.setUSERNAME(userName);
		opcodeSearch.setFLAGS(BigInteger.valueOf(2));
		opcodeSearch.setPARAMNAME("MSISDN");
		param.setVALUE(msisdn);
		param.setElem(BigInteger.valueOf(0));
		opcodeSearch.getPARAMS().add(param);

		String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);

		return queryBRMForAccountData(template, inputXML, oepOrderNumber);

	}

	private static CUSOPSEARCHOutputFlist getAccountDataFromBRMByFlag(final ProducerTemplate template,
			final String uimServiceIdentifer, final String channelName, final String userName,
			final String oepOrderNumber) {

		CUSOPSEARCHInputFlist opcodeSearch = new CUSOPSEARCHInputFlist();
		PARAMS param = new PARAMS();
		opcodeSearch.setPOID(OEPConstants.BRM_POID);
		opcodeSearch.setPROGRAMNAME(channelName);
		opcodeSearch.setUSERNAME(userName);
		opcodeSearch.setFLAGS(BigInteger.valueOf(201));
		param.setVALUE(uimServiceIdentifer);
		param.setElem(BigInteger.valueOf(0));
		opcodeSearch.getPARAMS().add(param);

		String inputXML = (String) template.requestBody("direct:convertToString", opcodeSearch);

		return queryBRMForAccountData(template, inputXML, oepOrderNumber);

	}

	public static RESULTS getActiveServiceFromSearchResult(CUSOPSEARCHOutputFlist searchResult) {

		for (RESULTS result : searchResult.getRESULTS()) {

			if ((result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_ACTIVE_STATUS_CODE)
					|| (result.getSTATUS().toString()).equals(OEPConstants.BRM_SERVICE_SUSPENDED_STATUS_CODE)) {

				log.info("Found Active Service. ServiceId: {}",result.getLOGIN());
				return result;
			}
		}
		return null;
	}

	private static CUSOPSEARCHOutputFlist queryBRMForAccountData(final ProducerTemplate template, final String inputXML,
			final String oepOrderNumber) {

		CUSOPSEARCHOutputFlist searchResult = null;

		Opcode opcode = new Opcode();
		opcode.setOpcodeName(OEPConstants.BRM_CUS_SEARCH_OPCODE);
		opcode.setInputXml(inputXML);

		try {

			log.info("OEP order number : {} Calling BRM webservice to fetch Account data. Input xml : \n {}", oepOrderNumber, inputXML);

			Exchange syncExchange = template.request("direct:getAccountAndPlanDetailsFromBRM", new Processor() {

				@Override
				public void process(Exchange exchange) throws Exception {
					Message in = exchange.getIn();
					in.setBody(opcode);
				}
			});

			Exception cause = syncExchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

			if (cause != null) {

				throw cause;

			}

			searchResult = (CUSOPSEARCHOutputFlist) syncExchange.getOut().getBody();

			if (searchResult.getERRORCODE() != null) {

				log.error("OEP order number : {}. Error while fetching Account data from BRM {}",oepOrderNumber, searchResult.getERRORDESCR());

			}

		} catch (Exception e) {

			String errorMessage = e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getMessage();
			log.error("OEP order number : {}. Exception while fetching Account data from BRM : {}",oepOrderNumber,errorMessage);

		}

		return searchResult;

	}
}
