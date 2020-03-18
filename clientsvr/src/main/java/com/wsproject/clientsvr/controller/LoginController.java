package com.wsproject.clientsvr.controller;

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
import com.wsproject.clientsvr.util.CommonUtil;
import com.wsproject.clientsvr.util.RestUtil;
import com.wsproject.clientsvr.util.TokenUtil;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LoginController {
	
	private Gson gson;
	
	private CommonUtil commonUtil;
		
	private TokenUtil tokenUtil;
		
	@GetMapping("/login")
	public String login() {
		return "redirect:/oauth2/authorization/ws-project";
	}
	
	@GetMapping("/login/oauth2/code")
	public String actionLogin(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
		String redirectUri = request.getRequestURL().toString();
		
		TokenInfo tokenInfo = tokenUtil.getTokenInfo(code, redirectUri);
		RestUtil restUtil = RestUtil.builder().url("/user-service/v1.0/users/me").get().tokenInfo(tokenInfo).build();
		ResponseEntity<String> result = restUtil.exchange();
		
		User user = gson.fromJson(result.getBody(), User.class);
		
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getIdx(), "N/A", user.getAuthorities()));
		commonUtil.addCookie("userInfo", gson.toJson(new UserInfo(user)));

		return "redirect:/";
	}
}
