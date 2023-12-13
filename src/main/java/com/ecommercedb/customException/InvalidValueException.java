package com.ecommercedb.customException;

import java.io.IOException;

public class InvalidValueException  extends IOException{
	
	
	public InvalidValueException(String msg, Throwable cause) {
		super(msg,cause);
		
	}
	
	public InvalidValueException(String msg) {
		super(msg);
	}

}
