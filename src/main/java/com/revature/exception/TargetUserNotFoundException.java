package com.revature.exception;

public class TargetUserNotFoundException extends RuntimeException {

	public TargetUserNotFoundException() {
		this("Can not perform this action without targeting a user!");
	}
	
	public TargetUserNotFoundException(String message) {
		super(message);
	}
}
