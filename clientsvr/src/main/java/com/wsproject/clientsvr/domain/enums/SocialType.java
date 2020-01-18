package com.wsproject.clientsvr.domain.enums;

public enum SocialType {
	FACEBOOK("facebook"),
	GOOGLE("google"),
	KAKAO("kakao");
	
	private String name;
	
	SocialType(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return name;
	}
}
