package com.wsproject.clientsvr.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.wsproject.clientsvr.domain.enums.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author mslim
 * User 정보
 */
@Getter
@NoArgsConstructor
public class User implements Serializable {
	
	private static final long serialVersionUID = 6510441028359513508L;
	
	private Long idx;
	
	private String name;
	
	private String email;
	
	private String principal;
	
	private SocialType socialType;
	
	private String pictureUrl;
	
	private String uid;
	
	private List<String> roles = new ArrayList<String>();
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
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
