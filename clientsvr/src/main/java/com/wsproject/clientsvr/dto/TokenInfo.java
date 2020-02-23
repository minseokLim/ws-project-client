package com.wsproject.clientsvr.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenInfo {
	private String access_token;
	private String token_type;
	private String refresh_token;
	private long expires_in;
	private String scope;
	
	@Builder
	public TokenInfo(String access_token, String token_type, String refresh_token, long expires_in, String scope) {
		this.access_token = access_token;
		this.token_type = token_type;
		this.refresh_token = refresh_token;
		this.expires_in = expires_in;
		this.scope = scope;
	}	
}
