package com.huonix.simplechat.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.junit.Before;
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
public class ChatParticipantTest {

	private UUID chatId;
	private UUID userId;
	private ChatParticipant chat1;
	private ChatParticipant chat2;
	
	@Before
	public void setupData() {
		this.chatId = UUIDs.timeBased();
		this.userId = UUIDs.timeBased();
		this.chat1 = new ChatParticipant(this.userId, this.chatId);
		this.chat2 = new ChatParticipant(this.userId, this.chatId);
	}
	
	@Test
	public void testHashCodeEqual() {
		chat1.setChatID(UUIDs.timeBased());
		chat2.setChatID(chat1.getChatID());
		assertEquals(chat1.hashCode(), chat2.hashCode());
	}
	
	@Test
	public void testHashCodeNullEqual() {
		chat1.setChatID(null);
		chat2.setChatID(null);
		assertEquals(chat1.hashCode(), chat2.hashCode());
	}
	
	@Test
	public void testHashCodeUserNullEqual() {
		chat1.setUserID(null);
		chat2.setUserID(null);
		assertEquals(chat1.hashCode(), chat2.hashCode());
	}
	
	@Test
	public void testEqualsSameOk() {
		chat1.setChatID(UUIDs.timeBased());
		assertTrue(chat1.equals(chat1));
	}
	
	@Test
	public void testEqualsOk() {
		chat1.setChatID(UUIDs.timeBased());
		chat2.setChatID(chat1.getChatID());
		assertTrue(chat1.equals(chat2));
	}
	
	@Test
	public void testEqualsNull() {
		assertFalse(chat1.equals(null));
	}
	
	@Test
	public void testEqualsDiffClass() {
		assertFalse(chat2.equals(new Object()));
	}
	
	@Test
	public void testEqualsFalseIdNull() {
		chat1.setChatID(null);
		chat2.setChatID(UUIDs.timeBased());
		assertFalse(chat1.equals(chat2));
	}
	
	@Test
	public void testEqualsFalseIdBothNull() {
		chat1.setChatID(null);
		chat2.setChatID(null);
		assertTrue(chat1.equals(chat2));
	}
	
	@Test
	public void testEqualsFalseIdDiff() {
		chat1.setChatID(UUIDs.timeBased());
		chat2.setChatID(UUIDs.timeBased());
		assertFalse(chat1.equals(chat2));
	}
	
	@Test
	public void testEqualsFalseUserIdNull() {
		chat1.setUserID(null);
		chat2.setUserID(UUIDs.timeBased());
		assertFalse(chat1.equals(chat2));
	}
	
	@Test
	public void testEqualsFalseIdDiffUser() {
		chat1.setUserID(UUIDs.timeBased());
		chat2.setUserID(UUIDs.timeBased());
		assertFalse(chat1.equals(chat2));
	}
	
	@Test
	public void testEqualsFalseIdBothUserNull() {
		chat1.setUserID(null);
		chat2.setUserID(null);
		assertTrue(chat1.equals(chat2));
	}
	
	@Test
	public void testSets() {
		Date now = new Date();
		chat1.setChatID(this.chatId);
		chat1.setUserID(this.userId);
		chat1.setDateAdded(now);
		assertEquals(this.chatId, chat1.getChatID());
		assertEquals(this.userId, chat1.getUserID());
		assertEquals(now, chat1.getDateAdded());
	}
	
}
