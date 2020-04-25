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
import com.wsproject.clientsvr.config.CustomProperties;
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
	
	private TokenUtil tokenUtil;
	
	private CustomProperties properties;
	
	/** 
	 * 로그인 페이지로 이동
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "redirect:/oauth2/authorization/ws-project";
	}
	
	/** 
	 * 인증서버의 인증을 거친후 리다이렉트되는 url <br>
	 * 인증서버로부터 받은 code 값을 바탕으로 인증서버에 토큰을 요청하고, 토큰을 바탕으로 사용자정보를 API server에 요청한다. <br>
	 * 이 때, 토큰과 사용자 정보는 암호화되어 쿠키에 저장된다.
	 * @param code
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/login/oauth2/code")
	public String actionLogin(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
		String redirectUri = request.getRequestURL().toString();
		
		// 토큰 요청
		TokenInfo tokenInfo = tokenUtil.getTokenInfo(code, redirectUri);
		
		// 토큰을 기반으로 사용자 정보 요청
		RestUtil restUtil = RestUtil.builder().url(properties.getApiBaseUri() + "/user-service/v1.0/users/me").tokenInfo(tokenInfo).build();
		ResponseEntity<String> result = restUtil.exchange();
		
		User user = gson.fromJson(result.getBody(), User.class);
		
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getIdx(), "N/A", user.getAuthorities()));
		// 사용자 정보를 암호화하여 쿠키에 저장
		CommonUtil.addCookie("userInfo", gson.toJson(new UserInfo(user)));

		return "redirect:/";
	}
}
