package com.wsproject.clientsvr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.wsproject.clientsvr.domain.User;
import com.wsproject.clientsvr.oauth2.OAuthToken;
import com.wsproject.clientsvr.property.CustomProperties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class LoginController {
	
	private Gson gson;
	
	private RestTemplate restTemplate;
	
	private OAuth2ClientProperties oAuth2ClientProperties;
	
	private CustomProperties customProperties;
	
	@GetMapping("/login")
	public String login() {
		return "redirect:/oauth2/authorization/ws-project";
	}
	
	@GetMapping("/loginSuccess")
    public String loginComplete(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		model.addAttribute("name", user.getName());
		
        return "loginSuccess";
    }
	
	@GetMapping("/login/oauth2/code")
	public String actionLogin(@RequestParam("code") String code, HttpServletRequest request) {
		Registration registration = (Registration) oAuth2ClientProperties.getRegistration().values().toArray()[0];
		String credentials = registration.getClientId() + ":" + registration.getClientSecret();
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", code);
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", request.getRequestURL().toString());
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(customProperties.getApiGatewayIp() + "/api/authsvr/oauth/token", entity, String.class);
		
		if(response.getStatusCode() == HttpStatus.OK) {
			OAuthToken token = gson.fromJson(response.getBody(), OAuthToken.class);
			
			headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token.getAccess_token());
			entity = new HttpEntity<>(headers);
			
			response = restTemplate.exchange(customProperties.getApiGatewayIp() + "/api/user-service/v1.0/users/me", HttpMethod.GET, entity, String.class);
			
			if(response.getStatusCode() == HttpStatus.OK) {
				User user = gson.fromJson(response.getBody(), User.class);
				SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getIdx(), "N/A", user.getAuthorities()));
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
			} else {
				log.info("Failed to get user information to log in");
				return "redirect:/error";
			}		
		}
			
		return "redirect:/loginSuccess";
	}
}
