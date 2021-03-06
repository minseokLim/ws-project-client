package com.wsproject.clientsvr.dto;

import com.wsproject.clientsvr.domain.User;
import com.wsproject.clientsvr.domain.enums.SocialType;

import lombok.Getter;

/**
 * 사용자의 기본적인 정보를 쿠키에 저장하기 위해 쓰이는 class
 * @author mslim
 *
 */
@Getter
public class UserInfo {
	
	private Long idx;
	
	private String name;
	
	private String email;
	
	private String principal;
	
	private SocialType socialType;
	
	private String pictureUrl;
	
	private String uid;

	public UserInfo(User user) {
		this.idx = user.getIdx();
		this.name = user.getName();
		this.email = user.getEmail();
		this.principal = user.getPrincipal();
		this.socialType = user.getSocialType();
		this.pictureUrl = user.getPictureUrl();
		this.uid = user.getUid();
	}
}
