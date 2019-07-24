package com.huonix.simplechat.repositories;

import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.huonix.simplechat.models.UserByName;

/**
 * UserByNameRepository repository
 * 
 * @author Kepler Vital
 *
 */
public interface UserByNameRepository extends CassandraRepository<UserByName, UserByName> {
	
	/**
	 * Find's a user by the name
	 * 
	 * @param name the user name
	 * @return Optional<UserByName> with the user
	 */
	Optional<UserByName> findByName(String name);
	
}
