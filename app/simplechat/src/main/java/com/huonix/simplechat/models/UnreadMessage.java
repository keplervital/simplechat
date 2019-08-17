package com.huonix.simplechat.models;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.redis.core.RedisHash;

import net.minidev.json.JSONObject;

@RedisHash("UnreadMessage")
public class UnreadMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Map<String, Integer> unread;
	
	public UnreadMessage(String id,  Map<String, Integer> unread) {
		this.id = id;
		this.unread = unread;
	}

	/**
	 * @return the userId
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the unread
	 */
	public Map<String, Integer> getUnread() {
		return unread;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param unread the unread to set
	 */
	public void setUnread(Map<String, Integer> unread) {
		this.unread = unread;
	}
	
	@Override
	public String toString() {
		JSONObject response = new JSONObject();
		JSONObject chats = new JSONObject();
		for(Map.Entry<String, Integer> entry : unread.entrySet()) {
			chats.put(entry.getKey(), entry.getValue());
		}
		response.put("id", this.id);
		response.put("unread", chats);
		return "UnreadMessage{" + response.toString() + "}";
	}	
	
}
