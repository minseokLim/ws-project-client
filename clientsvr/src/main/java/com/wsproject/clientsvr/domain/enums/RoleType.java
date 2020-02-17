package com.wsproject.clientsvr.domain.enums;

public enum RoleType {
	ADMIN("ADMIN"),
	USER("USER");
	
	private final static String prefix = "ROLE_";
	private String name;
	
	RoleType(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return prefix + name;
	}
}
