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
 * User entity
 * 
 * @author Kepler Vital
 *
 */
@Table("chats")
public class Chat implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID id = UUIDs.timeBased();
	
	@Column(value="name")
	private String name;
	
	@Column(value="participants")
	private Set<UUID> participants;
	
	@Column(value="date_added")
	private Date dateAdded;

	@Column(value="date_modified")
	private Date dateModified;
	
}
