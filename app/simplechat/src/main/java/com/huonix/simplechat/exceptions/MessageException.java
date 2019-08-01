package com.huonix.simplechat.exceptions;

/**
 * 
 * @author Kepler Vital
 *
 */
public class MessageException extends Exception {

	private static final long serialVersionUID = 1L;

	public MessageException() {
		super();
	}
	
	public MessageException(String message) {
		super(message);
	}
	
}
