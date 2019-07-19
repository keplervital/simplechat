package com.huonix.simplechat.controllers;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.huonix.simplechat.models.User;
import com.huonix.simplechat.repositories.UserRepository;

@RestController
public class UserController {
	
	@Autowired
    private UserRepository userRepository;
	
	@RequestMapping(value = "/user",method = RequestMethod.GET)
    @ResponseBody
    public List<User> greeting() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(e->users.add(e));
        return users;
    }
	
	@RequestMapping(value = "/user",method = RequestMethod.POST)
    @ResponseBody
    public String createUser(@RequestBody User user) {
        user.setDateAdded(new Date());
        userRepository.save(user);
        return "OK";
    }

}
