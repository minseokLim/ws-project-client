package com.wsproject.clientsvr.util;

import javax.annotation.PostConstruct;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;
import com.wsproject.clientsvr.config.CustomProperties;
import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.util.RestUtil.RestUtilBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** 
 * 토큰 발급/재발급을 관리하는 유틸
 * @author mslim
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtil {
	
	private final OAuth2ClientProperties oAuth2ClientProperties;
	
	private final Gson gson;
	
	private final CustomProperties properties;
	
	private RestUtilBuilder restUtilBuilder;
	
	@PostConstruct
	private void init() {
		Registration registration = (Registration) oAuth2ClientProperties.getRegistration().values().toArray()[0];
		String credentials = registration.getClientId() + ":" + registration.getClientSecret();
		String authorization = "Basic " + new String(Base64.encodeBase64(credentials.getBytes()));
		
		String url = properties.getApiBaseUri() + "/authsvr/oauth/token";
		
		restUtilBuilder = RestUtil.builder().url(url).post().headers(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
															.headers(HttpHeaders.AUTHORIZATION, authorization)
															.contentType(MediaType.APPLICATION_FORM_URLENCODED);
	}
	
	/** 
	 * 인증서버에 토큰 정보 요청
	 * @param code
	 * @param redirectUri
	 * @return 토큰정보
	 */
	public TokenInfo getTokenInfo(String code, String redirectUri) {
		log.info("getTokenInfo started");
		
		MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<String, Object>();
		bodyParams.add("code", code);
		bodyParams.add("grant_type", "authorization_code");
		bodyParams.add("redirect_uri", redirectUri);
		
		ResponseEntity<String> responseEntity = restUtilBuilder.bodyParam(bodyParams).build().exchange();
		
		TokenInfo tokenInfo = gson.fromJson(responseEntity.getBody(), TokenInfo.class);
		
		// 토큰 정보를 암호화하여 쿠키에 저장
		CommonUtil.addCookie("tokenInfo", gson.toJson(tokenInfo));
		
		log.info("getTokenInfo ended");
		return tokenInfo;
	}
	
	/** 
	 * 인증서버에 리프레시 토큰을 통해 토큰정보 재발급 요청
	 * @param refreshToken
	 * @return 토큰정보
	 */
	public TokenInfo refreshTokenInfo(String refreshToken) {
		log.info("refreshTokenInfo started");
		
		MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<String, Object>();
		bodyParams.add("grant_type", "refresh_token");
		bodyParams.add("refresh_token", refreshToken);
		
		ResponseEntity<String> responseEntity = restUtilBuilder.bodyParam(bodyParams).build().exchange();
		
		TokenInfo tokenInfo = gson.fromJson(responseEntity.getBody(), TokenInfo.class);
		
		// 토큰 정보를 암호화하여 쿠키에 저장
		CommonUtil.addCookie("tokenInfo", gson.toJson(tokenInfo));
		
		log.info("refreshTokenInfo ended");
		return tokenInfo;
	}
}
