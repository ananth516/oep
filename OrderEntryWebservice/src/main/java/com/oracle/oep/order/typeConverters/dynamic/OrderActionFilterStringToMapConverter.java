package com.oracle.oep.order.typeConverters.dynamic;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;

public class OrderActionFilterStringToMapConverter implements TypeConverters {	
	
	@Converter
    public Map toMap(String map) {
    	
    	System.out.println("In OrderActionFilterStringToMapConverter "+map);
    	
    	 Map params = new HashMap();    	    	 
    	 
    	 
    	
    	params.put("isPark","${in.body.get(\"isPark\")}");
    	params.put("isFuture","${in.body.get(\"isFuture\")}");
    	params.put("action","${in.body.get(\"action\")}");
    	params.put("bbAction","${in.body.get(\"bbAction\")}");
    	params.put("mobDisconnect","${in.body.get(\"mobDisconnect\")}");
        return params;
    }

}
