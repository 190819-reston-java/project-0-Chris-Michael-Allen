package com.revature.exception;

public class InvalidPasswordException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
		this("Can not retrieve user data with an invalid password!");
	}
	
	public InvalidPasswordException(String message) {
		super(message);
	}
}
