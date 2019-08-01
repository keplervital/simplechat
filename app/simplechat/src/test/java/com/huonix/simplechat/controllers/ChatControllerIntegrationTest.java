package com.huonix.simplechat.controllers;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.huonix.simplechat.helpers.AuthHelper;
import com.huonix.simplechat.models.Chat;
import com.huonix.simplechat.models.Message;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.services.ChatService;
import com.huonix.simplechat.services.MessageService;
import com.huonix.simplechat.services.UserService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
		EmbeddedCassandraConfig.class, 
		APISecurityConfig.class, 
		MessageService.class, 
		ChatController.class, 
		ChatService.class,
		UserService.class,
		APIKeyAuthFilter.class
}, initializers = ConfigFileApplicationContextInitializer.class)
@TestExecutionListeners({ 
	CassandraTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class 
})
@PropertySource(value = { "classpath:application.properties" })
@WebMvcTest
@AutoConfigureMockMvc
public class ChatControllerIntegrationTest {

	private User admin;
	private List<User> users;
	private Chat chat;
	private Chat groupChat;
	private Message message;
	
	@Value("${api.key.header}")
    private String apiKeyHeader;
	
    private MockMvc mvc;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private ChatService chatService;
	
	@Autowired
    private MessageService messageService;
	
	@Autowired
    private ChatController chatController;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Before
    public void setupMockMvc() throws NamingException {
        this.mvc = MockMvcBuilders
	        		.webAppContextSetup(webApplicationContext)
	        		.apply(springSecurity())
	                .build();
    }
	
	@Before
	public void setupData() {
		this.admin = userService.add(new User(UUIDs.timeBased(), true, "John Doe"));

		this.users = new ArrayList<>();
		this.users.add(userService.add(new User(UUIDs.timeBased(), false, "Mike Daniel")));
		this.users.add(userService.add(new User(UUIDs.timeBased(), false, "Maria Esperanza")));
		
		AuthHelper.offlineUser = this.admin;
		
		this.chat = chatService.addSimple(this.users.get(0).getId());
		this.groupChat = chatService.add(new Chat("Group", new HashSet<UUID>(), true));
		this.message = new Message(this.chat.getId(), this.admin.getId(), "test message");
		
		messageService.add(this.message);		
	}
	
	@Test
    public void whenControllerInjected_thenNotNull() throws Exception {
        assertNotNull(chatController);
    }
	
	@Test
    public void getBar_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/chat/bar")
				.header(apiKeyHeader, this.admin.getAccessKey())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.directs.length()", greaterThan(0)))
				.andExpect(jsonPath("$.users.length()", greaterThan(0)));
    }
	
	@Test
    public void getMessages_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/chat/"+this.chat.getId()+"/messages")
				.header(apiKeyHeader, this.admin.getAccessKey())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()", greaterThan(0)));
    }
	
	@Test
    public void createDirect_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/chat/with/" + this.users.get(1).getId())
				.header(apiKeyHeader, this.admin.getAccessKey())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errors").doesNotExist());
    }
	
	@Test
    public void createDirect_NotOk() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/chat/with/" + this.admin.getId())
				.header(apiKeyHeader, this.admin.getAccessKey())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errors").exists());
    }
	
	@Test
    public void create_Ok() throws Exception {
		JSONObject jsonData = new JSONObject();
		jsonData.put("name", "Just testing!");
		mvc.perform(MockMvcRequestBuilders.post("/chat")
				.header(apiKeyHeader, this.admin.getAccessKey())
				.content(jsonData.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errors").doesNotExist());
    }
	
	@Test
    public void create_NotOk() throws Exception {
		JSONObject jsonData = new JSONObject();
		mvc.perform(MockMvcRequestBuilders.post("/chat")
				.header(apiKeyHeader, this.admin.getAccessKey())
				.content(jsonData.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errors").exists());
    }
	
	@Test
    public void update_Ok() throws Exception {
		JSONObject jsonData = new JSONObject();
		jsonData.put("name", "Just testing!");
		mvc.perform(MockMvcRequestBuilders.put("/chat/" + this.groupChat.getId())
				.header(apiKeyHeader, this.admin.getAccessKey())
				.content(jsonData.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errors").doesNotExist());
    }

	@Test
    public void update_NotOk() throws Exception {
		JSONObject jsonData = new JSONObject();
		jsonData.put("name", "Just testing!");
		mvc.perform(MockMvcRequestBuilders.put("/chat/" + UUIDs.timeBased())
				.header(apiKeyHeader, this.admin.getAccessKey())
				.content(jsonData.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errors").exists());
    }
	
	@Test
    public void leave_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/chat/" + this.groupChat.getId() + "/leave")
				.header(apiKeyHeader, this.admin.getAccessKey())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errors").doesNotExist());
    }
	
	@Test
    public void leave_NotOk() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/chat/" + UUIDs.timeBased() + "/leave")
				.header(apiKeyHeader, this.admin.getAccessKey())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errors").exists());
    }
	
	@Test
    public void leave_NotPresent() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/chat/" + this.groupChat.getId() + "/leave")
				.header(apiKeyHeader, this.users.get(0).getAccessKey())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.errors").exists());
    }
	
}
