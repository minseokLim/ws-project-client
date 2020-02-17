package com.wsproject.clientsvr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
//	
//	private HandlerMethodArgumentResolver userArgumentResolver;
//
//	@Override
//	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//		argumentResolvers.add(userArgumentResolver);
//	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
