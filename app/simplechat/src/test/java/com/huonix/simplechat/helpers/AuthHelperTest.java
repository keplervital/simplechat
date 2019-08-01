package com.huonix.simplechat.helpers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.huonix.simplechat.configs.CassandraTestExecutionListener;
import com.huonix.simplechat.configs.EmbeddedCassandraConfig;
import com.huonix.simplechat.enums.ERole;
import com.huonix.simplechat.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		EmbeddedCassandraConfig.class
}, initializers = ConfigFileApplicationContextInitializer.class)
@TestExecutionListeners({ 
	CassandraTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class 
})
@SpringBootTest(classes = { UserService.class })
public class AuthHelperTest {
	
	@Test
    public void testInstantiateAuthHelper() {
		assertNotNull(new AuthHelper());
    }

	@Test
    public void testEmptyAuthorities() {
		SecurityContextHolder.getContext().setAuthentication(null);
		Set<String> authorities = AuthHelper.userAuthorities();
		assertTrue("The authorities list should be empty", authorities.isEmpty());
    }
	
	@Test
    public void testNotEmptyAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(ERole.DEFAULT.toString()));
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, 
				null, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
		Set<String> list = AuthHelper.userAuthorities();
		assertTrue("The authorities list should not be empty", !list.isEmpty());
    }
	
}
