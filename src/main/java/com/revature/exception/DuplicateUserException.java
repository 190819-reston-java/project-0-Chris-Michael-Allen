package com.revature.exception;

public class DuplicateUserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateUserException() {
		this("Can not create two users with the same name in the table!");
	}
	
	public DuplicateUserException(String message) {
		super(message);
	}
}
