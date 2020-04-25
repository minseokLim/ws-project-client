package com.wsproject.clientsvr.domain.enums;

/**
 * 권한 종류 <br>
 * 관리자(ADMIN)과 일반사용자(USER) 두 종류가 있다.
 * @author mslim
 *
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
