package com.huonix.simplechat.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.huonix.simplechat.enums.ERole;
import com.huonix.simplechat.exceptions.MissingAuthenticationHeadersException;
import com.huonix.simplechat.exceptions.UserNotFoundException;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.services.UserService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <h1>The filter that manages authentication</h1>
 * <p>
 * Retrieve data from the HEADER to check if current access is authorized.
 * </p>
 * 
 * @author Kepler Vital
 *
 */
@Component
@PropertySource(value = { "classpath:application.properties" })
public class APIKeyAuthFilter extends GenericFilterBean {
	
	@Autowired
	UserService userService;
	
	@Value("${api.key.header}")
    private String principalRequestHeader;

    protected String getApiKey(HttpServletRequest request) {
        return request.getHeader(principalRequestHeader);
    }
    
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String apiKey = getApiKey((HttpServletRequest) request);
			if(apiKey != null) {
				if(apiKey.isEmpty()) {
					throw new MissingAuthenticationHeadersException("Missing '" + principalRequestHeader + "' header.");
				}
				User user = userService.getByApiKey(apiKey);
				if(user == null) {
					throw new UserNotFoundException("API Key not found.");
				}
				Set<GrantedAuthority> authorities = new HashSet<>();
				authorities.add(new SimpleGrantedAuthority(ERole.DEFAULT.toString()));
				if(user.getAdmin()) {
					authorities.add(new SimpleGrantedAuthority(ERole.ADMIN.toString()));
				}
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, 
						user.getAccessKey(), authorities);
				auth.setDetails(user);
				SecurityContextHolder.getContext().setAuthentication(auth);
				chain.doFilter(request, response);
			} else {
				chain.doFilter(request, response);
			}
		} catch(MissingAuthenticationHeadersException e) {
			changeResponseToUnauthorized(response, e.getMessage());
		} catch(UserNotFoundException e) {
			changeResponseToUnauthorized(response, e.getMessage());
		} catch(Exception e) {
			changeResponseToUnauthorized(response, e.getMessage());
		}
	}
	
	/**
	 * Set the response to unauthorized.
	 * 
	 * @param response
	 * @throws IOException
	 */
	private void changeResponseToUnauthorized(ServletResponse response, String message) throws IOException {
		((HttpServletResponse) response).setContentType(MediaType.APPLICATION_JSON_VALUE);
		((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		((HttpServletResponse) response).getOutputStream().println("{ \"error\": \"UNAUTHORIZED\", \"message\": \""+message+"\" }");
	}
	
}
