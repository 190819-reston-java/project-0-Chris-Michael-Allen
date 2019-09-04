package com.revature.exception;

public class KeyNotFoundException extends RuntimeException {

	public KeyNotFoundException() {
		this("This key is not in the map!");
	}
	
	public KeyNotFoundException(String message) {
		super(message);
	}
}
