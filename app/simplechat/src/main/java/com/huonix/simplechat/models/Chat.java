package com.huonix.simplechat.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.utils.UUIDs;

/**
 * Chat entity
 * 
 * @author Kepler Vital
 *
 */
@Table("chats")
public class Chat implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID id = UUIDs.timeBased();
	
	@PrimaryKeyColumn(name = "is_group", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private Boolean isGroup;
	
	@Column(value="name")
	private String name;
	
	@Column(value="participants")
	private Set<UUID> participants;
	
	@Column(value="date_added")
	private Date dateAdded;

	@Column(value="date_modified")
	private Date dateModified;

	/**
	 * Chat constructor 
	 * 
	 * @param name the chat name
	 * @param participants all the participants
	 */
	public Chat(String name, Set<UUID> participants, Boolean isGroup) {
		super();
		this.name = name;
		this.participants = participants;
		this.isGroup = isGroup;
		this.dateAdded = new Date();
	}

	/**
	 * Gets the id of the chat
	 * 
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}
	
	/**
	 * Gets if this chat is a group
	 * 
	 * @return the isGroup
	 */
	public Boolean getIsGroup() {
		return isGroup;
	}

	/**
	 * Gets the name of the chat
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the participants
	 * 
	 * @return the participants
	 */
	public Set<UUID> getParticipants() {
		return participants;
	}

	/**
	 * Gets the date added
	 * 
	 * @return the dateAdded
	 */
	public Date getDateAdded() {
		return dateAdded;
	}

	/**
	 * Gets the date modified
	 * 
	 * @return the dateModified
	 */
	public Date getDateModified() {
		return dateModified;
	}

	/**
	 * Sets the id
	 * 
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}
	
	/**
	 * Sets the if the chat is a group
	 * 
	 * @param isGroup the isGroup to set
	 */
	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}

	/**
	 * Sets the name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the participants
	 * 
	 * @param participants the participants to set
	 */
	public void setParticipants(Set<UUID> participants) {
		this.participants = participants;
	}

	/**
	 * Sets the date added
	 * 
	 * @param dateAdded the dateAdded to set
	 */
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	/**
	 * Sets the date modified
	 * 
	 * @param dateModified the dateModified to set
	 */
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
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
		Chat other = (Chat) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
