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

/**
 * Application initialization logic
 * 
 * @author Kepler Vital
 *
 */
@Component
@PropertySource(value = { "classpath:application.properties" })
public class ApplicationFirstStartup {

	@Autowired
	private Environment environment;
	
	@Autowired
	private UserService userService;
	
	private static final Log LOGGER = LogFactory.getLog(ApplicationFirstStartup.class);
	 
    @PostConstruct
    public void init() {
    	LOGGER.info("##############################################");
    	LOGGER.info("# APPLICATION INITIALIZATION LOGIC");
        if(userService.count() == 0) {
        	createDefaultRootUser();
        	createDefaultAdminUser();
        }
        LOGGER.info("##############################################");
    }
    
    /**
     * Creates the app default admin user
     */
    private void createDefaultRootUser() {
    	User user = new User(UUIDs.timeBased(), true, environment.getProperty("simplechat.user.root"));
    	user = userService.add(user);
    	LOGGER.info("##############################################");
    	LOGGER.info("# SIMPLECHAT DEFAULT ROOT USER");
    	LOGGER.info("# id: " + user.getId());
    	LOGGER.info("# name: " + user.getName());
    	LOGGER.info("# apiKey: " + user.getAccessKey());
    	LOGGER.info("##############################################");
    }
    
    /**
     * Creates the app default admin user
     */
    private void createDefaultAdminUser() {
    	User user = new User(UUIDs.timeBased(), true, environment.getProperty("simplechat.user.admin"));
    	user = userService.add(user);
    	LOGGER.info("##############################################");
    	LOGGER.info("# SIMPLECHAT DEFAULT ADMIN USER");
    	LOGGER.info("# id: " + user.getId());
    	LOGGER.info("# name: " + user.getName());
    	LOGGER.info("# apiKey: " + user.getAccessKey());
    	LOGGER.info("##############################################");
    }
	
}