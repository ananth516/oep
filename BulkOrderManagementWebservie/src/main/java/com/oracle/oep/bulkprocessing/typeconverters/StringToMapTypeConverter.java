package com.oracle.oep.bulkprocessing.typeconverters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Converter
public class StringToMapTypeConverter {
	
	public static Logger log = LogManager.getLogger(StringToMapTypeConverter.class);
    @Converter
    public Map<String, Object> toMap(String map) throws IOException {
    	
    	log.info("In String Type Converters"+map);
    	
    	 Map<String, Object> params = new HashMap<String, Object>();    	    	 
    	 
    	 params.put("param","${in.body.get(\"param\")}"); 
    	 
        return params;
    }

}
