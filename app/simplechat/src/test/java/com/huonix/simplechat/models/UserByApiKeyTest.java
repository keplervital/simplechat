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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.datastax.driver.core.utils.UUIDs;
import com.huonix.simplechat.configs.CassandraTestExecutionListener;
import com.huonix.simplechat.configs.EmbeddedCassandraConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		EmbeddedCassandraConfig.class
}, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@TestExecutionListeners({ 
	CassandraTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class 
})
public class UserByApiKeyTest {

	@Test
    public void testUserByApiKeyIsEqual() {
		UUID id = UUIDs.timeBased();
		String key = id.toString().replace("-","");
		UserByApiKey u1 = new UserByApiKey(key, id);
		UserByApiKey u2 = new UserByApiKey(key, id);
		assertTrue("The users by api key provided must be equal", u1.equals(u2));
    }
	
	@Test
    public void testChangeKeySuccess() {
		UUID id = UUIDs.timeBased();
		UserByApiKey u1 = new UserByApiKey("Key", id);
		u1.setKey("New Key");
		assertEquals("The user key must be the new key", "New Key", u1.getKey());
    }
	
	@Test
    public void testChangeIdSuccess() {
		UUID id = UUIDs.timeBased();
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		u1.setUserId(id);
		assertEquals("The user id must be the new id", id, u1.getUserId());
    }
	
	@Test
    public void testHashCodeNotEqual() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		UserByApiKey u2 = new UserByApiKey("Key", UUIDs.timeBased());
		assertNotEquals("The user hashcode must not be equal", u1.hashCode(), u2.hashCode());
    }
	
	@Test
    public void testHashCodeNotEqualNullKey() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		UserByApiKey u2 = new UserByApiKey("Key", UUIDs.timeBased());
		u1.setKey(null);
		assertNotEquals("The user hashcode must not be equal", u1.hashCode(), u2.hashCode());
    }
	
	@Test
    public void testHashCodeNotEqualNullId() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		UserByApiKey u2 = new UserByApiKey("Key", UUIDs.timeBased());
		u1.setUserId(null);
		assertNotEquals("The user hashcode must not be equal", u1.hashCode(), u2.hashCode());
    }
	
	@Test
    public void testEqualSuccess() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		assertTrue("The user must be equal", u1.equals(u1));
    }
	
	@Test
    public void testNotEqualNull() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		assertFalse("The user must not be equal to null", u1.equals(null));
    }
	
	@Test
    public void testNotEqualDiffClass() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		assertFalse("The user must not be equal to another class", u1.equals(new Object()));
    }
	
	@Test
    public void testNotEqualNullKey() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		UserByApiKey u2 = new UserByApiKey("Key", UUIDs.timeBased());
		u1.setKey(null);
		assertFalse("The user must not be equal if key is null", u1.equals(u2));
    }
	
	@Test
    public void testNotEqualNullKeyOther() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		UserByApiKey u2 = new UserByApiKey("Key", UUIDs.timeBased());
		u2.setKey(null);
		assertFalse("The user must not be equal if key is null", u1.equals(u2));
    }
	
	@Test
    public void testNotEqualNullId() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		UserByApiKey u2 = new UserByApiKey("Key", UUIDs.timeBased());
		u1.setUserId(null);
		assertFalse("The user must not be equal if id is null", u1.equals(u2));
    }
	
	@Test
    public void testNotEqualNullIdOther() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		UserByApiKey u2 = new UserByApiKey("Key", UUIDs.timeBased());
		u1.setUserId(null);
		u2.setUserId(null);
		assertTrue("The user must be equal if id is null", u1.equals(u2));
    }
	
	@Test
    public void testNotEqualDiffId() {
		UserByApiKey u1 = new UserByApiKey("Key", UUIDs.timeBased());
		UserByApiKey u2 = new UserByApiKey("Key", UUIDs.timeBased());
		assertFalse("The user must not be equal if id is different", u1.equals(u2));
    }
	
}
