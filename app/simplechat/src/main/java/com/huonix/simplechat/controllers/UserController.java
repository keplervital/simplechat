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
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.repositories.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private UserRepository userRepository;
	
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
        user.setId(UUIDs.timeBased());
		user.setDateAdded(new Date());
        user = userRepository.save(user);
        response.put("id", user.getId().toString());
        response.put("accessKey", user.getAccessKey());
        return response;
    }
	
	@RequestMapping(value = "/delete/id/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, String> delete(@PathVariable("id") UUID id) {
		HashMap<String, String> response = new HashMap<>();
		Optional<User> optional = userRepository.findById(id);
		if(!optional.isPresent()) {
			response.put("error", "user not found.");
			return response;
		} 
		userRepository.deleteById(optional.get());
		response.put("message", "user successfully removed.");
        return response;
    }

}
