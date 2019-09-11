package com.revature.exception;

public class DatabaseProtectionFromTestDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatabaseProtectionFromTestDataException() {
		this("Will not update database with test data!");
	}
	
	public DatabaseProtectionFromTestDataException(String message) {
		super(message);
	}
}
