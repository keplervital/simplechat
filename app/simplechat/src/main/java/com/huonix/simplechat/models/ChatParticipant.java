package com.huonix.simplechat.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;


/**
 * Chat participants entity
 * 
 * @author Kepler Vital
 *
 */
@Table("chat_participants")
public class ChatParticipant implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID userID;
	
	@PrimaryKeyColumn(name = "chat_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private UUID chatID;
	
	@PrimaryKeyColumn(name = "date_added", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
	private Date dateAdded;

	/**
	 * Chat participant constructor
	 * 
	 * @param userID the user id
	 * @param chatID the chat id
	 * @param removed if the user is out of the chat
	 */
	public ChatParticipant(UUID userID, UUID chatID) {
		super();
		this.userID = userID;
		this.chatID = chatID;
	}

	/**
	 * Gets the user id
	 * 
	 * @return the userID
	 */
	public UUID getUserID() {
		return userID;
	}

	/**
	 * Gets the chat id
	 * 
	 * @return the chatID
	 */
	public UUID getChatID() {
		return chatID;
	}

	/**
	 * Gets when the user was added to the chat
	 * 
	 * @return the dateAdded
	 */
	public Date getDateAdded() {
		return dateAdded;
	}

	/**
	 * Sets the user id
	 * 
	 * @param userID the userID to set
	 */
	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	/**
	 * Sets the chat id
	 * 
	 * @param chatID the chatID to set
	 */
	public void setChatID(UUID chatID) {
		this.chatID = chatID;
	}

	/**
	 * Sets the date added
	 * 
	 * @param dateAdded the dateAdded to set
	 */
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chatID == null) ? 0 : chatID.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
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
		ChatParticipant other = (ChatParticipant) obj;
		if (chatID == null) {
			if (other.chatID != null)
				return false;
		} else if (!chatID.equals(other.chatID))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}

}
