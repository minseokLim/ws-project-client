package com.wsproject.clientsvr.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.wsproject.clientsvr.domain.User;
import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.property.CustomProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
	
	private final Gson gson;
	
	private final RestTemplate restTemplate;
	
	private final OAuth2ClientProperties oAuth2ClientProperties;
	
	private final CustomProperties properties;
		
	@Value("${encrypt.key}")
	private String encryptKey;
	
	@GetMapping("/login")
	public String login() {
		return "redirect:/oauth2/authorization/ws-project";
	}
	
	@GetMapping("/loginFailed")
	public String loginFailed() {
		return "loginFailed";
	}
	
	@GetMapping("/loginCompleted")
    public String loginCompleted(@CookieValue("userInfo") String userCookie, Model model) {
		UserInfo userInfo = gson.fromJson(userCookie, UserInfo.class);
		
		model.addAttribute("name", userInfo.getName());
		model.addAttribute("socialType", userInfo.getSocialType().getValue().toUpperCase());
		
        return "loginCompleted";
    }
	
	@GetMapping("/login/oauth2/code")
	public String actionLogin(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
		Registration registration = (Registration) oAuth2ClientProperties.getRegistration().values().toArray()[0];
		String credentials = registration.getClientId() + ":" + registration.getClientSecret();
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		String redirectUri = request.getRequestURL().toString();
		
		TokenInfo tokenInfo = requestTokenInfo(code, encodedCredentials, redirectUri);
		
		if(tokenInfo == null) {
			return "redirect:/loginFailed";
		}
		
		User user = requestUserInfo(tokenInfo);
		
		if(user == null) {
			return "redirect:/loginFailed";
		}
		
		try {
			loginProcess(response, tokenInfo, user);
		} catch (Exception e) {
			if(log.isDebugEnabled()) {
				e.printStackTrace();
			} else {
				log.error(e.getMessage());
			}
			
			return "redirect:/loginFailed";
		}

		return "redirect:/loginCompleted";
	}

	private void loginProcess(HttpServletResponse response, TokenInfo tokenInfo, User user) throws UnsupportedEncodingException {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getIdx(), "N/A", user.getAuthorities()));
		
		Cookie tokenCookie = new Cookie("tokenInfo", URLEncoder.encode(gson.toJson(tokenInfo), "UTF-8"));
		tokenCookie.setPath("/");
		Cookie userCookie = new Cookie("userInfo", URLEncoder.encode(gson.toJson(new UserInfo(user)), "UTF-8"));
		userCookie.setPath("/");
		
		response.addCookie(tokenCookie);
		response.addCookie(userCookie);
	}
	
	private User requestUserInfo(TokenInfo tokenInfo) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + tokenInfo.getAccess_token());
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(properties.getApiBaseUri() + "/user-service/v1.0/users/me", HttpMethod.GET, entity, String.class);
		
		if(response.getStatusCode() == HttpStatus.OK) {
			User user = gson.fromJson(response.getBody(), User.class);
			return user;
		} else {
			log.error("Failed to get user information to log in");
			return null;
		}
	}

	private TokenInfo requestTokenInfo(String code, String encodedCredentials, String redirectUri) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", code);
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", redirectUri);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(properties.getApiBaseUri() + "/authsvr/oauth/token", entity, String.class);
		
		if(response.getStatusCode() == HttpStatus.OK) {
			TokenInfo tokenInfo = gson.fromJson(response.getBody(), TokenInfo.class);
			
			log.debug("access_token : {}", tokenInfo.getAccess_token());
			log.debug("refresh_token : {}", tokenInfo.getRefresh_token());
			
			return tokenInfo;
		} else {
			log.error("Failed to get token from authorization server");
			return null;
		}
	}
}
