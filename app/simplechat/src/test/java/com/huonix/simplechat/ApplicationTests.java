package com.huonix.simplechat;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.huonix.simplechat.configs.CassandraTestExecutionListener;
import com.huonix.simplechat.configs.DockerContainer;
import com.huonix.simplechat.configs.EmbeddedCassandraConfig;
import com.huonix.simplechat.configs.EmbeddedRabbitConfig;
import com.huonix.simplechat.configs.EmbeddedRedisConfig;
import com.huonix.simplechat.enums.ERoleTest;
import com.huonix.simplechat.exceptions.ChatCreationErrorExceptionTest;
import com.huonix.simplechat.exceptions.ChatLeaveExceptionTest;
import com.huonix.simplechat.exceptions.ChatUpdateExceptionTest;
import com.huonix.simplechat.exceptions.MessageExceptionTest;
import com.huonix.simplechat.exceptions.MissingAuthenticationHeadersExceptionTest;
import com.huonix.simplechat.exceptions.UnauthorizedAccessExceptionTest;
import com.huonix.simplechat.exceptions.UserNameNotUniqueExceptionTest;
import com.huonix.simplechat.exceptions.UserNotFoundExceptionTest;
import com.huonix.simplechat.helpers.ApplicationFirstStartupTest;
import com.huonix.simplechat.helpers.AuthHelperTest;
import com.huonix.simplechat.helpers.ErrorHandlerTest;
import com.huonix.simplechat.models.ChatParticipantTest;
import com.huonix.simplechat.models.ChatTest;
import com.huonix.simplechat.models.MessageTest;
import com.huonix.simplechat.models.UserByApiKeyTest;
import com.huonix.simplechat.models.UserByNameTest;
import com.huonix.simplechat.models.UserTest;
import com.huonix.simplechat.services.ChatServiceTest;
import com.huonix.simplechat.services.MessageServiceTest;
import com.huonix.simplechat.services.UserServiceTest;

@RunWith(Suite.class)
@ContextConfiguration(classes = {
		EmbeddedCassandraConfig.class, 
		EmbeddedRedisConfig.class,
		EmbeddedRabbitConfig.class
}, initializers = ConfigFileApplicationContextInitializer.class)
@TestExecutionListeners({ 
	CassandraTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class 
})
@SuiteClasses({ 
	ChatServiceTest.class,
	MessageServiceTest.class,
	UserServiceTest.class,
	UserTest.class,
	ChatTest.class,
	ChatParticipantTest.class,
	MessageTest.class,
	UserByApiKeyTest.class,
	UserByNameTest.class,
	ApplicationFirstStartupTest.class,
	AuthHelperTest.class,
	ErrorHandlerTest.class,
	MissingAuthenticationHeadersExceptionTest.class,
	MessageExceptionTest.class,
	ChatUpdateExceptionTest.class,
	ChatLeaveExceptionTest.class,
	ChatCreationErrorExceptionTest.class,
	UnauthorizedAccessExceptionTest.class,
	UserNameNotUniqueExceptionTest.class,
	UserNotFoundExceptionTest.class,
	ERoleTest.class
})
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class ApplicationTests {

	@AfterClass
    public static void tearDown() {
		DockerContainer.removeAll();
    }

}
