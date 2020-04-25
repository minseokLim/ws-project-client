package com.wsproject.clientsvr.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/** 
 * static 메서드에서도 Bean객체를 사용할 수 있게 하기위해 applicationContext를 제공
 * @author mslim
 * 
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		applicationContext = ctx;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
