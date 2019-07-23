package com.huonix.simplechat.repositories;

import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.huonix.simplechat.models.UserByApiKey;

@Repository
public interface UserByApiKeyRepository extends CassandraRepository<UserByApiKey, UserByApiKey> {
	
	Optional<UserByApiKey> findByKey(String id);
	
}
