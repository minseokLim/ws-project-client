package com.wsproject.clientsvr.util;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CommonUtil {

	private AESUtil aesUtil;
	
	public void addCookie(String name, String value) throws Exception {
		addCookie(name, value, "/");
	}
	
	public void addCookie(String name, String value, String path) throws Exception {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		
		Cookie cookie = new Cookie(name, URLEncoder.encode(aesUtil.encrypt(value), "UTF-8"));
		cookie.setPath(path);
		response.addCookie(cookie);
	}
}
