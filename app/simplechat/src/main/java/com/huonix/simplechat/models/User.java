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

	@Column
	private Date date_added;

	@Column
	private Date date_modified;

	/**
	 * 
	 * @param id
	 * @param admin
	 * @param name
	 */
	public User(final UUID id, final Boolean admin, final String name) {
		this.id = id;
		this.admin = admin;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @return the mood
	 */
	public String getMood() {
		return mood;
	}

	/**
	 * @return the access_key
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * @return the blocked
	 */
	public Boolean getBlocked() {
		return blocked;
	}

	/**
	 * @return the admin
	 */
	public Boolean getAdmin() {
		return admin;
	}

	/**
	 * @return the date_added
	 */
	public Date getDateAdded() {
		return date_added;
	}

	/**
	 * @return the date_modified
	 */
	public Date getDateModified() {
		return date_modified;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @param mood the mood to set
	 */
	public void setMood(String mood) {
		this.mood = mood;
	}

	/**
	 * @param access_key the access_key to set
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	/**
	 * @param blocked the blocked to set
	 */
	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	/**
	 * @param date_added the date_added to set
	 */
	public void setDateAdded(Date dateAdded) {
		this.date_added = dateAdded;
	}

	/**
	 * @param date_modified the date_modified to set
	 */
	public void setDateModified(Date dateModified) {
		this.date_modified = dateModified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((accessKey == null) ? 0 : accessKey.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
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
