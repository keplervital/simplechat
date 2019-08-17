package com.huonix.simplechat;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.huonix.simplechat.configs.DockerContainer;
import com.huonix.simplechat.controllers.ChatControllerIntegrationTest;
import com.huonix.simplechat.controllers.MessageControllerIntegrationTest;
import com.huonix.simplechat.controllers.UserControllerIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	ChatControllerIntegrationTest.class,
	MessageControllerIntegrationTest.class,
	UserControllerIntegrationTest.class
})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test"})
public class IntegrationTests {

	@AfterClass
    public static void tearDown() {
		DockerContainer.removeAll();
    }

}
