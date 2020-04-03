package com.wsproject.clientsvr.domain.enums;

/**
 * @author mslim
 * 권한 정보
 */
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
