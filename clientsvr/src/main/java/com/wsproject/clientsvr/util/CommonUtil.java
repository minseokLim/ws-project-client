package com.wsproject.clientsvr.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class CommonUtil {

	@Value("${encrypt.key}")
	private String encryptKey;
	
	public void addCookie(String name, String value) throws UnsupportedEncodingException {
		addCookie(name, value, "/");
	}
	
	public void addCookie(String name, String value, String path) throws UnsupportedEncodingException {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		
		Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
		cookie.setPath(path);
		response.addCookie(cookie);
	}
}
