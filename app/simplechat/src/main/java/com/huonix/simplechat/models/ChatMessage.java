package com.huonix.simplechat.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * Chat messages entity
 * 
 * @author Kepler Vital
 *
 */
@Table("chat_messages")
public class ChatMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PrimaryKeyColumn(name = "chat_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID chatID;
	
	@PrimaryKeyColumn(name = "date_added", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private Date dateAdded;
	
	@PrimaryKeyColumn(name = "user_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
	private UUID userID;
	
	@PrimaryKeyColumn(name = "message_id", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
	private UUID messageID;

	/**
	 * Chat message constructor
	 * 
	 * @param chatID the chat id
	 * @param dateAdded the date it was sent
	 * @param userID the user id
	 * @param messageID the message id
	 */
	public ChatMessage(UUID chatID, Date dateAdded, UUID userID, UUID messageID) {
		super();
		this.chatID = chatID;
		this.dateAdded = dateAdded;
		this.userID = userID;
		this.messageID = messageID;
	}

	/**
	 * Get the chat id
	 * 
	 * @return the chatID
	 */
	public UUID getChatID() {
		return chatID;
	}

	/**
	 * Get the date added
	 * 
	 * @return the dateAdded
	 */
	public Date getDateAdded() {
		return dateAdded;
	}

	/**
	 * Get the user id
	 * 
	 * @return the userID
	 */
	public UUID getUserID() {
		return userID;
	}

	/**
	 * Get the message id
	 * 
	 * @return the messageID
	 */
	public UUID getMessageID() {
		return messageID;
	}

	/**
	 * Set the chat id
	 * 
	 * @param chatID the chatID to set
	 */
	public void setChatID(UUID chatID) {
		this.chatID = chatID;
	}

	/**
	 * Set the date added
	 * 
	 * @param dateAdded the dateAdded to set
	 */
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	/**
	 * Set the user id
	 * 
	 * @param userID the userID to set
	 */
	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	/**
	 * Set the message id
	 * 
	 * @param messageID the messageID to set
	 */
	public void setMessageID(UUID messageID) {
		this.messageID = messageID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chatID == null) ? 0 : chatID.hashCode());
		result = prime * result + ((messageID == null) ? 0 : messageID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatMessage other = (ChatMessage) obj;
		if (chatID == null) {
			if (other.chatID != null)
				return false;
		} else if (!chatID.equals(other.chatID))
			return false;
		if (messageID == null) {
			if (other.messageID != null)
				return false;
		} else if (!messageID.equals(other.messageID))
			return false;
		return true;
	}

}
