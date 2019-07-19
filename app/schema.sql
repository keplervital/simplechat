CREATE KEYSPACE simplechat WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE user (
	id uuid PRIMARY KEY,
	name varchar,
	avatar blob,
	mood text,
	access_key varchar,
	blocked boolean,
	admin boolean,
	date_added timestamp,
	date_modified timestamp
);

CREATE TABLE chat (
	id uuid PRIMARY KEY,
	group boolean,
	name varchar,
	participants set<uuid>,	
	date_added timestamp
);

CREATE TABLE message (
	id uuid PRIMARY KEY,
	chat_id uuid,
	user_id uuid,
	body text,
	date_added timestamp
);