package com.wsproject.clientsvr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.gson.Gson;
import com.wsproject.clientsvr.config.CustomProperties;
import com.wsproject.clientsvr.dto.TodaysWs;
import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.util.CommonUtil;
import com.wsproject.clientsvr.util.RestUtil;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class WsController {
	
	private Gson gson;
	
	private CommonUtil commonUtil;
	
	private CustomProperties properties;
	
	/** 오늘의 명언 메인화면
	 * @param userCookie
	 * @param tokenCookie
	 * @param model
	 * @return
	 */
	@GetMapping(value = {"/ws-service/main", "/"})
	public String main(@CookieValue("userInfo") String userCookie, @CookieValue("tokenInfo") String tokenCookie, Model model) {
		
		UserInfo userInfo = commonUtil.extractCookie(userCookie, UserInfo.class);
		TokenInfo tokenInfo = commonUtil.extractCookie(tokenCookie, TokenInfo.class);
		
		RestUtil restUtil = RestUtil.builder().url("/ws-service/v1.0/users/" + userInfo.getIdx() + "/todaysWs").get().tokenInfo(tokenInfo).build();
				
		ResponseEntity<String> entity = restUtil.exchange();
		
		TodaysWs todaysWs = gson.fromJson(entity.getBody(), TodaysWs.class);
		
		model.addAttribute("todaysWs", todaysWs);
		model.addAttribute("authsvrBaseUri", properties.getAuthsvrBaseUri());
		
		return "ws-service/main";
	}
	
	@GetMapping("/ws-service/saveWsPage")
	public String saveWsPage() {
		return "ws-service/saveWsPage";
	}
}
