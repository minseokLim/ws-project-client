package com.wsproject.clientsvr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.wsproject.clientsvr.config.CustomProperties;
import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.util.CommonUtil;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class WsController {
	
	private CustomProperties properties;
	
	/**
	 * 오늘의 명언 메인화면
	 * @param userCookie
	 * @param model
	 * @return
	 */
	@GetMapping(value = {"/ws-service/main", "/"})
	public String main(@CookieValue("userInfo") String userCookie, Model model) {
		Long userIdx = CommonUtil.extractObjFromCookie(userCookie, UserInfo.class).getIdx();
		String apiUrl = properties.getApiBaseUri() + "/ws-service/v1.0/users/" + userIdx + "/todaysWs";
		model.addAttribute("apiUrl", apiUrl);
		
		return "ws-service/main";
	}
	
	/**
	 * 명언 추가 화면
	 * @param userCookie
	 * @param model
	 * @return
	 */
	@GetMapping("/ws-service/addWs")
	public String addWs(@CookieValue("userInfo") String userCookie, Model model) {
		Long userIdx = CommonUtil.extractObjFromCookie(userCookie, UserInfo.class).getIdx();
		String apiUrl = properties.getApiBaseUri() + "/ws-service/v1.0/users/" + userIdx + "/wses";
		model.addAttribute("apiUrl", apiUrl);
		
		return "ws-service/addWs";
	}

	/**
	 * 내가 추가한 명언 목록 화면
	 * @param userCookie
	 * @param model
	 * @return
	 */
	@GetMapping("/ws-service/wsList")
	public String wsList(@CookieValue("userInfo") String userCookie, Model model) {
		Long userIdx = CommonUtil.extractObjFromCookie(userCookie, UserInfo.class).getIdx();
		String apiUrl = properties.getApiBaseUri() + "/ws-service/v1.0/users/" + userIdx + "/wses";
		model.addAttribute("apiUrl", apiUrl);
		
		return "ws-service/wsList";
	}
	
	/**
	 * 내가 추가한 명언 상세 보기 화면
	 * @return
	 */
	@GetMapping("/ws-service/wsDetail")
	public String wsDetail() {
		return "ws-service/wsDetail";
	}
	
	/**
	 * 내가 추가한 명언 수정하기 화면
	 * @return
	 */
	@GetMapping("/ws-service/editWs")
	public String editWs() {
		return "ws-service/editWs";
	}
}
