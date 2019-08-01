package com.huonix.simplechat.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.huonix.simplechat.exceptions.ChatLeaveException;
import com.huonix.simplechat.models.Chat;
import com.huonix.simplechat.models.Message;

/**
 * Interface for generic operations on the Chat repository and associations.
 * 
 * @author Kepler Vital
 *
 */
@Service
public interface IChatService extends IService<Chat, UUID> {
	
	/**
	 * Creates a simple chat between two users
	 * 
	 * @param userId the user id to start the chat
	 * @return the id
	 */
	Chat addSimple(UUID userId);
	
	/**
	 * Retrieves all the messages by the chat id
	 * 
	 * @param chatId the chat id
	 * @return the list of messages
	 */
	List<Message> getMessages(UUID chatId);
	
	/**
	 * Lets the user leave a chat
	 * 
	 * @param chatId the chat id
	 * @return if the user leaved
	 */
	boolean leaveChat(UUID chatId) throws ChatLeaveException;
	
}
