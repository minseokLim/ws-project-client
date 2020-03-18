package com.wsproject.clientsvr.util;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.wsproject.clientsvr.provider.ApplicationContextProvider;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CommonUtil {

	private AESUtil aesUtil;
	
	private Gson gson;
	
	public void addCookie(String name, String value) {
		addCookie(name, value, "/");
	}
	
	public void addCookie(String name, String value, String path) {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		
		try {
			Cookie cookie = new Cookie(name, URLEncoder.encode(aesUtil.encrypt(value), "UTF-8"));
			cookie.setPath(path);
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public <T> T extractCookie(String cookie, Class<T> classType) {
		return gson.fromJson(aesUtil.decrypt(cookie), classType);
	}
	
	public static <T> T getBean(Class<T> classType) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		
		return applicationContext.getBean(classType);
	}
}
