CREATE KEYSPACE 'simplechat' WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };

CREATE TABLE users (
	id uuid PARTITION KEY,
	access_key varchar CLUSTERING KEY,
	name varchar CLUSTERING KEY,
	avatar blob,
	mood text,
	blocked boolean,
	admin boolean,
	date_added timestamp,
	date_modified timestamp
);

CREATE TABLE chats (
	id uuid PARTITION KEY,
	is_group boolean,
	name varchar,
	participants set<uuid>,	
	date_added timestamp,
	date_modified timestamp
);

CREATE TABLE chat_participants (
	user_id uuid PARTITION KEY,
	chat_id uuid CLUSTERING KEY,
	date_added timestamp CLUSTERING KEY
);

CREATE TABLE messages (
	id uuid PARTITION KEY,
	chat_id uuid,
	user_id uuid,
	body text,
	date_added timestamp
);

CREATE TABLE chat_messages (
	chat_id uuid PARTITION KEY,
	date_added timestamp CLUSTERING KEY,
	user_id uuid CLUSTERING KEY,
	message_id uuid CLUSTERING KEY
);
