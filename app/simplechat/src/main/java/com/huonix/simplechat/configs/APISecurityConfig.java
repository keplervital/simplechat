package com.huonix.simplechat.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import com.huonix.simplechat.enums.ERole;
import com.huonix.simplechat.filters.APIKeyAuthFilter;

@Configuration
@EnableWebSecurity
@Order(1)
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	APIKeyAuthFilter apiKeyAuthFilter;
	
	static final String AUTH_ROUTE = "/**";

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        	.csrf()
        	.disable()
        	.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	.and()
        	//.antMatcher(AUTH_ROUTE)
        	.addFilterBefore(apiKeyAuthFilter, AnonymousAuthenticationFilter.class)
        	.authorizeRequests()
        	//.antMatchers(HttpMethod.POST, USER_CREATE_ROUTE).permitAll()
        	.antMatchers(AUTH_ROUTE).hasAuthority(ERole.DEFAULT.toString());
        	//.authenticated();
    }
	
}
