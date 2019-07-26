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
public class UserByNameTest {

	@Test
    public void testUserByNameIsEqual() {
		UUID id = UUIDs.timeBased();
		UserByName u1 = new UserByName("John Doe", id);
		UserByName u2 = new UserByName("John Doe", id);
		assertTrue("The users by name provided must be equal", u1.equals(u2));
    }
	
	@Test
    public void testChangeNameSuccess() {
		UUID id = UUIDs.timeBased();
		UserByName u1 = new UserByName("John Doe", id);
		u1.setName("New Name");
		assertEquals("The user name must be the new name", "New Name", u1.getName());
    }
	
	@Test
    public void testChangeIdSuccess() {
		UUID id = UUIDs.timeBased();
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		u1.setUserId(id);
		assertEquals("The user id must be the new id", id, u1.getUserId());
    }
	
	@Test
    public void testHashCodeNotEqual() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		UserByName u2 = new UserByName("John Doe", UUIDs.timeBased());
		assertNotEquals("The user hashcode must not be equal", u1.hashCode(), u2.hashCode());
    }
	
	@Test
    public void testHashCodeNotEqualNullName() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		UserByName u2 = new UserByName("John Doe", UUIDs.timeBased());
		u1.setName(null);
		assertNotEquals("The user hashcode must not be equal", u1.hashCode(), u2.hashCode());
    }
	
	@Test
    public void testHashCodeNotEqualNullId() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		UserByName u2 = new UserByName("John Doe", UUIDs.timeBased());
		u1.setUserId(null);
		assertNotEquals("The user hashcode must not be equal", u1.hashCode(), u2.hashCode());
    }
	
	@Test
    public void testEqualSuccess() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		assertTrue("The user must be equal", u1.equals(u1));
    }
	
	@Test
    public void testNotEqualNull() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		assertFalse("The user must not be equal to null", u1.equals(null));
    }
	
	@Test
    public void testNotEqualDiffClass() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		assertFalse("The user must not be equal to another class", u1.equals(new Object()));
    }
	
	@Test
    public void testNotEqualNullName() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		UserByName u2 = new UserByName("John Doe", UUIDs.timeBased());
		u1.setName(null);
		assertFalse("The user must not be equal if name is null", u1.equals(u2));
    }
	
	@Test
    public void testNotEqualNullNameOther() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		UserByName u2 = new UserByName("John Doe", UUIDs.timeBased());
		u2.setName(null);
		assertFalse("The user must not be equal if name is null", u1.equals(u2));
    }
	
	@Test
    public void testNotEqualNullId() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		UserByName u2 = new UserByName("John Doe", UUIDs.timeBased());
		u1.setUserId(null);
		assertFalse("The user must not be equal if id is null", u1.equals(u2));
    }
	
	@Test
    public void testNotEqualNullIdOther() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		UserByName u2 = new UserByName("John Doe", UUIDs.timeBased());
		u1.setUserId(null);
		u2.setUserId(null);
		assertTrue("The user must be equal if id is null", u1.equals(u2));
    }
	
	@Test
    public void testNotEqualDiffId() {
		UserByName u1 = new UserByName("John Doe", UUIDs.timeBased());
		UserByName u2 = new UserByName("John Doe", UUIDs.timeBased());
		assertFalse("The user must not be equal if id is different", u1.equals(u2));
    }
	
}
