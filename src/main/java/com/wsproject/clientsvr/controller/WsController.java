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
	
	/** 오늘의 명언 메인화면
	 * @param userCookie
	 * @param tokenCookie
	 * @param model
	 * @return
	 */
	@GetMapping(value = {"/ws-service/main", "/"})
	public String main(Model model) {
		String apiUrl = properties.getApiBaseUri() + "/ws-service/v1.0/users/{userIdx}/todaysWs";
		model.addAttribute("apiUrl", apiUrl);
		
		return "ws-service/main";
	}
	
	@GetMapping("/ws-service/saveWsPsl")
	public String saveWsPsl(@CookieValue("userInfo") String userCookie, Model model) {
		Long userIdx = CommonUtil.extractCookie(userCookie, UserInfo.class).getIdx();
		String apiUrl = properties.getApiBaseUri() + "/ws-service/v1.0/users/{userIdx}/wses";
		model.addAttribute("apiUrl", apiUrl);
		model.addAttribute("userIdx", userIdx);
		
		return "ws-service/saveWsPsl";
	}
	
	@GetMapping("/ws-service/wsPslList")
	public String wsPslList(Model model) {
		String apiUrl = properties.getApiBaseUri() + "/ws-service/v1.0/users/{userIdx}/wses";
		model.addAttribute("apiUrl", apiUrl);
		
		return "ws-service/wsPslList";
	}
	
	@GetMapping("/ws-service/viewWsPsl")
	public String viewWsPsl() {
		return "ws-service/viewWsPsl";
	}
	
	@GetMapping("/ws-service/modifyWsPsl")
	public String modifyWsPsl(Model model) {
		return "ws-service/modifyWsPsl";
	}
}
