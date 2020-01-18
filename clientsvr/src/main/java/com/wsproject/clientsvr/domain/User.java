package com.wsproject.clientsvr.domain;

import java.time.LocalDateTime;

import com.wsproject.clientsvr.domain.enums.RoleType;
import com.wsproject.clientsvr.domain.enums.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {
	
	private Long idx;
	
	private String name;
	
	private String email;
	
	private String principal;
	
	private SocialType socialType;
	
	private String pictureUrl;
	
	private RoleType roleType;
	
	private LocalDateTime createdDate;
	
	@Builder
	public User(Long idx, String name, String email, String principal, SocialType socialType, String pictureUrl,
			RoleType roleType, LocalDateTime createdDate) {
		this.idx = idx;
		this.name = name;
		this.email = email;
		this.principal = principal;
		this.socialType = socialType;
		this.pictureUrl = pictureUrl;
		this.roleType = roleType;
		this.createdDate = createdDate;
	}	
}
