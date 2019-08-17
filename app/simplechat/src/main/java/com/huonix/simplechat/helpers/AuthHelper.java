package com.huonix.simplechat.helpers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.datastax.driver.core.utils.UUIDs;
import com.huonix.simplechat.models.User;

/**
 * Authentication Helpers
 * 
 * @author Kepler Vital
 *
 */
public class AuthHelper {
	
	public static User offlineUser = new User(UUIDs.timeBased(), false, "guest");
	
	/**
	 * Loads the authenticated user authorities
	 * 
	 * @return Set<String>
	 */
	public static Set<String> userAuthorities() {
		Set<String> userAuthorities = new HashSet<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			for(Object authority : auth.getAuthorities()) {
				userAuthorities.add(((GrantedAuthority)authority).getAuthority());
			}
		} catch(Exception e) {
			return userAuthorities;
		}
		return userAuthorities;
	}
	
	/**
	 * Loads the authenticated user authorities
	 * 
	 * @return Set<String>
	 */
	public static User user() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			return (User) auth.getDetails();
		} catch(Exception e) {
			return offlineUser;
		}
	}
	
}
