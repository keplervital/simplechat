package com.huonix.simplechat.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.utils.UUIDs;
import com.huonix.simplechat.exceptions.UserNameNotUniqueException;
import com.huonix.simplechat.exceptions.UserNotFoundException;
import com.huonix.simplechat.helpers.ErrorHandler;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.models.UserByApiKey;
import com.huonix.simplechat.models.UserByName;
import com.huonix.simplechat.repositories.UserByApiKeyRepository;
import com.huonix.simplechat.repositories.UserByNameRepository;
import com.huonix.simplechat.repositories.UserRepository;

/**
 * Service handler for all user business logic
 * 
 * @author Kepler Vital
 *
 */
@Service
public class UserService extends ErrorHandler implements IUserService {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private UserByApiKeyRepository userByApiKeyRepository;
	
	@Autowired
	private UserByNameRepository userByNameRepository;
	
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	@Override
	public Optional<User> getById(UUID id) {
		return userRepository.findById(id);
	}
	
	/**
	 * Adds and user
	 *
	 * @param user must not be {@literal null}.
	 * @return {@literal User} with the entity, {@literal null} otherwise.
	 */
	@Override
	public User add(User user) {
		try {
			this.clearErrors();
			Optional<UserByName> chatUser = userByNameRepository.findByName(user.getName());
			if(chatUser.isPresent()) {
				throw new UserNameNotUniqueException("The name especified already exists.");
			}
			user.setId(UUIDs.timeBased());
			user.setDateAdded(new Date());
			user = userRepository.save(user);
	        
	        userByApiKeyRepository.save(new UserByApiKey(user.getAccessKey(), user.getId()));
	        userByNameRepository.save(new UserByName(user.getName(), user.getId()));
	    } catch(Exception e) {
	    	this.addError(e.getMessage());
	    	return null;
	    }
		return user;
	}

	/**
	 * Update a user by it's id and model
	 *
	 * @param id must not be {@literal null}.
	 * @param user must not be {@literal null}.
	 * @return {@literal User} with the entity, {@literal null} otherwise.
	 */
	@Override
	public User update(UUID id, User newData) {
		User user;
		try {
			this.clearErrors();
			Optional<User> opUser = userRepository.findById(id);
			if(!opUser.isPresent()) {
				throw new UserNotFoundException("User not found.");
			}
			user = opUser.get();
			Optional<UserByName> chatUser = userByNameRepository.findByName(newData.getName());
			if(chatUser.isPresent() && !chatUser.get().getUserId().equals(id)) {
				throw new UserNameNotUniqueException("The name especified already exists.");
			}			
			if(!newData.getName().isEmpty() && !user.getName().equals(newData.getName())) {
				userByNameRepository.deleteById(new UserByName(user.getName(), user.getId()));
				userByNameRepository.save(new UserByName(newData.getName(), user.getId()));
				userRepository.delete(user);
			}
			user.setName(newData.getName());
			user.setMood(newData.getMood());
			user.setAvatar(newData.getAvatar());
			user.setBlocked(newData.getBlocked());
			user.setAdmin(newData.getAdmin());
			user.setDateModified(new Date());
			user = userRepository.save(user);
	    } catch(Exception e) {
	    	this.addError(e.getMessage());
	    	return null;
	    }
		return user;
	}

	/**
	 * Deletes a user by it's model
	 *
	 * @param user must not be {@literal null}.
	 * @return {@literal true} if delete was successful, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	@Override
	public boolean delete(User user) {
		try {
			this.clearErrors();
			Optional<UserByApiKey> userByApiKey = userByApiKeyRepository.findByKey(user.getAccessKey());
			Optional<UserByName> userByName = userByNameRepository.findByName(user.getName());
			if(userByApiKey.isPresent()) {
				userByApiKeyRepository.delete(userByApiKey.get());
			}
			if(userByName.isPresent()) {
				userByNameRepository.delete(userByName.get());
			}
			userRepository.delete(user);
		} catch(Exception e) {
			this.addError(e.getMessage());
			return false;
		}
		return true;
	}

	
	@Override
	public boolean existsById(UUID id) {
		return userRepository.findById(id).isPresent();
	}

	@Override
	public long count() {
		return userRepository.count();
	}

	@Override
	public boolean deleteById(UUID id) {
		try {
			this.clearErrors();
			Optional<User> optional = userRepository.findById(id);
			if(!optional.isPresent()) {
				throw new UserNotFoundException("User not found.");
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
		} catch(Exception e) {
			this.addError(e.getMessage());
			return false;
		}
		return true;
	}

}
