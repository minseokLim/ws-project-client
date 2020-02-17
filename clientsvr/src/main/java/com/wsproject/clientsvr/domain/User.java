package com.wsproject.clientsvr.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	
	private String uid;
	
	private List<String> roles = new ArrayList<String>();
	
	private LocalDateTime createdDate;

	@Builder
	public User(Long idx, String name, String email, String principal, SocialType socialType, String pictureUrl,
			String uid, List<String> roles, LocalDateTime createdDate) {
		this.idx = idx;
		this.name = name;
		this.email = email;
		this.principal = principal;
		this.socialType = socialType;
		this.pictureUrl = pictureUrl;
		this.uid = uid;
		this.roles = roles;
		this.createdDate = createdDate;
	}
}
