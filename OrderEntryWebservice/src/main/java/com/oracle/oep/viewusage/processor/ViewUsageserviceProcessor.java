package com.oracle.oep.viewusage.processor;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.oracle.oep.viewusage.xsd.CHARGES;
import com.oracle.oep.viewusage.xsd.CUSOPSUBSCRIPTIONVIEWUSAGEInputFlistRequest;
import com.oracle.oep.viewusage.xsd.CUSOPSUBSCRIPTIONVIEWUSAGEOutputFlistResponse;
import com.oracle.oep.viewusage.xsd.USAGERECORDS;

import oracle.jdbc.OracleTypes;

public class ViewUsageserviceProcessor implements Processor {
	
	public static final Logger log = LogManager.getLogger(ViewUsageserviceProcessor.class);
	
	private DataSource brmDataSource;
	
	 private JdbcTemplate jdbcTemplate;
	
	//private ProducerTemplate template;	
	Message in= null;
	Message out= null;
	CUSOPSUBSCRIPTIONVIEWUSAGEInputFlistRequest request = null;
	CUSOPSUBSCRIPTIONVIEWUSAGEOutputFlistResponse response = null;

	public DataSource getBrmDataSource() {
		return brmDataSource;
	}

	public void setBrmDataSource(DataSource brmDataSource) {
		this.brmDataSource = brmDataSource;
	}
	
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		Message in = exchange.getIn();
		Message out = exchange.getOut();
		List<ViewUsage> usage = null;
		
		request = in.getBody(CUSOPSUBSCRIPTIONVIEWUSAGEInputFlistRequest.class);
		String requestStr = in.getBody(String.class);
		
		log.info("Received usage view Request  : \n"+requestStr);
		
		String msisdn = request.getCUSFLDSERVICEIDENTIFIER();
		log.info("Received usage view Request for MSISDN : \n"+msisdn);
		
		int flag = request.getUSAGECATEGORY();
		Long usageEndT = request.getUSAGEENDT();
		Long usageStartT = request.getUSAGESTARTT();
		
		SimpleJdbcCall  executor = new SimpleJdbcCall(jdbcTemplate)
				.withCatalogName("CUS_TEST_VIEW_USAGE")
                .withProcedureName("CUS_TEST_VIEWUSAGE").withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                    new SqlParameter("INparam1", Types.VARCHAR),
                    new SqlParameter("INparam2", Types.INTEGER),
                    new SqlParameter("INparam3", Types.INTEGER),
                    new SqlParameter("INparam4", Types.INTEGER),
                    new SqlOutParameter("OUTParam1", OracleTypes.CURSOR, new ViewUsageRowMapper()));
		executor.compile();
		
	    SqlParameterSource param = new MapSqlParameterSource()
	            .addValue("INparam1", msisdn)
	            .addValue("INparam2", flag)
	    		.addValue("INparam3",usageStartT)
	    		.addValue("INparam4", usageEndT);
	    
	    Map<String, Object> map = executor.execute(param);
	    
	    //log.info("Usage Map "+ map);
	    log.info("Usage Map size "+ map.size());
	    usage = (List<ViewUsage>) map.get("OUTParam1");
	    
	    //log.info("Output from Procedure : "+ map.get("OUTParam1")+"......");
	    
	    response = new CUSOPSUBSCRIPTIONVIEWUSAGEOutputFlistResponse();
	    
	    List<ViewUsage> aList = aggregateUsage(usage);
	    prepareResponse(aList);
	
		
		log.info("Msisdn : "+ msisdn+". Response is set");
		out.setBody(response);
	}

	private List<ViewUsage> aggregateUsage(List<ViewUsage> usage) {
		// TODO Auto-generated method stub
		
		List<ViewUsage> aggregatedList = new ArrayList<ViewUsage>();

		Map<String, List<ViewUsage>> sessionMap = new LinkedHashMap<String, List<ViewUsage>>();
		Iterator<ViewUsage> itr = usage.iterator();
		
		//log.info("Seesion Map "+ sessionMap);
		
		while(itr.hasNext()) {
			ViewUsage usageRec = itr.next();
			
			if(sessionMap.containsKey(usageRec.getNetworkSessionid())) {
				sessionMap.get(usageRec.getNetworkSessionid()).add(usageRec);
			} else {
				List<ViewUsage> newList = new ArrayList<ViewUsage>();
				newList.add(usageRec);
				sessionMap.put(usageRec.getNetworkSessionid(), newList);
			}
		}

		
		for (Map.Entry<String, List<ViewUsage>> entry : sessionMap.entrySet()) {

			List<ViewUsage> innerList = entry.getValue();
			String event_no = null;
			BigDecimal sumAmount = new BigDecimal(0);
			long sumNetQuantity = 0L;
			
			//log.info("No of records "+ innerList.size());
			
			for(int index = 0; index < innerList.size(); index++) {
				
				//Sum Amount
				if(innerList.get(index).getEbiResourceId().equals("462")
						|| innerList.get(index).getEbiResourceId().equals("840")
						|| innerList.get(index).getEbiResourceId().equals("19900000")) {	
					sumAmount = sumAmount.add(innerList.get(index).getEbiAmt());
				}
				
				if(innerList.get(index).getQuantity() != null
						&& (innerList.get(index).getEbiResourceId().equals("462")
						|| innerList.get(index).getEbiResourceId().equals("19900000"))
						&& innerList.get(index).getEbiAmt().compareTo(BigDecimal.ZERO) != -1) {
					sumNetQuantity = sumNetQuantity + innerList.get(index).getEbiQty().longValue();
				}
				
				if(innerList.get(index).getEventNo() != null
						&& !innerList.get(index).getEventNo().equalsIgnoreCase("")) {
					event_no = innerList.get(index).getEventNo();
				}
				UsageCharges usageCharge = new UsageCharges();
				usageCharge.setAmount(innerList.get(index).getEbiAmt());
				usageCharge.setOfferName(innerList.get(index).getOfferName());
				usageCharge.setQuantity(innerList.get(index).getEbiQty());
				usageCharge.setResourceId(innerList.get(index).getEbiResourceId());
				
				innerList.get(0).getChargesList().add(usageCharge);
			}
			
			//Update the index = 0 element with the updates above
			if(innerList != null) {
				if(innerList.get(0) != null) {
					innerList.get(0).setStartT(innerList.get(0).getStartT());
					innerList.get(0).setEndT(innerList.get(innerList.size() - 1).getEndT());
					innerList.get(0).setAmount(sumAmount);
					innerList.get(0).setQuantity(sumNetQuantity);
					if(innerList.get(0).getEventNo() != null
							&& !innerList.get(0).getEventNo().equalsIgnoreCase("")) {
						if(innerList.get(0).getEventNo().endsWith("|")) {
							String[] pipeTokens = innerList.get(0).getEventNo().split("\\|");
							innerList.get(0).setEventNo(sumAmount + "->" + pipeTokens[0].split("\\->")[1]);
						} 						
						else if(innerList.get(0).getEventNo().contains("->")){
							innerList.get(0).setEventNo(sumAmount + "->" + innerList.get(0).getEventNo().split("\\->")[1]);
						}
						else {
							
							innerList.get(0).setEventNo(sumAmount + "->" + innerList.get(0).getEventNo());
						}
					} else {
						if(event_no != null) {
							if(!event_no.equalsIgnoreCase("")) {
								String[] pipeTokens = event_no.split("\\|");
								 if(innerList.get(0).getEventNo().contains("->"))
									 innerList.get(0).setEventNo(sumAmount + "->" + pipeTokens[0].split("\\->")[1]);
							}
						} else {
							innerList.get(0).setEventNo("");
						}
						
					}
		
					aggregatedList.add(innerList.get(0));
				}
			}
		}
		
		return aggregatedList;
	}

	private void prepareResponse(List<ViewUsage> usage) {
		// TODO Auto-generated method stub
		
		Iterator<ViewUsage> itr = usage.iterator();
		int count = 0;
		while(itr.hasNext()) {
			ViewUsage usageRec = itr.next();
			
			USAGERECORDS record = new USAGERECORDS();
			record.setEVENTNO(usageRec.getEventNo());
			record.setDESCR(usageRec.getDescr());
			record.setUSAGETYPE(usageRec.getUsageType());
			record.setCUSFLDUNITNAME(usageRec.getUnitName());
			record.setSTARTT(usageRec.getStartT());
			record.setENDT(usageRec.getEndT());
			record.setQUANTITY(usageRec.getQuantity());
			record.setAMOUNT(usageRec.getAmount());
			record.setCALLEDTO(usageRec.getCalledTo());
			record.setTERMINATECAUSE(usageRec.getTerminateCause());
			record.setIMPACTCATEGORY(usageRec.getImpactCategory());
			record.setUsageClass(usageRec.getUsageClass());
			int chargeCount = 0;
			for(UsageCharges charge : usageRec.getChargesList()) {
				chargeCount++;
				CHARGES c = new CHARGES();
				c.setAMOUNT(charge.getAmount());
				c.setOFFERNAME(charge.getOfferName());
				c.setQUANTITY(charge.getQuantity());
				c.setRESOURCEID(charge.getResourceId());
				c.setElem(chargeCount);
				record.getCharges().add(c);
			}
			count++;
			record.setElem(count);
			response.getUsageRecords().add(record);
			
		}
		
		response.setSTATUS(0);

	}


}
