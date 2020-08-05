package com.wsproject.clientsvr.util.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.util.CommonUtil;

/**
 * 로그인된 상태의 모든 요청의 Model객체에 userInfo객체와 관리자여부를 주입
 * @author mslim
 *
 */
@Component
public class UserInfoInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		
		if(modelAndView != null) {
			UserInfo userInfo = CommonUtil.extractObjFromCookie(CommonUtil.getCookie(request, "userInfo"), UserInfo.class);
			modelAndView.addObject("userInfo", userInfo);

			boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
									.stream().filter(elem -> "ROLE_ADMIN".equals(elem.getAuthority())).count() > 0 ? true : false;
			
			modelAndView.addObject("isAdmin", isAdmin);
		}
	}
}
