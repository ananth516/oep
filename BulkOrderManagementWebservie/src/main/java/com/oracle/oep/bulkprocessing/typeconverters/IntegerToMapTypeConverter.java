package com.oracle.oep.bulkprocessing.typeconverters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Converter
public class IntegerToMapTypeConverter {
	
	public static Logger log = LogManager.getLogger(IntegerToMapTypeConverter.class);
    @Converter
    public Map<String, Object> toMap(Integer map) throws IOException {
    	
    	log.info("In Integer Type Converters"+map);
    	
    	 Map<String, Object> params = new HashMap<String, Object>();    	    	 
    	 
    	 params.put("param","${in.body.get(param)}");
    	 
        return params;
    }

}
