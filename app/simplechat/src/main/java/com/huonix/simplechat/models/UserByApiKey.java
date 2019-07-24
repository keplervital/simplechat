package com.huonix.simplechat.models;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.utils.UUIDs;

/**
 * UserByApiKey entity
 * 
 * @author Kepler Vital
 *
 */
@Table("users_by_api_keys")
public class UserByApiKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "key", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String key = UUIDs.timeBased().toString().replaceAll("-", "");

	@PrimaryKeyColumn(name = "user_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private UUID userId;
	
	/**
	 * User must have all params
	 * 
	 * @param key the api key
	 * @param userId the user id
	 */
	public UserByApiKey(final String key, final UUID userId) {
		this.key = key;
		this.userId = userId;
	}

	/**
	 * Gets the api key
	 * 
	 * @return String the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the user id
	 * 
	 * @return UUID the userId
	 */
	public UUID getUserId() {
		return userId;
	}

	/**
	 * Sets the api key
	 * 
	 * @param key the key to set
	 * @return void
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Sets the user id
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
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		UserByApiKey other = (UserByApiKey) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}
