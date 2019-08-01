package com.huonix.simplechat.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

import com.huonix.simplechat.exceptions.MessageException;
import com.huonix.simplechat.models.Message;
import com.huonix.simplechat.services.MessageService;

/**
 * <h1>Message rest controller</h1>
 * 
 * @author Kepler Vital
 *
 */
@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
	MessageService messageService;
	
	/**
	 * Retrieve the message
	 * 
	 * @param UUID id of the message
	 * @return the chat message
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Message> get(@PathVariable("id") UUID id) {
		Optional<Message> optional = messageService.getById(id);
		if(!optional.isPresent()) {
			return new ResponseEntity<Message>(HttpStatus.NOT_FOUND);
		}
        return new ResponseEntity<Message>(optional.get(), HttpStatus.OK);
    }
	
	/**
	 * Creates a new message
	 * 
	 * @return the chat message
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> create(@RequestBody Message message) {
		Map<String, Object> response = new HashMap<>();
		try {
			message = messageService.add(message);
			if(!messageService.getErrors().isEmpty()) {
				throw new MessageException("Error creating message.");
			}
	        response.put("message", message);
	    } catch(Exception e) {
	    	response.put("message", e.getMessage());
	    	response.put("errors", messageService.getErrors());
	    }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
	/**
	 * Update the message
	 * 
	 * @param UUID id of the message
	 * @return the chat message
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> update(
    		@PathVariable("id") UUID id, 
    		@RequestBody Message message
    ) {
		Map<String, Object> response = new HashMap<>();
		try {
			message = messageService.update(id, message);
			if(!messageService.getErrors().isEmpty()) {
				throw new MessageException("Error updating message.");
			}
	        response.put("message", message);
	    } catch(Exception e) {
	    	response.put("message", e.getMessage());
	    	response.put("errors", messageService.getErrors());
	    }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
	/**
	 * Remove the message
	 * 
	 * @param UUID id of the message
	 * @return the chat message
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") UUID id) {
		Map<String, Object> response = new HashMap<>();
		try {
			if(!messageService.deleteById(id)) {
				throw new MessageException("Error deleting message.");
			}
			response.put("success", true);
			response.put("message", "Message deleted successfully.");
		} catch(Exception e) {
			response.put("message", e.getMessage());
	    	response.put("errors", messageService.getErrors());
	    }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
}
