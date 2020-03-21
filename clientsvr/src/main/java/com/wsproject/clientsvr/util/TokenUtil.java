package com.wsproject.clientsvr.util;

import javax.annotation.PostConstruct;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.property.CustomProperties;

import lombok.RequiredArgsConstructor;

/** 토큰 발급/재발급을 관리하는 유틸
 * @author mslim
 *
 */
@Component
@RequiredArgsConstructor
public class TokenUtil {
	
	private final OAuth2ClientProperties oAuth2ClientProperties;
	
	private final Gson gson;
	
	private final CustomProperties properties;
	
	private final RestTemplate restTemplate;
	
	private final CommonUtil commonUtil;
	
	private String url;
	
	private HttpHeaders headers = new HttpHeaders();
	
	@PostConstruct
	private void init() {
		Registration registration = (Registration) oAuth2ClientProperties.getRegistration().values().toArray()[0];
		String credentials = registration.getClientId() + ":" + registration.getClientSecret();
		String authorization = "Basic " + new String(Base64.encodeBase64(credentials.getBytes()));
		
		url = properties.getApiBaseUri() + "/authsvr/oauth/token";
		
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		headers.add(HttpHeaders.AUTHORIZATION, authorization);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	}
	
	/** 인증서버에 토큰 정보 요청
	 * @param code
	 * @param redirectUri
	 * @return 토큰정보
	 */
	public TokenInfo getTokenInfo(String code, String redirectUri) {		
		MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<String, Object>();
		bodyParams.add("code", code);
		bodyParams.add("grant_type", "authorization_code");
		bodyParams.add("redirect_uri", redirectUri);
		
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String,Object>>(bodyParams, headers);
		
		String response = restTemplate.postForObject(url, entity, String.class);
		
		TokenInfo tokenInfo = gson.fromJson(response, TokenInfo.class);
		
		commonUtil.addCookie("tokenInfo", gson.toJson(tokenInfo));
		
		return tokenInfo;
	}
	
	/** 인증서버에 리프레시 토큰을 통해 토큰정보 재발급 요청
	 * @param refreshToken
	 * @return 토큰정보
	 */
	public TokenInfo refreshTokenInfo(String refreshToken) {		
		MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<String, Object>();
		bodyParams.add("grant_type", "refresh_token");
		bodyParams.add("refresh_token", refreshToken);
		
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String,Object>>(bodyParams, headers);
		
		String response = restTemplate.postForObject(url, entity, String.class);
		
		TokenInfo tokenInfo = gson.fromJson(response, TokenInfo.class);
		
		commonUtil.addCookie("tokenInfo", gson.toJson(tokenInfo));
		
		return tokenInfo;
	}
}
