package com.oracle.oep.order.typeConverters.dynamic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StatusQueryFilterStringToMapConverter implements TypeConverters{
	
	public static Logger log = LogManager.getLogger(StatusQueryFilterStringToMapConverter.class);
    @Converter
    public Map<String, Object> toMap(String map) throws IOException {
    	
    	log.info("In StatusQueryFilterStringToMapConverter Converters"+map);
    	
    	 Map<String, Object> params = new HashMap<String, Object>();    	    	 
    	 
    	 params.put("acctId","${in.header.acctId}");
    	 params.put("status","${in.header.status}");
    	 
    	
    	 
        return params;
    }

}