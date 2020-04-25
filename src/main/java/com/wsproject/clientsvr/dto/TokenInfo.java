package com.wsproject.clientsvr.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 토큰 정보를 담는 class
 * @author mslim
 *
 */
@Getter
@Setter
public class TokenInfo {
	
	private String access_token;
	private String token_type;
	private String refresh_token;
	private long expires_in;
	private String scope;
}
