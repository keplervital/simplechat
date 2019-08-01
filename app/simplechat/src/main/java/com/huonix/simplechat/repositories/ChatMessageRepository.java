package com.huonix.simplechat.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.huonix.simplechat.models.ChatMessage;

/**
 * ChatMessage persistency repository
 * 
 * @author Kepler Vital
 *
 */
@Repository
public interface ChatMessageRepository extends CassandraRepository<ChatMessage, ChatMessage> {

	/**
	 * Find all messages of chat
	 * 
	 * @param chatId must not be null {@literal null}
	 * @return List<ChatMessage> the chat
	 */
	List<ChatMessage> findAllByChatID(UUID chatId);
	
	/**
	 * Delete all messages of chat
	 * 
	 * @param chatId must not be null {@literal null}
	 * @return void
	 */
	void deleteByChatID(UUID chatId);
	
}
