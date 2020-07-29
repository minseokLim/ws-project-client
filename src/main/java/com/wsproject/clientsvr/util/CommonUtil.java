package com.wsproject.clientsvr.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.wsproject.clientsvr.util.provider.ApplicationContextProvider;

public class CommonUtil {

	/** 
	 * '/' 경로에 쿠키를 암호화하여 추가한다
	 * @param name
	 * @param value
	 */
	public static void addCookie(String name, String value) {
		addCookie(name, value, "/");
	}
	
	/** 
	 * path에 쿠키를 암호화하여 추가한다
	 * @param name
	 * @param value
	 * @param path
	 */
	public static void addCookie(String name, String value, String path) {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		
		try {
			Cookie cookie = new Cookie(name, URLEncoder.encode(AESUtil.encrypt(value), "UTF-8"));
			cookie.setPath(path);
			response.addCookie(cookie);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/** 
	 * 암호화되어 저장되어있는 쿠키값을 객체로 변환하여 추출한다.
	 * @param <T>
	 * @param cookie
	 * @param classType
	 * @return 쿠키로부터 추출된 객체
	 */
	public static <T> T extractObjFromCookie(String cookie, Class<T> classType) {
		Gson gson = getBean(Gson.class);
		return gson.fromJson(AESUtil.decrypt(cookie), classType);
	}
	
	/**
	 * 쿠키 이름을 통해 request로부터 쿠키를 얻는다.
	 * @param request
	 * @param name
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getCookie(HttpServletRequest request, String name) throws UnsupportedEncodingException {
		
		String result = null;
		
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(name)) {
					result = URLDecoder.decode(cookie.getValue(), "UTF-8");
					break;
				}
			}
		}
		
		return result;
	}
	
	/** 
	 * Bean객체를 얻는다
	 * @param <T>
	 * @param classType
	 * @return Bean 객체
	 */
	public static <T> T getBean(Class<T> classType) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		
		return applicationContext.getBean(classType);
	}
}
