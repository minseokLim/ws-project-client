package com.wsproject.clientsvr.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.wsproject.clientsvr.domain.User;
import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.property.CustomProperties;
import com.wsproject.clientsvr.service.RestService;
import com.wsproject.clientsvr.util.CommonUtil;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LoginController {
	
	private Gson gson;
	
	private CommonUtil commonUtil;
		
	private RestService restService;
	
	private CustomProperties properties;
	
	@GetMapping("/login")
	public String login() {
		return "redirect:/oauth2/authorization/ws-project";
	}
	
	@GetMapping("/login/oauth2/code")
	public String actionLogin(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String redirectUri = request.getRequestURL().toString();
		
		try {
			TokenInfo tokenInfo = restService.getTokenInfo(code, redirectUri, null, false);
			
			ResponseEntity<String> responseEntity = restService.getForEntity(properties.getApiBaseUri() + "/user-service/v1.0/users/me", tokenInfo);
			User user = gson.fromJson(responseEntity.getBody(), User.class);
			
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getIdx(), "N/A", user.getAuthorities()));
			commonUtil.addCookie("userInfo", gson.toJson(new UserInfo(user)));
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/loginFailed";
		}
		
		return "redirect:/ws-service/main";
	}
}
