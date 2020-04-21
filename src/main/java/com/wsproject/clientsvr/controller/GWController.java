package com.wsproject.clientsvr.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.util.CommonUtil;
import com.wsproject.clientsvr.util.RestUtil;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GWController {
	
	private static final String API_URL = "apiUrl";
	
	@GetMapping
	public ResponseEntity<String> getRequestAtApi(@CookieValue("tokenInfo") String tokenCookie, HttpServletRequest request) {
		
		TokenInfo tokenInfo = CommonUtil.extractCookie(tokenCookie, TokenInfo.class);
		
		String apiUrl = request.getParameter(API_URL);
		
		RestUtil restUtil = RestUtil.builder().url(apiUrl).get().tokenInfo(tokenInfo).build();
		
		ResponseEntity<String> entity = restUtil.exchange();
		
		return new ResponseEntity<String>(entity.getBody(), entity.getStatusCode());
	}
	
	@PostMapping
	public ResponseEntity<String> postRequestAtApi(@CookieValue("tokenInfo") String tokenCookie, @RequestBody Map<String, Object> bodyParam) {
		
		TokenInfo tokenInfo = CommonUtil.extractCookie(tokenCookie, TokenInfo.class);
		
		String apiUrl = (String) bodyParam.get(API_URL);
		bodyParam.remove(API_URL);
		
		RestUtil restUtil = RestUtil.builder().url(apiUrl).post().tokenInfo(tokenInfo).bodyParam(bodyParam).build();

		ResponseEntity<String> entity = restUtil.exchange();
		
		return new ResponseEntity<String>(entity.getBody(), entity.getStatusCode());
	}
	
	@PutMapping
	public ResponseEntity<String> putRequestAtApi(@CookieValue("tokenInfo") String tokenCookie, @RequestBody Map<String, Object> bodyParam) {
		
		TokenInfo tokenInfo = CommonUtil.extractCookie(tokenCookie, TokenInfo.class);
		
		String apiUrl = (String) bodyParam.get(API_URL);
		bodyParam.remove(API_URL);
		
		RestUtil restUtil = RestUtil.builder().url(apiUrl).put().tokenInfo(tokenInfo).bodyParam(bodyParam).build();

		ResponseEntity<String> entity = restUtil.exchange();
		
		return new ResponseEntity<String>(entity.getBody(), entity.getStatusCode());
	}
}
