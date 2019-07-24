package com.huonix.simplechat.exceptions;

/**
 * Throw when user not found
 * 
 * @author Kepler Vital
 *
 */
public class UserNameNotUniqueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNameNotUniqueException(String message) {
		super(message);
	}
	
}
