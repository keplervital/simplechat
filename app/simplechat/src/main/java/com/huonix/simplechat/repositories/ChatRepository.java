package com.huonix.simplechat.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.huonix.simplechat.models.Chat;

/**
 * Chat persistency repository
 * 
 * @author Kepler Vital
 *
 */
@Repository
public interface ChatRepository extends CassandraRepository<Chat, Chat> {
	
	/**
	 * Find the chat by the id
	 * 
	 * @param chatId must not be null {@literal null}
	 * @return Optional<Chat> the chat
	 */
	Optional<Chat> findById(UUID chatId);
	
}
