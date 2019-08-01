package com.huonix.simplechat.controllers;

import java.util.HashMap;
import java.util.List;
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

import com.huonix.simplechat.annotations.AllowAccess;
import com.huonix.simplechat.enums.ERole;
import com.huonix.simplechat.helpers.AuthHelper;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.services.UserService;

/**
 * User rest controller
 * 
 * @author Kepler Vital
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * Retrieves the authenticated user
	 * 
	 * @return the user data
	 */
	@RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> me() {
        return new ResponseEntity<User>(AuthHelper.user(), HttpStatus.OK);
    }
	
	/**
	 * Retrieves all users
	 * 
	 * @return a list of all users
	 */
	@AllowAccess(roles = {ERole.ADMIN})
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<User>> list() {
		return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
    }
	
	/**
	 * Retrieves a user
	 * 
	 * @param id the user id
	 * @return the user data
	 */
	@AllowAccess(roles = {ERole.ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> get(@PathVariable("id") UUID id) {
		Optional<User> optional = userService.getById(id);
		if(!optional.isPresent()) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
        return new ResponseEntity<User>(optional.get(), HttpStatus.OK);
    }
	
	/**
	 * Adds a new user
	 * 
	 * @param user the user data
	 * @return the new generated user id and api key to access the system
	 */
	@AllowAccess(roles = {ERole.ADMIN})
	@RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> create(@RequestBody User user) {
		HashMap<String, Object> response = new HashMap<>();
		try {
			user = userService.add(user);
	        response.put("id", user.getId().toString());
	        response.put("accessKey", user.getAccessKey());
	    } catch(Exception e) {
	    	response.put("errors", userService.getErrors());
	    }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
	/**
	 * Updates an existing user
	 * 
	 * @param id the user id
	 * @param user the user data
	 * @return the user id and api key to access the system
	 */
	@AllowAccess(roles = {ERole.ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> update(@PathVariable("id") UUID id, @RequestBody User user) {
		Map<String, Object> response = new HashMap<>();
		try {
			user = userService.update(id, user);
	        response.put("id", user.getId().toString());
	        response.put("accessKey", user.getAccessKey());
	    } catch(Exception e) {
	    	response.put("errors", userService.getErrors());
	    }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
	
	/**
	 * Deletes a system user
	 * 
	 * @param id the user id
	 * @return message if the user was successfully deleted
	 */
	@AllowAccess(roles = {ERole.ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") UUID id) {
		HashMap<String, Object> response = new HashMap<>();
		if(userService.deleteById(id)) {
			response.put("message", "User successfully removed.");
		} else {
			response.put("errors", userService.getErrors());
		}
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
