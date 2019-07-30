package com.huonix.simplechat.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.huonix.simplechat.enums.ERole;

/**
 * Used to grant access to certain roles
 * 
 * @author Kepler Vital
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowAccess {

	ERole[] roles() default ERole.GUEST;
	
}
