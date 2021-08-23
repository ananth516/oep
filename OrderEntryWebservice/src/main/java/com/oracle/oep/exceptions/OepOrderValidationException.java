package com.oracle.oep.exceptions;

public class OepOrderValidationException extends RuntimeException {

	private static final long serialVersionUID = -3771465127548493728L;
	
	public OepOrderValidationException(String message){
		
		super(message);
	}


}
