package com.wsproject.clientsvr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.gson.Gson;
import com.wsproject.clientsvr.dto.TodaysWs;
import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.property.CustomProperties;
import com.wsproject.clientsvr.service.RestService;
import com.wsproject.clientsvr.util.AESUtil;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class WsController {
	
	private Gson gson;
	
//	private CommonUtil commonUtil;
	
	private AESUtil aesUtil;
	
	private RestService restService;
	
	private CustomProperties properties;
		
	@GetMapping(value = {"/ws-service/main", "/"})
	public String main(@CookieValue("userInfo") String userCookie, @CookieValue("tokenInfo") String tokenCookie, Model model) {
		
		try {
			UserInfo userInfo = gson.fromJson(aesUtil.decrypt(userCookie), UserInfo.class);
			TokenInfo tokenInfo = gson.fromJson(aesUtil.decrypt(tokenCookie), TokenInfo.class);
			
			String url = properties.getApiBaseUri() + "/ws-service/v1.0/users/" + userInfo.getIdx() + "/todaysWs";
			
			ResponseEntity<String> entity = restService.getForEntity(url, tokenInfo);
			
			TodaysWs todaysWs = gson.fromJson(entity.getBody(), TodaysWs.class);
			
			model.addAttribute("todaysWs", todaysWs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/ws-service/main";
	}
}
