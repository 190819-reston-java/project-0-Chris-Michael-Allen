package com.revature.exception;

public class InvalidPasswordException extends RuntimeException {

	public InvalidPasswordException() {
		this("Can not retrieve user data with an invalid password!");
	}
	
	public InvalidPasswordException(String message) {
		super(message);
	}
}
