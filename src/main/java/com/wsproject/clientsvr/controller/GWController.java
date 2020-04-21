package com.wsproject.clientsvr.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.util.CommonUtil;
import com.wsproject.clientsvr.util.RestUtil;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class GWController {
	
	private static final String API_URL = "apiUrl";
	
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private static final String DELETE = "DELETE";
	
	@RequestMapping("/api")
	public ResponseEntity<String> requestAtApi(@CookieValue("userInfo") String userCookie, @CookieValue("tokenInfo") String tokenCookie, 
											   @RequestBody(required = false) Map<String, Object> bodyParam, HttpServletRequest request) {
		
		TokenInfo tokenInfo = CommonUtil.extractCookie(tokenCookie, TokenInfo.class);
		String userIdx = String.valueOf(CommonUtil.extractCookie(userCookie, UserInfo.class).getIdx());
		String apiUrl = request.getParameter(API_URL).replace("{userIdx}", userIdx);
		String method = request.getMethod();
		
		RestUtil.RestUtilBuilder restUtilBuilder = RestUtil.builder().url(apiUrl).tokenInfo(tokenInfo).bodyParam(bodyParam);
		RestUtil restUtil = null;
		
		switch (method) {
		case GET:
			restUtil = restUtilBuilder.get().build();
			break;
		case POST:
			restUtil = restUtilBuilder.post().build();
			break;
		case PUT:
			restUtil = restUtilBuilder.put().build();
			break;
		case DELETE:
			restUtil = restUtilBuilder.delete().build();
			break;
		}
		
		ResponseEntity<String> entity = restUtil.exchange();
		
		return new ResponseEntity<String>(entity.getBody(), entity.getStatusCode());
	}
}
