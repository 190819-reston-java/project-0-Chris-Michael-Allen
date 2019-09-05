package com.revature.exception;

public class FundsTooHighException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FundsTooHighException(){
		this("Can not enter an amount that exceeds 1000000!");
	}
	
	public FundsTooHighException(String message){
		super(message);
	}
}
