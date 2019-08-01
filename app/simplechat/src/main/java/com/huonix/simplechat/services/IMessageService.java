package com.huonix.simplechat.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.huonix.simplechat.models.Message;

/**
 * Interface for generic operations on the Message repository and associations.
 * 
 * @author Kepler Vital
 *
 */
@Service
public interface IMessageService extends IService<Message, UUID> {

}
