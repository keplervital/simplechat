package com.huonix.simplechat.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.huonix.simplechat.models.User;

/**
 * Interface for generic operations on the User repository and associations.
 * 
 * @author Kepler Vital
 *
 */
@Service
public interface IUserService extends IService<User, UUID> {

	/**
	 * Finds the user by it's api key
	 * 
	 * @param apiKey the api key
	 * @return User
	 */
	User getByApiKey(String apiKey);
	
}
