package com.huonix.simplechat.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.utils.UUIDs;

/**
 * Message entity
 * 
 * @author Kepler Vital
 *
 */
@Table("messages")
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID id = UUIDs.timeBased();
	
	@Column(value="chat_id")
	private UUID chatID;
	
	@Column(value="user_id")
	private UUID userID;

	@Column(value="body")
	private String body;
	
	@Column(value="date_added")
	private Date dateAdded;
	
	@Column(value="removed")
	private Boolean removed;

	/**
	 * Message constructor
	 * 
	 * @param chatID the chat id
	 * @param userID the user id
	 * @param body the message body
	 */
	public Message(UUID chatID, UUID userID, String body) {
		super();
		this.chatID = chatID;
		this.userID = userID;
		this.body = body;
	}

	/**
	 * Get the id
	 * 
	 * @return the id
	 */
	public UUID getId() {
		return id;
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
	 * Get the user id
	 * 
	 * @return the userID
	 */
	public UUID getUserID() {
		return userID;
	}

	/**
	 * Get the message body
	 * 
	 * @return the body
	 */
	public String getBody() {
		return body;
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
	 * Get if it was removed
	 * 
	 * @return the removed
	 */
	public Boolean getRemoved() {
		return removed;
	}

	/**
	 * Set the message id
	 * 
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
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
	 * Set the user id
	 * 
	 * @param userID the userID to set
	 */
	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	/**
	 * Set the body
	 * 
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
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
	 * Set the date removed
	 * 
	 * @param removed the removed to set
	 */
	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Message other = (Message) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
