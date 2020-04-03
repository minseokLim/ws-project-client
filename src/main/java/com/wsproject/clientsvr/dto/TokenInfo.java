package com.wsproject.clientsvr.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mslim
 * 토큰 정보
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
