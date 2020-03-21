package com.wsproject.clientsvr.domain.enums;

/**
 * @author mslim
 * Facebook, Google, Kakao 등의 Oauth2 Login 종류
 */
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
