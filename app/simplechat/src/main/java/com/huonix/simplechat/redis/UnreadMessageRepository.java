package com.huonix.simplechat.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.huonix.simplechat.models.UnreadMessage;

@Repository
public interface UnreadMessageRepository extends CrudRepository<UnreadMessage, String> {}