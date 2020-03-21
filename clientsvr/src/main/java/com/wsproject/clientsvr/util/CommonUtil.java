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
	
	/** '/' 경로에 쿠키를 추가한다
	 * @param name
	 * @param value
	 */
	public void addCookie(String name, String value) {
		addCookie(name, value, "/");
	}
	
	/** path에 쿠키를 추가한다
	 * @param name
	 * @param value
	 * @param path
	 */
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
	
	/** 암호화되어 저장되어있는 쿠키값을 객체로 변환하여 추출한다.
	 * @param <T>
	 * @param cookie
	 * @param classType
	 * @return 쿠키로부터 추출된 객체
	 */
	public <T> T extractCookie(String cookie, Class<T> classType) {
		return gson.fromJson(aesUtil.decrypt(cookie), classType);
	}
	
	/** Bean객체를 얻는다
	 * @param <T>
	 * @param classType
	 * @return Bean 객체
	 */
	public static <T> T getBean(Class<T> classType) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		
		return applicationContext.getBean(classType);
	}
}
