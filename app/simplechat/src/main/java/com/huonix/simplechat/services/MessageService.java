package com.huonix.simplechat.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.utils.UUIDs;
import com.google.common.base.Strings;
import com.huonix.simplechat.exceptions.ChatUpdateException;
import com.huonix.simplechat.exceptions.MessageException;
import com.huonix.simplechat.helpers.AuthHelper;
import com.huonix.simplechat.helpers.ErrorHandler;
import com.huonix.simplechat.models.ChatMessage;
import com.huonix.simplechat.models.Message;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.repositories.ChatMessageRepository;
import com.huonix.simplechat.repositories.MessageRepository;

/**
 * Service handler for all message business logic
 * 
 * @author Kepler Vital
 *
 */
@Service
public class MessageService extends ErrorHandler implements IMessageService {

	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Override
	public List<Message> findAll() {
		return messageRepository.findAll();
	}

	@Override
	public Optional<Message> getById(UUID id) {
		if(isMine(id)) {
			return messageRepository.findById(id);
		}
		return Optional.empty();
	}

	@Override
	public Message add(Message message) {
		try {
			this.clearErrors();
			Date now = new Date();
			User user = AuthHelper.user();
			message.setId(UUIDs.timeBased());
			message.setDateAdded(now);
			message.setUserID(user.getId());
			ChatMessage chatMessage = new ChatMessage(
					message.getChatID(), 
					message.getDateAdded(), 
					message.getUserID(), 
					message.getId()
			);
			chatMessageRepository.save(chatMessage);
			message = messageRepository.save(message);
		} catch(Exception e) {
			this.addError(e.getMessage());
	    	return null;
		}
		return message;
	}

	@Override
	public Message update(UUID id, Message model) {
		try {
			this.clearErrors();
			if(!existsById(id)) {
				throw new MessageException("Can't update message that you are not the owner.");
			}
			Optional<Message> opMessage = getById(id);
			if(!opMessage.isPresent()) {
				throw new MessageException("Message not found.");
			}
			Message message = opMessage.get();
			if(!Strings.isNullOrEmpty(model.getBody())) {
				message.setBody(model.getBody());
			}
			model = messageRepository.save(message);
		} catch(Exception e) {
			this.addError(e.getMessage());
	    	return null;
		}
		return model;
	}

	@Override
	public boolean delete(Message message) {
		try {
			this.clearErrors();
			if(!existsById(message.getId())) {
				throw new MessageException("Can't delete message that you are not the owner.");
			}
			ChatMessage chatMessage = new ChatMessage(
					message.getChatID(), 
					message.getDateAdded(), 
					message.getUserID(), 
					message.getId()
			);
			chatMessageRepository.delete(chatMessage);
			messageRepository.delete(message);
		} catch(Exception e) {
			this.addError(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean existsById(UUID id) {
		return isMine(id);
	}

	@Override
	public long count() {
		return messageRepository.count();
	}

	@Override
	public boolean deleteById(UUID id) {
		try {
			if(!existsById(id)) {
				throw new MessageException("Message not found.");
			}
			if(!delete(messageRepository.findById(id).get())) {
				throw new ChatUpdateException("Message delete error.");
			}
		} catch(Exception e) {
			this.addError(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Check if the message id is mine
	 * 
	 * @param chatId the UUID
	 * @return if the chat is mine
	 */
	private boolean isMine(UUID messageId) {
		User user = AuthHelper.user();
		Optional<Message> opMessage = messageRepository.findById(messageId);
		if(opMessage.isPresent() && opMessage.get().getUserID().equals(user.getId())) {
			return true;
		}
		return false;
	}

}
