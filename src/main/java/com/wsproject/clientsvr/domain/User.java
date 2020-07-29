package com.wsproject.clientsvr.domain;

import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.wsproject.clientsvr.domain.enums.SocialType;

import lombok.Getter;

/**
 * 사용자 정보를 담는 class
 * @author mslim
 *
 */
@Getter
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
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
	}
}
