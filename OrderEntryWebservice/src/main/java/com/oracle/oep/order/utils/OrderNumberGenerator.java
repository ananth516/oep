package com.oracle.oep.order.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.CRC32;

import org.apache.camel.ProducerTemplate;

public final class OrderNumberGenerator {
	
	private static String toCRC32HexString(String str)
	  {
	    byte[] bytes = str.getBytes();
	    CRC32 x = new CRC32();
	    x.update(bytes);
	    String formattedStr = Long.toHexString(x.getValue());
	    //System.out.println("CRC32 (via Java's library)     = " + formattedStr);
	    return formattedStr;
	  }
	
	//Generate unique order number 
	/*public static String generateOrderNumber(){
		
		UUID id = UUID.randomUUID();       
	       
	    SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");	     
	    Date date= new Date();   
	   	    
	    String hexvalue = toCRC32HexString(id.toString());	    
	    hexvalue=formatter.format(date)+"-"+hexvalue;
	    
	    date=null;
	    formatter=null;    
	    
		return hexvalue;
	}*/
		
	public static String generateOrderNumber(ProducerTemplate template){
		
		UUID id = UUID.randomUUID();       
	       
	    SimpleDateFormat formatter = new SimpleDateFormat("dd");	     
	    Date date= new Date(); 
	    String prefix = "01";
	   	    
	    Map<String,Object> params = new HashMap<String,Object>();
	    
	    Object object= template.requestBody("direct:getOEPOrderSequence", params);
	    
	    if(object!=null){
	    	
	    	List<Map<String, Object>> dbsequence = (List<Map<String, Object>>) object; 
	    	
	    	return prefix+dbsequence.get(0).get("NEXTVAL");
	    	
	    }
	    
	    
	    
	    return new Long(Integer.toUnsignedLong(id.hashCode())+Long.parseLong(formatter.format(date).toString())).toString();
	}
	
	
}
