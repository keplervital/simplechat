package com.huonix.simplechat.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.huonix.simplechat.models.User;

/**
 * User persistency repository
 * 
 * @author Kepler Vital
 *
 */
@Repository
public interface UserRepository extends CassandraRepository<User, User> {
	
	/**
	 * Find the user by the id
	 * 
	 * @param id must not be {@literal null}
	 * @return Optional<User>
	 */
	Optional<User> findById(UUID id);
	
}
