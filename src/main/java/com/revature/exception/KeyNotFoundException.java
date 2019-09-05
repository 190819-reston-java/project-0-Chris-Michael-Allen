package com.revature.exception;

public class KeyNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KeyNotFoundException() {
		this("This key is not in the map!");
	}
	
	public KeyNotFoundException(String message) {
		super(message);
	}
}
