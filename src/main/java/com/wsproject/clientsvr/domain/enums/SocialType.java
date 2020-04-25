package com.wsproject.clientsvr.domain.enums;

/**
 * Social 서비스 종류 <br>
 * 사용자가 OAuth2를 통해 로그인할 수 있는 서비스 종류
 * @author mslim
 *
 */
public enum SocialType {
	FACEBOOK("facebook"),
	GOOGLE("google"),
	KAKAO("kakao"),
	GITHUB("github");
	
	private String name;
	
	SocialType(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return name;
	}
}
