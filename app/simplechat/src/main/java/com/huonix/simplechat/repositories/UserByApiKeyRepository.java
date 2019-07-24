package com.huonix.simplechat.repositories;

import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.huonix.simplechat.models.UserByApiKey;

/**
 * UserByApiKeyRepository repository
 * 
 * @author Kepler Vital
 *
 */
@Repository
public interface UserByApiKeyRepository extends CassandraRepository<UserByApiKey, UserByApiKey> {
	
	/**
	 * Find's a user by the api key
	 * 
	 * @param id the api key
	 * @return Optional<UserByApiKey> with the UserByApiKey
	 */
	Optional<UserByApiKey> findByKey(String id);
	
}
