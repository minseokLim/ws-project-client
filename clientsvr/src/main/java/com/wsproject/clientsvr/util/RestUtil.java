package com.wsproject.clientsvr.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.property.CustomProperties;

public class RestUtil {
	
	private String url;
	private HttpMethod method;
	private HttpHeaders headers;
	private TokenInfo tokenInfo;
	private Map<String, Object[]> queryParams;
	private MultiValueMap<String, Object> bodyParams;
	
	public RestUtil(String url, HttpMethod method, HttpHeaders headers, TokenInfo tokenInfo, 
					Map<String, Object[]> queryParams, MultiValueMap<String, Object> bodyParams) {
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.tokenInfo = tokenInfo;
		this.queryParams = queryParams;
		this.bodyParams = bodyParams;
	}
	
	public ResponseEntity<String> exchange() {
		CustomProperties properties = CommonUtil.getBean(CustomProperties.class);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(properties.getApiBaseUri() + url);
		queryParams.entrySet().forEach(entry -> builder.queryParam(entry.getKey(), entry.getValue()));
		
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenInfo.getAccess_token());
		
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String,Object>>(bodyParams, headers);
		
		RestTemplate restTemplate = CommonUtil.getBean(RestTemplate.class);
		
		try {
			return restTemplate.exchange(builder.toUriString(), method, entity, String.class);
		// 토큰이 만료됐을 경우 리프레시 토큰을 통해 재발급받는다
		} catch (HttpClientErrorException.Unauthorized e) {
			TokenUtil tokenUtil = CommonUtil.getBean(TokenUtil.class);
			
			TokenInfo refreshed = tokenUtil.refreshTokenInfo(tokenInfo.getRefresh_token());
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + refreshed.getAccess_token());
			entity = new HttpEntity<MultiValueMap<String,Object>>(bodyParams, headers);
			
			return restTemplate.exchange(builder.toUriString(), method, entity, String.class);
		}
	}
	
	public static RestUtilBuilder builder() {
		return new RestUtilBuilder();
	}
	
	public static class RestUtilBuilder {
		
		private String url;
		private HttpMethod method;
		private HttpHeaders headers = new HttpHeaders();
		private TokenInfo tokenInfo;
		private Map<String, Object[]> queryParams = new HashMap<>();
		private MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<String, Object>();
		
		public RestUtilBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		public RestUtilBuilder get() {
			method = HttpMethod.GET;
			return this;
		}
		
		public RestUtilBuilder post() {
			method = HttpMethod.POST;
			return this;
		}
		
		public RestUtilBuilder put() {
			method = HttpMethod.PUT;
			return this;
		}
		
		public RestUtilBuilder delete() {
			method = HttpMethod.DELETE;
			return this;
		}
		
		public RestUtilBuilder headers(String headerName, String headerValue) {
			headers.add(headerName, headerValue);
			return this;
		}
		
		public RestUtilBuilder contentType(MediaType mediaType) {
			headers.setContentType(mediaType);
			return this;
		}
		
		public RestUtilBuilder tokenInfo(TokenInfo tokenInfo) {
			this.tokenInfo = tokenInfo;
			return this;
		}
		
		public RestUtilBuilder queryParams(String key, Object... values) {
			queryParams.put(key, values);
			return this;
		}

		public RestUtilBuilder bodyParams(String key, String value) {
			bodyParams.add(key, value);
			return this;
		}
		
		public RestUtil build() {
			return new RestUtil(url, method, headers, tokenInfo, queryParams, bodyParams);
		}
	}
}
