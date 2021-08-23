package com.oracle.oep.order.typeConverters.dynamic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ServiceNoAndOrderActionFilterStringToMapConverter implements TypeConverters{
	
	public static Logger log = LogManager.getLogger(ServiceNoAndOrderActionFilterStringToMapConverter.class);
    @Converter
    public Map<String, Object> toMap(String map) throws IOException {
    	
    	log.info("In ServiceNoAndOrderActionFilterStringToMapConverter Converters"+map);
    	
    	 Map<String, Object> params = new HashMap<String, Object>();    	    	 
    	 
    	 
    	 params.put("serviceNumber","${in.header.serviceNumber}");
    	 params.put("actions","${in.header.actions}");
    	
    	 
        return params;
    }

}