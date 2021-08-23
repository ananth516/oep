package com.oracle.oep.order.utils;

import org.w3c.dom.Document;

public class TransformXqueryResponseToString {
	
public static String stringTransfomer(Document message){
	
	return message.getDocumentElement().getTextContent();
}

}
