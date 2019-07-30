package com.huonix.simplechat.enums;

public enum ERole {
	
	ADMIN("admin"),
	DEFAULT("default"),
	GUEST("guest");
	
	private String role;
	
	ERole(String role) {
		this.role = role;
	}
	
	public String role() {
        return role;
    }
	
}
