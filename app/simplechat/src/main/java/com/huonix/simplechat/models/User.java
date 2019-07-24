package com.huonix.simplechat.models;

import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;

import com.datastax.driver.core.utils.UUIDs;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

/**
 * User entity
 * 
 * @author Kepler Vital
 *
 */
@Table("users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID id = UUIDs.timeBased();

	@PrimaryKeyColumn(name = "access_key", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private String accessKey = UUIDs.timeBased().toString().replaceAll("-", "");

	@PrimaryKeyColumn(name = "name", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
	@NotBlank(message = "Name is required")
	private String name;

	@Column
	private String avatar;

	@Column
	private String mood;

	@Column
	private Boolean blocked;

	@Column
	private Boolean admin;

	@Column(value="date_added")
	private Date dateAdded;

	@Column(value="date_modified")
	private Date dateModified;

	/**
	 * User constructor
	 * 
	 * @param id the user id
	 * @param admin if the user has admin rights
	 * @param name the user name
	 */
	public User(final UUID id, final Boolean admin, final String name) {
		this.id = id;
		this.admin = admin;
		this.name = name;
	}

	/**
	 * Gets the user id
	 * 
	 * @return UUID the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Gets the user name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the user image
	 * 
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Gets the user mood
	 * 
	 * @return the mood
	 */
	public String getMood() {
		return mood;
	}

	/**
	 * Gets the user access key
	 * 
	 * @return the access_key
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * Gets if the usar is blocked
	 * 
	 * @return the blocked
	 */
	public Boolean getBlocked() {
		return blocked;
	}

	/**
	 * Gets if the user has admin rights
	 * 
	 * @return the admin
	 */
	public Boolean getAdmin() {
		return admin;
	}

	/**
	 * Gets the date added
	 * 
	 * @return the date_added
	 */
	public Date getDateAdded() {
		return dateAdded;
	}

	/**
	 * Gets the last date modified
	 * 
	 * @return the date_modified
	 */
	public Date getDateModified() {
		return dateModified;
	}

	/**
	 * Sets the user id
	 * 
	 * @param id the id to set
	 * @return void
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Sets the user name
	 * 
	 * @param name the name to set
	 * @return void
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the user image
	 * 
	 * @param avatar the avatar to set
	 * @return void
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * Sets the user mood
	 * 
	 * @param mood the mood to set
	 * @return void
	 */
	public void setMood(String mood) {
		this.mood = mood;
	}

	/**
	 * Sets the user api key
	 * 
	 * @param access_key the access_key to set
	 * @return void
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	/**
	 * Sets the user blocked
	 * 
	 * @param blocked the blocked to set
	 * @return void
	 */
	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	/**
	 * Sets if a user is admin
	 * @param admin the admin to set
	 * @return void
	 */
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	/**
	 * Sets the date the user was added
	 * 
	 * @param date_added the date_added to set
	 * @return void
	 */
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	/**
	 * Sets the date the user was modified
	 * 
	 * @param date_modified the date_modified to set
	 * @return void
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
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		}
		User other = (User) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
