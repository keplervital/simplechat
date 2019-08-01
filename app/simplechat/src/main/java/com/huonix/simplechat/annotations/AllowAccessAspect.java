package com.huonix.simplechat.annotations;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.huonix.simplechat.enums.ERole;
import com.huonix.simplechat.exceptions.UnauthorizedAccessException;
import com.huonix.simplechat.helpers.AuthHelper;

/**
 * The Allow Access annotation responsible to check user access
 * 
 * @author Kepler Vital
 *
 */
@Aspect
@Component
public class AllowAccessAspect {
	
	static final String UNAUTHORIZED_ACCESS = "Unauthorized";

	/**
	 * Checks if the user has the access required
	 * 
	 * @param point ProceedingJoinPoint
	 * @param allowAccess
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(allowAccess)")
	public Object validate(ProceedingJoinPoint point, AllowAccess allowAccess) throws Throwable {
		try {
			boolean hasAccess = false;
			Set<String> userAuthorities = AuthHelper.userAuthorities();
			ERole[] roles = allowAccess.roles();
			for(ERole role : roles) {
				if(userAuthorities.contains(role.toString())) {
					hasAccess = true;
					break;
				}
			}
			if(!hasAccess && roles.length > 0) {
				throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
			}
		} catch(Exception e) {
			httpResponseMessage(e.getMessage());
			return null;
		}
		return point.proceed();
	}
	
	/**
	 * Sends a response to the request
	 * 
	 * @param message The message to show in the request
	 * @throws IOException
	 */
	protected void httpResponseMessage(String message) throws IOException {
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
				.getResponse();
		response.sendError(HttpStatus.FORBIDDEN.value(), message);
	}

}
