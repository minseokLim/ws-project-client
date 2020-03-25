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

import com.wsproject.clientsvr.config.CustomProperties;
import com.wsproject.clientsvr.dto.TokenInfo;

import lombok.extern.slf4j.Slf4j;

/** Rest 요청에 대한 중복코드를 최소화 하기위해 생성한 유틸
 * @author mslim
 *
 */
@Slf4j
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
	
	/** 객체에 있는 정보를 기반으로 Rest요청을 한다.
	 * @return 리스판스
	 */
	public ResponseEntity<String> exchange() {
		log.info("exchange started - url : {}", url);
		CustomProperties properties = CommonUtil.getBean(CustomProperties.class);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(properties.getApiBaseUri() + url);
		queryParams.entrySet().forEach(entry -> builder.queryParam(entry.getKey(), entry.getValue()));
		
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenInfo.getAccess_token());
		
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String,Object>>(bodyParams, headers);
		
		RestTemplate restTemplate = CommonUtil.getBean(RestTemplate.class);
		
		ResponseEntity<String> result;
		
		try {
			result = restTemplate.exchange(builder.toUriString(), method, entity, String.class);
		// 토큰이 만료됐을 경우 리프레시 토큰을 통해 재발급받는다
		} catch (HttpClientErrorException.Unauthorized e) {
			TokenUtil tokenUtil = CommonUtil.getBean(TokenUtil.class);
			
			TokenInfo refreshed = tokenUtil.refreshTokenInfo(tokenInfo.getRefresh_token());
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + refreshed.getAccess_token());
			entity = new HttpEntity<MultiValueMap<String,Object>>(bodyParams, headers);
			
			result = restTemplate.exchange(builder.toUriString(), method, entity, String.class);
		}
		
		log.info("exchange ended - url : {}", url);
		return result;
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
		
		/** HttpMethod를 GET으로 설정
		 * @return
		 */
		public RestUtilBuilder get() {
			method = HttpMethod.GET;
			return this;
		}
		
		/** HttpMethod를 POST로 설정
		 * @return
		 */
		public RestUtilBuilder post() {
			method = HttpMethod.POST;
			return this;
		}
		
		/** HttpMethod를 PUT으로 설정
		 * @return
		 */
		public RestUtilBuilder put() {
			method = HttpMethod.PUT;
			return this;
		}
		
		/** HttpMethod를 DELETE로 설정
		 * @return
		 */
		public RestUtilBuilder delete() {
			method = HttpMethod.DELETE;
			return this;
		}
		
		/** header에 key, value형태로 값을 추가
		 * @param headerName
		 * @param headerValue
		 * @return
		 */
		public RestUtilBuilder headers(String headerName, String headerValue) {
			headers.add(headerName, headerValue);
			return this;
		}
		
		/** header에 contentType을 설정
		 * @param mediaType
		 * @return
		 */
		public RestUtilBuilder contentType(MediaType mediaType) {
			headers.setContentType(mediaType);
			return this;
		}
		
		/** 요청에 쓰일 토큰정보 설정
		 * @param tokenInfo
		 * @return
		 */
		public RestUtilBuilder tokenInfo(TokenInfo tokenInfo) {
			this.tokenInfo = tokenInfo;
			return this;
		}
		
		/** 쿼리파라미터를 key, value형태로 추가
		 * @param key
		 * @param values
		 * @return
		 */
		public RestUtilBuilder queryParams(String key, Object... values) {
			queryParams.put(key, values);
			return this;
		}

		/** 바디에 들어갈 파라미터를 key, value 형태로 추가
		 * @param key
		 * @param value
		 * @return
		 */
		public RestUtilBuilder bodyParams(String key, Object value) {
			bodyParams.add(key, value);
			return this;
		}
		
		public RestUtil build() {
			return new RestUtil(url, method, headers, tokenInfo, queryParams, bodyParams);
		}
	}
}
