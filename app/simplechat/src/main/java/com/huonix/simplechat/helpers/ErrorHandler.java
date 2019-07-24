package com.huonix.simplechat.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Kepler Vital
 *
 */
public abstract class ErrorHandler {

	private List<String> errors = new ArrayList<>();

	/**
	 * Clears all the errors
	 * 
	 * @return void
	 */
	public void clearErrors() {
		this.errors.clear();
	}
	
	/**
	 * Gets all the error messages
	 * 
	 * @return List<String> with all the errors
	 */
	public List<String> getErrors() {
		return this.errors;
	}
	
	/**
	 * Adds and error
	 * 
	 * @param error 
	 * @return this instance
	 */
	public ErrorHandler addError(String error) {
		this.errors.add(error);
		return this;
	}
	
	/**
	 * Check to see if and error exists
	 * 
	 * @return boolean
	 */
	public boolean existsErrors() {
		return !this.errors.isEmpty();
	}
	
}
