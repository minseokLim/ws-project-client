package com.wsproject.clientsvr.util.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * "/admin/**" 경로로 접근하는 사용자가 ADMIN 권한을 가지고 있는지의 여부를 체크
 * ADMIN 권한이 없을 경우 error페이지로 리다이렉트
 * @author mslim
 */
@Component
public class AdminCheckInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.stream().filter(elem -> "ROLE_ADMIN".equals(elem.getAuthority())).count() > 0 ? true : false;
		
		if(!isAdmin) {
			response.sendRedirect("/error");
		}
		
		return true;
	}
}
