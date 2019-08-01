package com.huonix.simplechat.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.huonix.simplechat.exceptions.ChatCreationErrorException;
import com.huonix.simplechat.exceptions.ChatLeaveException;
import com.huonix.simplechat.models.Chat;
import com.huonix.simplechat.models.Message;
import com.huonix.simplechat.services.ChatService;

/**
 * Chat rest controller
 * 
 * @author Kepler Vital
 *
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	private ChatService chatService;
	
	/**
	 * Retrieves the chat bar
	 * 
	 * @return the chat bar with users and groups
	 */
	@RequestMapping(value = "/bar", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> bar() {
		Map<String, Object> response = new HashMap<>();
		response.put("directs", chatService.directChats());
		response.put("groups", chatService.groupChats());
		response.put("users", chatService.usersChatInfo());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
	/**
	 * Retrieves the chat messages
	 * 
	 * @return the chat messages
	 */
	@RequestMapping(value = "/{id}/messages", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Message>> messages(@PathVariable("id") UUID chatId) {
        return new ResponseEntity<List<Message>>(chatService.getMessages(chatId), HttpStatus.OK);
    }
	
	/**
	 * Creates a chat with a user
	 * 
	 * @return the chat messages
	 */
	@RequestMapping(value = "/with/{userId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createDirect(@PathVariable("userId") UUID userId) {
		Map<String, Object> response = new HashMap<>();
		try {
			Chat chat = chatService.addSimple(userId);
			if(!chatService.getErrors().isEmpty()) {
				throw new ChatCreationErrorException("Error creating user chat.");
			}
	        response.put("chat", chat);
	    } catch(Exception e) {
	    	response.put("message", e.getMessage());
	    	response.put("errors", chatService.getErrors());
	    }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
	/**
	 * Creates a new group chat
	 * 
	 * @return the chat messages
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> create(@RequestBody Chat chat) {
		Map<String, Object> response = new HashMap<>();
		try {
			chat.setIsGroup(true);
			chat = chatService.add(chat);
			if(!chatService.getErrors().isEmpty()) {
				throw new ChatCreationErrorException("Error creating group chat.");
			}
			response.put("chat", chat);
		} catch(Exception e) {
			response.put("message", e.getMessage());
	    	response.put("errors", chatService.getErrors());
	    }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
	/**
	 * Updates a group chat
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> update(
    		@PathVariable("id") UUID chatId, 
    		@RequestBody Chat chat
    ) {
		Map<String, Object> response = new HashMap<>();
		try {
			chat = chatService.update(chatId, chat);
			if(!chatService.getErrors().isEmpty()) {
				throw new ChatCreationErrorException("Error updating group chat.");
			}
			response.put("chat", chat);
		} catch(Exception e) {
			response.put("message", e.getMessage());
	    	response.put("errors", chatService.getErrors());
	    }
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
	/**
	 * Removes chat users
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{id}/leave", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeParticipant(@PathVariable("id") UUID chatId) {
		Map<String, Object> response = new HashMap<>();
		try {
			if(!chatService.leaveChat(chatId)) {
				throw new ChatLeaveException("Can't leave chat.");
			}
			response.put("success", true);
			response.put("message", "Exited the chat successfully.");
		} catch(Exception e) {
			response.put("message", e.getMessage());
	    	response.put("errors", chatService.getErrors());
	    }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
}
