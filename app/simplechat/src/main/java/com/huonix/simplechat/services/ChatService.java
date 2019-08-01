package com.huonix.simplechat.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.utils.UUIDs;
import com.google.common.base.Strings;
import com.huonix.simplechat.exceptions.ChatCreationErrorException;
import com.huonix.simplechat.exceptions.ChatLeaveException;
import com.huonix.simplechat.exceptions.ChatUpdateException;
import com.huonix.simplechat.helpers.AuthHelper;
import com.huonix.simplechat.helpers.ErrorHandler;
import com.huonix.simplechat.models.Chat;
import com.huonix.simplechat.models.ChatMessage;
import com.huonix.simplechat.models.ChatParticipant;
import com.huonix.simplechat.models.Message;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.repositories.ChatMessageRepository;
import com.huonix.simplechat.repositories.ChatParticipantRepository;
import com.huonix.simplechat.repositories.ChatRepository;
import com.huonix.simplechat.repositories.MessageRepository;
import com.huonix.simplechat.repositories.UserRepository;

/**
 * Service handler for all chat business logic
 * 
 * @author Kepler Vital
 *
 */
@Service
public class ChatService extends ErrorHandler implements IChatService {

	@Autowired
	private ChatRepository chatRepository;
	
	@Autowired
	private ChatParticipantRepository chatParticipantRepository;
	
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Finds all of the user chats
	 */
	@Override
	public List<Chat> findAll() {
		User user = AuthHelper.user();
		List<Chat> list = new ArrayList<>();
		List<ChatParticipant> chats = chatParticipantRepository.findAllByUserID(user.getId());
		chats.forEach(e->{
			Optional<Chat> chat = chatRepository.findById(e.getChatID());
			if(chat.isPresent()) {
				list.add(chat.get());
			}
		});
		return list;
	}

	@Override
	public Optional<Chat> getById(UUID id) {
		if(isMyChat(id)) {
			return chatRepository.findById(id);
		}
		return Optional.empty();
	}
	
	@Override
	public Chat addSimple(UUID userId) {
		Set<UUID> participants = new HashSet<>();
		participants.add(AuthHelper.user().getId());
		participants.add(userId);
		return add(new Chat(Chat.DIRECT_CHAT, participants, false));
	}

	@Override
	public Chat add(Chat model) {
		try {
			this.clearErrors();
			User user = AuthHelper.user();
			if(model.getParticipants() == null) {
				Set<UUID> participants = new HashSet<>();
				model.setParticipants(participants);
			}
			if(!model.getParticipants().contains(user.getId())) {
				Set<UUID> participants = model.getParticipants();
				participants.add(user.getId());
				model.setParticipants(participants);				
			}
			if(!model.getIsGroup() && model.getParticipants().size() != 2) {
				throw new ChatCreationErrorException("A direct chat must contain two users.");
			} else if(!model.getIsGroup()) {
				Iterator<UUID> ite = model.getParticipants().iterator();
				if(!isUniqueDirectChat(ite.next(), ite.next())) {
					throw new ChatCreationErrorException("Chat already created between this two users.");
				}
			}
			model.setId(UUIDs.timeBased());
			model.setDateAdded(new Date());
			model = chatRepository.save(model);
			for(UUID userId : model.getParticipants()) {
				ChatParticipant participant = new ChatParticipant(userId, model.getId());
				participant.setDateAdded(new Date());
				chatParticipantRepository.save(participant);
			}
		} catch(Exception e) {
			this.addError(e.getMessage());
	    	return null;
		}
		return model;
	}

	@Override
	public Chat update(UUID id, Chat model) {
		try {
			this.clearErrors();
			if(!isMyChat(id)) {
				throw new ChatUpdateException("Can't update chat that you are not present.");
			}
			Optional<Chat> opChat = chatRepository.findById(id);
			if(opChat.isPresent() && !opChat.get().getIsGroup()) {
				throw new ChatUpdateException("Can't update a direct chat between two users.");
			}
			Chat chat = opChat.get();
			chat.setDateModified(new Date());
			if(!Strings.isNullOrEmpty(model.getName())) {
				chat.setName(model.getName());
			}
			Set<UUID> addParticipants = model.getParticipants();
			if(addParticipants != null) {
				Set<UUID> participants = chat.getParticipants();
				addParticipants.forEach(userId->{
					participants.add(userId);
					Optional<ChatParticipant> opChatParticipant = 
							chatParticipantRepository.findByUserIDAndChatID(userId, chat.getId());
					if(!opChatParticipant.isPresent()) {
						ChatParticipant chatParticipant = new ChatParticipant(userId, chat.getId());
						chatParticipant.setDateAdded(new Date());
						chatParticipantRepository.save(chatParticipant);
					}
				});
				chat.setParticipants(participants);
			}
			model = chatRepository.save(chat);
		} catch(Exception e) {
			this.addError(e.getMessage());
	    	return null;
		}
		return model;
	}

	@Override
	public boolean delete(Chat model) {
		try {
			this.clearErrors();
			if(!isMyChat(model.getId())) {
				throw new ChatUpdateException("Can't delete chat that you are not present.");
			}
			if(!model.getIsGroup()) {
				throw new ChatUpdateException("Can't delete a direct chat between two users.");
			}
			Set<UUID> participants = model.getParticipants();
			if(participants != null) {
				participants.forEach(userId->{
					Optional<ChatParticipant> chatparticipant = 
							chatParticipantRepository.findByUserIDAndChatID(userId, model.getId());
					if(chatparticipant.isPresent())
						chatParticipantRepository.delete(chatparticipant.get());
				});
			}
			List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatID(model.getId());
			chatMessages.forEach(e->{
				messageRepository.deleteById(e.getMessageID());
			});
			chatMessageRepository.deleteByChatID(model.getId());
			chatRepository.delete(model);
		} catch(Exception e) {
			this.addError(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean existsById(UUID id) {
		if(isMyChat(id)) {
			return chatRepository.findById(id).isPresent();
		}
		return false;
	}

	@Override
	public long count() {
		return chatParticipantRepository.findAllByUserID(AuthHelper.user().getId()).size();
	}

	@Override
	public boolean deleteById(UUID id) {
		try {
			Optional<Chat> chat = chatRepository.findById(id);
			if(!chat.isPresent()) {
				throw new ChatUpdateException("Chat not found.");
			}
			if(!delete(chat.get())) {
				throw new ChatUpdateException("Chat delete error.");
			}
		} catch(Exception e) {
			this.addError(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Check if the chat id is mine
	 * 
	 * @param chatId the UUID
	 * @return if the chat is mine
	 */
	private boolean isMyChat(UUID chatId) {
		User user = AuthHelper.user();
		Optional<ChatParticipant> chatParticipant = chatParticipantRepository.findByUserIDAndChatID(user.getId(), chatId);
		return chatParticipant.isPresent();
	}
	
	/**
	 * Check if a direct chat between two users already exists
	 * 
	 * @param userId first user
	 * @param toId second user
	 * @return
	 */
	private boolean isUniqueDirectChat(UUID userId, UUID toId) {
		List<ChatParticipant>  chats = chatParticipantRepository.findAllByUserID(userId);
		for(ChatParticipant chatParticipant : chats) {
			Optional<Chat> chat = chatRepository.findById(chatParticipant.getChatID());
			if(chat.isPresent() && !chat.get().getIsGroup()) {
				Set<UUID> participants = chat.get().getParticipants();
				if(
					participants.contains(userId) &&
					participants.contains(toId)	
				) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public List<Message> getMessages(UUID chatId) {
		if(!isMyChat(chatId))
			return Collections.emptyList();
		List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatID(chatId);
		List<Message> messages = new ArrayList<>();
		chatMessages.forEach(m->{
			Optional<Message> message = messageRepository.findById(m.getMessageID());
			if(message.isPresent()) {
				messages.add(message.get());
			}
		});
		return messages;
	}
	
	@Override
	public boolean leaveChat(UUID chatId) throws ChatLeaveException {
		this.clearErrors();
		if(!isMyChat(chatId)) {
			this.addError("Can't leave a chat that you are not present.");
			throw new ChatLeaveException("Chat leave error.");
		}
		User user = AuthHelper.user();
		Optional<ChatParticipant> participant = chatParticipantRepository.findByUserIDAndChatID(user.getId(), chatId);
		if(participant.isPresent()) {
			Optional<Chat> opChat = chatRepository.findById(chatId);
			if(opChat.isPresent() && !opChat.get().getIsGroup()) {
				this.addError("Can't leave a direct chat between two users.");
				throw new ChatLeaveException("Chat leave error.");
			}
			Chat chat = opChat.get();
			Set<UUID> participants = chat.getParticipants();
			if(participants != null && participants.contains(user.getId())) {
				if(participants.size() == 1) {
					if(!delete(chat)) {
						this.addError("Error exiting chat.");
						throw new ChatLeaveException("Chat leave error.");
					}
				} else {
					participants.remove(user.getId());
					chat.setParticipants(participants);
					chat.setDateModified(new Date());
					chatRepository.save(chat);
				}
			}
			chatParticipantRepository.delete(participant.get());
			return true;
		}
		return false;
	}

	@Override
	public List<Chat> directChats() {
		User me = AuthHelper.user();
		List<ChatParticipant> allMyChats = chatParticipantRepository.findAllByUserID(me.getId());
		List<Chat> myDirectChats = new ArrayList<>();
		allMyChats.forEach(chat->{
			Optional<Chat> opChat = chatRepository.findById(chat.getChatID());
			if(opChat.isPresent() && !opChat.get().getIsGroup()) {
				myDirectChats.add(opChat.get());
			}
		});
		List<User> users = userRepository.findAll();
		users.forEach(user->{
			if(!user.equals(me)) {
				boolean existChat = false;
				for(Chat chat : myDirectChats) {
					if(chat.getParticipants().contains(user.getId())) {
						existChat = true;
						break;
					}
				}
				if(!existChat && !user.getBlocked()) {
					myDirectChats.add(addSimple(user.getId()));
				}
			}
		});
		return myDirectChats;
	}

	@Override
	public List<Chat> groupChats() {
		User me = AuthHelper.user();
		List<ChatParticipant> allMyChats = chatParticipantRepository.findAllByUserID(me.getId());
		List<Chat> myGroupChats = new ArrayList<>();
		allMyChats.forEach(chat->{
			Optional<Chat> opChat = chatRepository.findById(chat.getChatID());
			if(opChat.isPresent() && opChat.get().getIsGroup()) {
				myGroupChats.add(opChat.get());
			}
		});
		return myGroupChats;
	}
	
	public Map<UUID, Map<String, Object>> usersChatInfo() {
		User me = AuthHelper.user();
		Map<UUID, Map<String, Object>> people = new HashMap<>();
		List<ChatParticipant> allMyChats = chatParticipantRepository.findAllByUserID(me.getId());
		List<Chat> chats = new ArrayList<>();
		allMyChats.forEach(chat->{
			Optional<Chat> opChat = chatRepository.findById(chat.getChatID());
			if(opChat.isPresent()) {
				chats.add(opChat.get());
			}
		});
		chats.forEach(chat->{
			Set<UUID> participants = chat.getParticipants();
			for(UUID userId : participants) {
				if(people.containsKey(userId))
					continue;
				Optional<User> opUser = userRepository.findById(userId);
				if(opUser.isPresent()) {
					User user = opUser.get();
					Map<String, Object> info = new HashMap<>();
					info.put("id", user.getId());
					info.put("name", user.getName());
					info.put("mood", user.getMood());
					info.put("avatar", user.getAvatar());
					people.put(user.getId(), info);
				}
			}
		});
		return people;
	}

}
