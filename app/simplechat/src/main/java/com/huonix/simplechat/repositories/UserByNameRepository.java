package com.huonix.simplechat.repositories;

import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.huonix.simplechat.models.UserByName;

public interface UserByNameRepository extends CassandraRepository<UserByName, UserByName> {

	Optional<UserByName> findByName(String name);
	
}
