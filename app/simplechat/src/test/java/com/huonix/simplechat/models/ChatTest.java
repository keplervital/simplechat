package com.huonix.simplechat.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
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
public class ChatTest {
	
	@Test
	public void testHashCodeEqual() {
		Chat chat1 = new Chat("test", new HashSet<UUID>(), true);
		Chat chat2 = new Chat("test", new HashSet<UUID>(), true);
		chat1.setId(UUIDs.timeBased());
		chat2.setId(chat1.getId());
		assertEquals(chat1.hashCode(), chat2.hashCode());
	}
	
	@Test
	public void testHashCodeNullEqual() {
		Chat chat1 = new Chat("test", new HashSet<UUID>(), true);
		Chat chat2 = new Chat("test", new HashSet<UUID>(), true);
		chat1.setId(null);
		chat2.setId(null);
		assertEquals(chat1.hashCode(), chat2.hashCode());
	}
	
	@Test
	public void testEqualsSameOk() {
		Chat chat1 = new Chat("test", new HashSet<UUID>(), true);
		chat1.setId(UUIDs.timeBased());
		assertTrue(chat1.equals(chat1));
	}
	
	@Test
	public void testEqualsOk() {
		Chat chat1 = new Chat("test", new HashSet<UUID>(), true);
		Chat chat2 = new Chat("test", new HashSet<UUID>(), true);
		chat1.setId(UUIDs.timeBased());
		chat2.setId(chat1.getId());
		assertTrue(chat1.equals(chat2));
	}
	
	@Test
	public void testEqualsNull() {
		Chat chat1 = new Chat("test", new HashSet<UUID>(), true);
		assertFalse(chat1.equals(null));
	}
	
	@Test
	public void testEqualsDiffClass() {
		Chat chat1 = new Chat("test", new HashSet<UUID>(), true);
		assertFalse(chat1.equals(new Object()));
	}
	
	@Test
	public void testEqualsFalseIdNull() {
		Chat chat1 = new Chat("test", new HashSet<UUID>(), true);
		Chat chat2 = new Chat("test", new HashSet<UUID>(), true);
		chat1.setId(null);
		chat2.setId(UUIDs.timeBased());
		assertFalse(chat1.equals(chat2));
	}
	
	@Test
	public void testEqualsFalseIdBothNull() {
		Chat chat1 = new Chat("test", new HashSet<UUID>(), true);
		Chat chat2 = new Chat("test", new HashSet<UUID>(), true);
		chat1.setId(null);
		chat2.setId(null);
		assertTrue(chat1.equals(chat2));
	}
	
	@Test
	public void testEqualsFalseIdDiff() {
		Chat chat1 = new Chat("test", new HashSet<UUID>(), true);
		Chat chat2 = new Chat("test", new HashSet<UUID>(), true);
		chat1.setId(UUIDs.timeBased());
		chat2.setId(UUIDs.timeBased());
		assertFalse(chat1.equals(chat2));
	}
	
}
