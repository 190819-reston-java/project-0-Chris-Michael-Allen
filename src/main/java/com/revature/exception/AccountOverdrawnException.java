package com.revature.exception;

public class AccountOverdrawnException extends RuntimeException {

	public AccountOverdrawnException() {
		this("Can not withdraw more funds than exist within account!");
	}
	
	public AccountOverdrawnException(String message) {
		super(message);
	}
}
