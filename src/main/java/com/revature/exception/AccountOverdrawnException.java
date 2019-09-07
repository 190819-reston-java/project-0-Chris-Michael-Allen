package com.revature.exception;

public class AccountOverdrawnException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountOverdrawnException() {
		this("Can not withdraw more funds than exist within account!");
	}
	
	public AccountOverdrawnException(String message) {
		super(message);
	}
}
