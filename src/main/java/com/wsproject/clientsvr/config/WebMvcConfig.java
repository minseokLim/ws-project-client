package com.wsproject.clientsvr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wsproject.clientsvr.util.interceptor.AdminCheckInterceptor;
import com.wsproject.clientsvr.util.interceptor.UserInfoInterceptor;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
	
	private UserInfoInterceptor userInfoInterceptor;
	
	private AdminCheckInterceptor adminCheckInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userInfoInterceptor).excludePathPatterns("/api/**", "/login/**");
		registry.addInterceptor(adminCheckInterceptor).addPathPatterns("/admin/**");
	}
}
