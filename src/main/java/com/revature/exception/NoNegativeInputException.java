package com.revature.exception;

public class NoNegativeInputException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoNegativeInputException() {
		this("Can not make deposits or withdrawals with negative numbers!");
	}
	
	public NoNegativeInputException(String message) {
		super(message);
	}
}
