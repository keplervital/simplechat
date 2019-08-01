package com.huonix.simplechat.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.huonix.simplechat.models.Message;

/**
 * Message persistency repository
 * 
 * @author Kepler Vital
 *
 */
@Repository
public interface MessageRepository extends CassandraRepository<Message, Message> {

	/**
	 * Find the message by the id
	 * 
	 * @param messageId must not be null {@literal null}
	 * @return Optional<Message> the chat
	 */
	Optional<Message> findById(UUID messageId);
	
	/**
	 * Delete the message by the id
	 * 
	 * @param messageId must not be null {@literal null}
	 * @return void
	 */
	void deleteById(UUID messageId);
	
}
