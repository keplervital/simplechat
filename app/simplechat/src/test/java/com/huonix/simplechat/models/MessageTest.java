package com.huonix.simplechat.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
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
public class MessageTest {

	private UUID chatId;
	private UUID userId;
	private Message message1;
	private Message message2;
	
	@Before
	public void setupData() {
		this.chatId = UUIDs.timeBased();
		this.userId = UUIDs.timeBased();
		this.message1 = new Message(chatId, userId, "test");
		this.message2 = new Message(chatId, userId, "test2");
	}
	
	@Test
	public void testHashCodeEqual() {
		message1.setId(UUIDs.timeBased());
		message2.setId(message1.getId());
		assertEquals(message1.hashCode(), message2.hashCode());
	}
	
	@Test
	public void testHashCodeNullEqual() {
		message1.setId(null);
		message2.setId(null);
		assertEquals(message1.hashCode(), message2.hashCode());
	}
	
	@Test
	public void testEqualsSameOk() {
		message1.setId(UUIDs.timeBased());
		assertTrue(message1.equals(message1));
	}
	
	@Test
	public void testEqualsOk() {
		message1.setId(UUIDs.timeBased());
		message2.setId(message1.getId());
		assertTrue(message1.equals(message2));
	}
	
	@Test
	public void testEqualsNull() {
		assertFalse(message1.equals(null));
	}
	
	@Test
	public void testEqualsDiffClass() {
		assertFalse(message1.equals(new Object()));
	}
	
	@Test
	public void testEqualsFalseIdNull() {
		message1.setId(null);
		message2.setId(UUIDs.timeBased());
		assertFalse(message1.equals(message2));
	}
	
	@Test
	public void testEqualsFalseIdBothNull() {
		message1.setId(null);
		message2.setId(null);
		assertTrue(message1.equals(message2));
	}
	
	@Test
	public void testEqualsFalseIdDiff() {
		message1.setId(UUIDs.timeBased());
		message2.setId(UUIDs.timeBased());
		assertFalse(message1.equals(message2));
	}
	
	@Test
	public void testSets() {
		message1.setChatID(this.chatId);
		message1.setRemoved(true);
		assertEquals(this.chatId, message1.getChatID());
		assertEquals(true, message1.getRemoved());
	}
	
}
