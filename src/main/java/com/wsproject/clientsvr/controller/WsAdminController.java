package com.wsproject.clientsvr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wsproject.clientsvr.config.CustomProperties;
import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.util.CommonUtil;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class WsAdminController {

	private CustomProperties properties;
	
	/**
	 * 관리자 메인 화면
	 * @param userCookie
	 * @param model
	 * @return
	 */
	@GetMapping(value = {"/ws-service/main", ""})
	public String main(@CookieValue("userInfo") String userCookie, Model model) {
		Long userIdx = CommonUtil.extractObjFromCookie(userCookie, UserInfo.class).getIdx();
		String apiUrl = properties.getApiBaseUri() + "/ws-service/v1.0/users/" + userIdx + "/todaysWs";
		model.addAttribute("apiUrl", apiUrl);
		
		return "admin/ws-service/main";
	}
	
	/**
	 * 명언 추가 화면 (관리자)
	 * @param model
	 * @return
	 */
	@GetMapping("/ws-service/addWs")
	public String addWs(Model model) {
		String apiUrl = properties.getApiBaseUri() + "/ws-service/v1.0/wses";
		model.addAttribute("apiUrl", apiUrl);
		
		return "admin/ws-service/addWs";
	}
	
	/**
	 * 명언 목록 화면 (관리자)
	 * @param model
	 * @return
	 */
	@GetMapping("/ws-service/wsList")
	public String wsList(Model model) {
		String apiUrl = properties.getApiBaseUri() + "/ws-service/v1.0/wses";
		model.addAttribute("apiUrl", apiUrl);
		
		return "admin/ws-service/wsList";
	}
	
	/**
	 * 명언 상세보기 화면 (관리자)
	 * @return
	 */
	@GetMapping("/ws-service/wsDetail")
	public String wsDetail() {
		return "admin/ws-service/wsDetail";
	}
	
	/**
	 * 명언 수정하기 화면 (관리자)
	 * @return
	 */
	@GetMapping("/ws-service/editWs")
	public String editWs() {
		return "admin/ws-service/editWs";
	}
}
