package com.huonix.simplechat.exceptions;

public class UnauthorizedAccessException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedAccessException() {
		super();
	}
	
	public UnauthorizedAccessException(String message) {
		super(message);
	}
	
}
