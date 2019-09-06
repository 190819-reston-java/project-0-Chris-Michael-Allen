package com.revature.exception;

public class DuplicateUserException extends RuntimeException {

	public DuplicateUserException() {
		this("Can not create two users with the same name in the table!");
	}
	
	public DuplicateUserException(String message) {
		super(message);
	}
}
