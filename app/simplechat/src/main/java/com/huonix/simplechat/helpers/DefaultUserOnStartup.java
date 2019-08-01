package com.huonix.simplechat.helpers;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.utils.UUIDs;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.services.UserService;

@Component
@PropertySource(value = { "classpath:application.properties" })
public class DefaultUserOnStartup {

	@Autowired
	private Environment environment;
	
	@Autowired
	private UserService userService;
	
	private static final Log LOGGER = LogFactory.getLog(DefaultUserOnStartup.class);
	 
    @PostConstruct
    private void init() {
    	LOGGER.info("AppInitializator initialization logic ...");
        if(userService.count() == 0) {
        	createDefaultUser();
        }
    }
    
    /**
     * Creates the app default admin user
     */
    private void createDefaultUser() {
    	User user = new User(UUIDs.timeBased(), true, environment.getProperty("default.user.name"));
    	user = userService.add(user);
    	LOGGER.info("##############################################");
    	LOGGER.info("# SIMPLECHAT DEFAULT USER CREATED");
    	LOGGER.info("# id: " + user.getId());
    	LOGGER.info("# name: " + user.getName());
    	LOGGER.info("# apiKey: " + user.getAccessKey());
    	LOGGER.info("##############################################");
    }
	
}