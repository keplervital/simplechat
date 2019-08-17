package com.huonix.simplechat.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.datastax.driver.core.utils.UUIDs;
import com.huonix.simplechat.configs.CassandraTestExecutionListener;
import com.huonix.simplechat.configs.EmbeddedCassandraConfig;
import com.huonix.simplechat.configs.EmbeddedRabbitConfig;
import com.huonix.simplechat.configs.EmbeddedRedisConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		EmbeddedCassandraConfig.class, 
		EmbeddedRedisConfig.class,
		EmbeddedRabbitConfig.class
}, initializers = ConfigFileApplicationContextInitializer.class)
@TestExecutionListeners({ 
	CassandraTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class 
})
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class UserTest {
	
	@Test
    public void testUserIsEqual() {
		UUID id = UUIDs.timeBased();
		User u1 = new User(id, true, "John Doe");
		User u2 = new User(id, true, "John Doe");
		assertTrue("The users provided must be equal", u1.equals(u2));
    }
	
	@Test
    public void testUserIsNotEqual() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		User u2 = new User(UUIDs.timeBased(), true, "John Doe");
		assertFalse("The users provided must not be equal", u1.equals(u2));
    }
	
	@Test
    public void testUserIsNotEqualNullUser() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		assertFalse("The users provided must not be equal", u1.equals(null));
    }
	
	@Test
    public void testUserIsNotEqualIdNull() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		User u2 = new User(UUIDs.timeBased(), true, "John Doe");
		u1.setId(null);
		assertFalse("The users provided must not be equal", u1.equals(u2));
    }
	
	@Test
    public void testUserIsNotEqualIdNullOtherUser() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		User u2 = new User(UUIDs.timeBased(), true, "John Doe");
		u2.setId(null);
		assertFalse("The users provided must not be equal", u1.equals(u2));
    }
	
	@Test
    public void testUserIsNotEqualDiffClass() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		assertFalse("The users provided must not be equal", u1.equals(new Object()));
    }
	
	@Test
    public void testUserHashCodeEqual() {
		UUID id = UUIDs.timeBased();
		User u1 = new User(id, true, "John Doe");
		User u2 = new User(id, true, "John Doe");
		assertEquals("The users hashcode provided must be equal", u1.hashCode(), u2.hashCode());
    }
	
	@Test
    public void testUserHashCodeNotEqual() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		User u2 = new User(UUIDs.timeBased(), true, "John Doe");
		assertNotEquals("The users hashcode provided must not be equal", u1.hashCode(), u2.hashCode());
    }
	
	@Test
    public void testUserHashCodeNotEqualNullId() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		User u2 = new User(UUIDs.timeBased(), true, "John Doe");
		u1.setId(null);
		assertNotEquals("The users hashcode provided must not be equal", u1.hashCode(), u2.hashCode());
    }
	
	@Test
    public void testDateAddedIsNull() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		assertEquals("The user date added must be null", null, u1.getDateAdded());
    }
	
	@Test
    public void testDateModifiedIsNull() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		assertEquals("The user date modified must be null", null, u1.getDateModified());
    }
	
	@Test
    public void testAccessKeyIsNull() {
		User u1 = new User(UUIDs.timeBased(), true, "John Doe");
		u1.setAccessKey(null);
		assertEquals("The user access key must be null", null, u1.getAccessKey());
    }

}
