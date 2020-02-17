package com.wsproject.clientsvr.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wsproject.clientsvr.oauth2.OAuthToken;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LoginController {
	
	private Gson gson;
	private RestTemplate restTemplate;
	
	@GetMapping("/login")
	public String login() {
		return "redirect:/oauth2/authorization/ws-project";
	}
	
	@GetMapping("/loginSuccess")
    public String loginComplete(Model model) {
		JsonObject object = (JsonObject) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("name", object.get("name"));
		
        return "loginSuccess";
    }
	
	@GetMapping("/login/oauth2/code")
	public String callbackSocial(@RequestParam("code") String code, HttpServletRequest req, Model model) {
		
		String credentials = "ws-project:zq8WAZ5V9GVQK6COD2TQSfvOzExRibD4";
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", code);
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", req.getRequestURL().toString());
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		
		try {
			ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8901/oauth/token", request, String.class);
			
			if(response.getStatusCode() == HttpStatus.OK) {
				OAuthToken token = gson.fromJson(response.getBody(), OAuthToken.class);
				headers = new HttpHeaders();
				String accessToken = token.getAccess_token();
				headers.add("Authorization", "Bearer " + accessToken);
				
				request = new HttpEntity<>(headers);
				
				response = restTemplate.exchange("http://localhost:8082/v1.0/users/me", HttpMethod.GET, request, String.class);
				
				JsonObject object = gson.fromJson(response.getBody(), JsonObject.class);
				
				SecurityContextHolder.getContext()
					.setAuthentication(new UsernamePasswordAuthenticationToken(object, encodedCredentials, AuthorityUtils.createAuthorityList("ROLE_USER")));	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/loginSuccess";
	}
}
