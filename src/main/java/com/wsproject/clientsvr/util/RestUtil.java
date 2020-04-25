package com.wsproject.clientsvr.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.wsproject.clientsvr.dto.TokenInfo;

import lombok.extern.slf4j.Slf4j;

/** 
 * RestTemplate을 이용한 요청에 대한 중복코드를 최소화 하기위해 생성한 유틸
 * @author mslim
 *
 */
@Slf4j
public class RestUtil {
	
	private String url;
	private HttpMethod method;
	private HttpHeaders headers;
	private TokenInfo tokenInfo;
	private Map<String, String[]> queryParams;
	private Object bodyParam;
	
	// 토큰 정보를 Header에 주입할 때 토큰 앞에 붙일 값
	private static final String BEARER_PREFIX = "Bearer ";
	
	public RestUtil(String url, HttpMethod method, HttpHeaders headers, TokenInfo tokenInfo, 
					Map<String, String[]> queryParams, Object bodyParam) {
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.tokenInfo = tokenInfo;
		this.queryParams = queryParams;
		this.bodyParam = bodyParam;
	}
	
	/** 
	 * RestTemplate과 객체에 있는 정보를 기반으로 http 요청을 한다.
	 * @return 리스판스
	 */
	public ResponseEntity<String> exchange() {
		log.debug("exchange started - url : {}", url);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		queryParams.entrySet().forEach(entry -> builder.queryParam(entry.getKey(), Arrays.asList(entry.getValue())));
		
		if(tokenInfo != null) {
			headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + tokenInfo.getAccess_token()); // 토큰 정보를 헤더에 주입
		}
		
		HttpEntity<Object> entity = new HttpEntity<Object>(bodyParam, headers);
		
		RestTemplate restTemplate = CommonUtil.getBean(RestTemplate.class);
		
		ResponseEntity<String> result;
		
		try {
			result = restTemplate.exchange(builder.toUriString(), method, entity, String.class);
		// 토큰이 만료됐을 경우 리프레시 토큰을 통해 재발급받는다
		} catch (HttpClientErrorException.Unauthorized e) {
			TokenUtil tokenUtil = CommonUtil.getBean(TokenUtil.class);
			
			TokenInfo refreshed = tokenUtil.refreshTokenInfo(tokenInfo.getRefresh_token());
			headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + refreshed.getAccess_token());
			entity = new HttpEntity<Object>(bodyParam, headers);
			
			result = restTemplate.exchange(builder.toUriString(), method, entity, String.class);
		}
		
		log.debug("exchange ended - url : {}", url);
		return result;
	}
	
	public static RestUtilBuilder builder() {
		return new RestUtilBuilder();
	}
	
	public static class RestUtilBuilder {
		
		private String url;
		private HttpMethod method = HttpMethod.GET;
		private HttpHeaders headers = new HttpHeaders();
		private TokenInfo tokenInfo;
		private Map<String, String[]> queryParams = new HashMap<>();
		private Object bodyParam;
		
		public RestUtilBuilder() {
			// Default Content-Type=application/json
			headers.setContentType(MediaType.APPLICATION_JSON);
		}

		public RestUtilBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		/** 
		 * HttpMethod를 GET으로 설정
		 * @return
		 */
		@Deprecated
		public RestUtilBuilder get() {
			method = HttpMethod.GET;
			return this;
		}
		
		/** 
		 * HttpMethod를 POST로 설정
		 * @return
		 */
		public RestUtilBuilder post() {
			method = HttpMethod.POST;
			return this;
		}
		
		/** 
		 * HttpMethod를 PUT으로 설정
		 * @return
		 */
		public RestUtilBuilder put() {
			method = HttpMethod.PUT;
			return this;
		}
		
		/** 
		 * HttpMethod를 DELETE로 설정
		 * @return
		 */
		public RestUtilBuilder delete() {
			method = HttpMethod.DELETE;
			return this;
		}
		
		/** 
		 * header에 key, value형태로 값을 추가
		 * @param headerName
		 * @param headerValue
		 * @return
		 */
		public RestUtilBuilder headers(String headerName, String headerValue) {
			headers.add(headerName, headerValue);
			return this;
		}
		
		/** 
		 * header에 contentType을 설정
		 * @param mediaType
		 * @return
		 */
		public RestUtilBuilder contentType(MediaType mediaType) {
			headers.setContentType(mediaType);
			return this;
		}
		
		/** 
		 * 요청에 쓰일 토큰정보 설정
		 * @param tokenInfo
		 * @return
		 */
		public RestUtilBuilder tokenInfo(TokenInfo tokenInfo) {
			this.tokenInfo = tokenInfo;
			return this;
		}
		
		/** 
		 * 쿼리파라미터를 key, value형태로 추가
		 * @param key
		 * @param values
		 * @return
		 */
		public RestUtilBuilder queryParam(String key, String... values) {
			queryParams.put(key, values);
			return this;
		}
		
		/**
		 * POST/PUT 전송 시 body로 보낼 파라미터를 지정
		 * @param bodyParam
		 * @return
		 */
		public RestUtilBuilder bodyParam(Object bodyParam) {
			this.bodyParam = bodyParam;
			return this;
		}
		
		public RestUtil build() {
			return new RestUtil(url, method, headers, tokenInfo, queryParams, bodyParam);
		}
	}
}
