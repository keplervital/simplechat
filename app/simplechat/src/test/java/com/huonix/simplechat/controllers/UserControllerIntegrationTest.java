package com.huonix.simplechat.controllers;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.datastax.driver.core.utils.UUIDs;
import com.huonix.simplechat.configs.APISecurityConfig;
import com.huonix.simplechat.configs.CassandraTestExecutionListener;
import com.huonix.simplechat.configs.EmbeddedCassandraConfig;
import com.huonix.simplechat.filters.APIKeyAuthFilter;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.services.UserService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import javax.naming.NamingException;

import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
		EmbeddedCassandraConfig.class, APISecurityConfig.class, UserService.class, UserController.class, APIKeyAuthFilter.class
}, initializers = ConfigFileApplicationContextInitializer.class)
@TestExecutionListeners({ 
	CassandraTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class 
})
@PropertySource(value = { "classpath:application.properties" })
@WebMvcTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
	
	private User user;
	
	@Value("${api.key.header}")
    private String apiKeyHeader;
	
    private MockMvc mvc;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private UserController userController;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Before
    public void setupMockMvc() throws NamingException {
		MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders
	        		.webAppContextSetup(webApplicationContext)
	        		.apply(springSecurity())
	                .build();
    }
	
	@Before
	public void setupFirstUser() {
		this.user = userService.add(new User(UUIDs.timeBased(), true, "John Doe"));
	}
	
	@Test
    public void whenUserControllerInjected_thenNotNull() throws Exception {
        assertNotNull(userController);
    }
	
	@Test
	public void getUserList_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/user/list")
			.header(apiKeyHeader, this.user.getAccessKey())
		    .contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void getUserListHasOneUser_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/user/list")
			.header(apiKeyHeader, this.user.getAccessKey())
		    .contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$", hasSize(1)));
	}
	
	@Test
	public void getUserIsListed_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/user/list")
			.header(apiKeyHeader, this.user.getAccessKey())
		    .contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$[0].id", is(this.user.getId().toString())));
	}
	
	@Test
	public void getUserById_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/user/" + this.user.getId())
			.header(apiKeyHeader, this.user.getAccessKey())
		    .contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(this.user.getId().toString())));
	}
	
	@Test
	public void getUserByIdNull_thenNotFound() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/user/" + UUIDs.timeBased())
			.header(apiKeyHeader, this.user.getAccessKey())
		    .contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void deleteUserById_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/user/" + this.user.getId())
			.header(apiKeyHeader, this.user.getAccessKey())
		    .contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.message", is("User successfully removed.")));
	}
	
	@Test
	public void deleteUserByIdNotFound_thenErrorMessages() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/user/" + UUIDs.timeBased())
			.header(apiKeyHeader, this.user.getAccessKey())
		    .contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.errors.length()", greaterThan(0)));
	}
	
	@Test
	public void getAuthenticatedUser_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/user/me")
			.header(apiKeyHeader, this.user.getAccessKey())
		    .contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(this.user.getId().toString())));
	}
	
}
