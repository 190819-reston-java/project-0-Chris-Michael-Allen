package com.revature.exception;

public class NoUserTargetedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoUserTargetedException() {
		this("Can not perform functions without looking at a specific user!");
	}
	
	public NoUserTargetedException(String message) {
		super(message);
	}

}
