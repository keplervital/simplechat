package com.huonix.simplechat.exceptions;

/**
 * Throw when user not found
 * 
 * @author Kepler Vital
 *
 */
public class UserNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException() {
		super();
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}
	
}
