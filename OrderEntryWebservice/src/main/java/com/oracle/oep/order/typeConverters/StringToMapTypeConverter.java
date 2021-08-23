package com.oracle.oep.order.typeConverters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Converter
public class StringToMapTypeConverter implements TypeConverters {
	
	public static Logger log = LogManager.getLogger(StringToMapTypeConverter.class);
    @Converter
    public Map<String, Object> toMap(String map) throws IOException {
    	
    	log.info("In Type Converters"+map);
    	
    	Map<String, Object> params = new HashMap<String, Object>(); 
    	
    	if(map!=null && map.contains("orderQueryFilterString")){
    		
    		params.put("acctId","${in.header.acctId}");
       	 	params.put("orderId","${in.header.orderId}");
    	}
    	else if(map!=null && map.contains("serviceNoAndAccountNo")){
    		
    		params.put("acctId","${in.header.acctId}");
    		params.put("serviceNumber","${in.header.serviceNumber}");
    	}
    	
    	else if(map!=null && map.contains("accountNoAndStatusQueryFilter")){
    		
    		params.put("acctId","${in.header.acctId}");
       	 	params.put("status","${in.header.status}");
    	}
    	 
    	else if(map!=null && map.contains("creationDateQueryFilter")){
    		
    		 params.put("accountId","${in.header.accountId}");
        	 params.put("creationDateMin","${in.header.creationDateMin}");
        	 params.put("creationDateMax","${in.header.creationDateMax}");
    	}
    	else if(map!=null && map.contains("serviceNoAndOrderAction")){
    		
    		params.put("serviceNumber","${in.header.serviceNumber}");
       	 	params.put("actions","${in.header.actions}");
    	}
    	else if(map!=null && map.contains("orderActionFilter")){
    		
    		params.put("isPark","${in.body.get(\"isPark\")}");
        	params.put("isFuture","${in.body.get(\"isFuture\")}");
        	params.put("action","${in.body.get(\"action\")}");
        	params.put("bbAction","${in.body.get(\"bbAction\")}");
        	params.put("mobDisconnect","${in.body.get(\"mobDisconnect\")}");
        	params.put("mobileDisconnectFutureDate","${in.body.get(\"mobileDisconnectFutureDate\")}");
    	}
    	
    	else
    		params.put("param","${in.body.get(\"param\")}"); 
    	
    	 
        return params;
    }    
    
}
