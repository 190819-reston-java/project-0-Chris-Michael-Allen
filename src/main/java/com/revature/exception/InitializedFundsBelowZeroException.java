package com.revature.exception;

public class InitializedFundsBelowZeroException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitializedFundsBelowZeroException() {
		this("Funds can not be initialized as a negative!");
	}
	
	public InitializedFundsBelowZeroException(String message) {
		super(message);
	}
}
