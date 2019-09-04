package com.revature.exception;

public class FundsTooHighException extends RuntimeException {

	public FundsTooHighException(){
		this("Can not enter an amount that exceeds 1000000!");
	}
	
	public FundsTooHighException(String message){
		super(message);
	}
}
