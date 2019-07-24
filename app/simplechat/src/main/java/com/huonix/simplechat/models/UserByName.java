package com.huonix.simplechat.models;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * UserByName entitiy
 * 
 * @author Kepler Vital
 *
 */
@Table("users_by_name")
public class UserByName implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "name", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String name;
	
	@PrimaryKeyColumn(name = "user_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private UUID userId;

	/**
	 * User must have all params
	 * 
	 * @param name the user name
	 * @param userId the user id
	 */
	public UserByName(String name, UUID userId) {
		super();
		this.name = name;
		this.userId = userId;
	}

	/**
	 * Get the user name
	 * 
	 * @return String the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the user id
	 * 
	 * @return UUID with the userId
	 */
	public UUID getUserId() {
		return userId;
	}

	/**
	 * Sets a new user name
	 * 
	 * @param name the name to set
	 * @return void
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets a new user id
	 * 
	 * @param userId the userId to set
	 * @return void
	 */
	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		UserByName other = (UserByName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}
