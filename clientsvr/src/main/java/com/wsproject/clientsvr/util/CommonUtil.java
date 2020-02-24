package com.wsproject.clientsvr.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.property.CustomProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommonUtil {

	private final RestTemplate restTemplate;
	
	private final OAuth2ClientProperties oAuth2ClientProperties;
	
	private final CustomProperties properties;
	
	private final Gson gson;
	
	@Value("${encrypt.key}")
	private String encryptKey;
	
	public ResponseEntity<String> getForEntity(String url, TokenInfo tokenInfo) throws UnsupportedEncodingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + tokenInfo.getAccess_token());
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity;
		
		try {
			responseEntity = restTemplate.exchange(properties.getApiBaseUri() + url, HttpMethod.GET, entity, String.class);
		} catch (HttpClientErrorException.Unauthorized e) {
			tokenInfo = getTokenInfo(null, null, tokenInfo.getRefresh_token(), true);
			
			headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + tokenInfo.getAccess_token());
			entity = new HttpEntity<>(headers);
			responseEntity = restTemplate.exchange(properties.getApiBaseUri() + url, HttpMethod.GET, entity, String.class);
		}
		
		return responseEntity;
	}
	
	public TokenInfo getTokenInfo(String code, String redirectUri, String refreshToken, boolean refresh) throws UnsupportedEncodingException {
		Registration registration = (Registration) oAuth2ClientProperties.getRegistration().values().toArray()[0];
		String credentials = registration.getClientId() + ":" + registration.getClientSecret();
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		
		if(refresh) {
			params.add("refresh_token", refreshToken);
			params.add("grant_type", "refresh_token");
		} else {
			params.add("code", code);
			params.add("grant_type", "authorization_code");
			params.add("redirect_uri", redirectUri);
		}
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
		
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(properties.getApiBaseUri() + "/authsvr/oauth/token", entity, String.class);
		
		addCookie("tokenInfo", responseEntity.getBody());
		
		TokenInfo tokenInfo = gson.fromJson(responseEntity.getBody(), TokenInfo.class);
		
		log.debug("access_token : {}", tokenInfo.getAccess_token());
		log.debug("refresh_token : {}", tokenInfo.getRefresh_token());
		
		return tokenInfo;
	}
	
	public void addCookie(String name, String value) throws UnsupportedEncodingException {
		addCookie(name, value, "/");
	}
	
	public void addCookie(String name, String value, String path) throws UnsupportedEncodingException {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		
		Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
		cookie.setPath(path);
		response.addCookie(cookie);
	}
}
