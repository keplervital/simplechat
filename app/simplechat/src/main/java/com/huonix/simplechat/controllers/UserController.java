package com.huonix.simplechat.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.utils.UUIDs;
import com.huonix.simplechat.models.UserByApiKey;
import com.huonix.simplechat.models.UserByName;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.repositories.UserByApiKeyRepository;
import com.huonix.simplechat.repositories.UserByNameRepository;
import com.huonix.simplechat.repositories.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private UserByApiKeyRepository userByApiKeyRepository;
	
	@Autowired
	private UserByNameRepository userByNameRepository;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<User> list() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(e->users.add(e));
        return users;
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> get(@PathVariable("id") UUID id) {
		Optional<User> optional = userRepository.findById(id);
		if(!optional.isPresent()) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		} 
        return new ResponseEntity<User>(optional.get(), HttpStatus.OK);
    }
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> create(@Valid @RequestBody User user) {
		HashMap<String, String> response = new HashMap<>();
		try {
			Optional<UserByName> chatUser = userByNameRepository.findByName(user.getName());
			if(chatUser.isPresent()) {
				throw new Exception("The name especified already exists.");
			}
			user.setId(UUIDs.timeBased());
			user.setDateAdded(new Date());
	        user = userRepository.save(user);
	        
	        userByApiKeyRepository.save(new UserByApiKey(user.getAccessKey(), user.getId()));
	        userByNameRepository.save(new UserByName(user.getName(), user.getId()));
	        
	        response.put("id", user.getId().toString());
	        response.put("accessKey", user.getAccessKey());
	    } catch(Exception e) {
	    	response.put("error", e.getMessage());
	    }
        return response;
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, String> update(@PathVariable("id") User user) {
		HashMap<String, String> response = new HashMap<>();
		try {
			// TODO
	    } catch(Exception e) {
	    	response.put("error", e.getMessage());
	    }
        return response;
    }
	
	@RequestMapping(value = "/delete/id/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, String> delete(@PathVariable("id") UUID id) {
		HashMap<String, String> response = new HashMap<>();
		Optional<User> optional = userRepository.findById(id);
		if(!optional.isPresent()) {
			response.put("error", "User not found.");
			return response;
		} 
		User user = optional.get();		
		Optional<UserByApiKey> userByApiKey = userByApiKeyRepository.findByKey(user.getAccessKey());
		Optional<UserByName> userByName = userByNameRepository.findByName(user.getName());
		if(userByApiKey.isPresent()) {
			userByApiKeyRepository.delete(userByApiKey.get());
		}
		if(userByName.isPresent()) {
			userByNameRepository.delete(userByName.get());
		}
		userRepository.delete(user);
		response.put("message", "User successfully removed.");
        return response;
    }

}
