package com.wsproject.clientsvr.resolver;

import static com.wsproject.clientsvr.domain.enums.SocialType.FACEBOOK;
import static com.wsproject.clientsvr.domain.enums.SocialType.GOOGLE;
import static com.wsproject.clientsvr.domain.enums.SocialType.KAKAO;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.UriComponentsBuilder;

import com.wsproject.clientsvr.annotation.SocialUser;
import com.wsproject.clientsvr.domain.User;
import com.wsproject.clientsvr.domain.enums.RoleType;
import com.wsproject.clientsvr.property.CustomProperties;

import lombok.AllArgsConstructor;

// TODO 레스트 템플릿 최적화 및 예외 처리 해야함. 일단 지금은 되는지만 보자
@Component
@AllArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
		
	private CustomProperties customProperties;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(SocialUser.class) != null && parameter.getParameterType().equals(User.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
		User user = (User) session.getAttribute("user");
		return getUser(user, session);
	}

	private User getUser(User user, HttpSession session) {
        if(user == null) {
            try {
                OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                Map<String, Object> map = authentication.getPrincipal().getAttributes();
                User convertedUser = convertUser(authentication.getAuthorizedClientRegistrationId(), map);
                
                Map<String, String> param = new HashMap<String, String>();
                param.put("principal", convertedUser.getPrincipal());
                param.put("socialType", convertedUser.getSocialType().getValue().toUpperCase());
                
                RestTemplate restTemplate = new RestTemplate();
                
                try {
                	UriComponentsBuilder builder 
                		= UriComponentsBuilder.fromHttpUrl("http://localhost:5555/api/user-service/v1.0/users/search/findByPrincipalAndSocialType")
                			.queryParam("principal", convertedUser.getPrincipal())
                			.queryParam("socialType", convertedUser.getSocialType().getValue().toUpperCase());
                	
                	user = restTemplate.getForObject(builder.toUriString(), User.class);
                	
				} catch (HttpClientErrorException.NotFound e) {
					user = restTemplate.postForObject("http://localhost:5555/api/user-service/v1.0/users", convertedUser, User.class);
				}
               
                setRoleIfNotSame(user, authentication, map);
                session.setAttribute("user", user);
            } catch (ClassCastException e) {
            	e.printStackTrace();
                return user;
            }
        }
        
        return user;
    }

    private User convertUser(String registrationId, Map<String, Object> map) {
        if(FACEBOOK.getValue().equals(registrationId)) {
        	return getFacebookUser(map);
        } else if(GOOGLE.getValue().equals(registrationId)) {
        	return getGoogleUser(map);
        } else if(KAKAO.getValue().equals(registrationId)) {
        	return getKaKaoUser(map);
        }
        
        return null;
    }
    
    private User getFacebookUser(Map<String, Object> map) {
    	@SuppressWarnings("unchecked")
		Object pictureUrl = ((HashMap<String, Object>) ((HashMap<String, Object>) map.get("picture")).get("data")).get("url");
    	String userEmail = convertObjToStr(map.get("email"));
    	    	
		return User.builder()
                .name(convertObjToStr(map.get("name")))
                .email(userEmail)
                .principal(convertObjToStr(map.get("id")))
                .socialType(FACEBOOK)
                .pictureUrl(convertObjToStr(pictureUrl))
                .roleType(getRoleType(userEmail))
                .build();
    }

    private User getGoogleUser(Map<String, Object> map) {
    	String userEmail = convertObjToStr(map.get("email"));
    	
		return User.builder()
                .name(convertObjToStr(map.get("name")))
                .email(userEmail)
                .principal(convertObjToStr(map.get(IdTokenClaimNames.SUB)))
                .socialType(GOOGLE)
                .pictureUrl(convertObjToStr(map.get("picture")))
                .roleType(getRoleType(userEmail))
                .build();
    }
    
    private User getKaKaoUser(Map<String, Object> map) {
        @SuppressWarnings("unchecked")
		Map<String, String> propertyMap = (HashMap<String, String>) map.get("properties");
        
        String userEmail = convertObjToStr(map.get("kaccount_email"));
		return User.builder()
                .name(propertyMap.get("nickname"))
                .email(userEmail)
                .principal(convertObjToStr(map.get("id")))
                .socialType(KAKAO)
                .pictureUrl(propertyMap.get("thumbnail_image"))
                .roleType(getRoleType(userEmail))
                .build();
    }

    private void setRoleIfNotSame(User user, OAuth2AuthenticationToken authentication, Map<String, Object> map) {
        if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority(user.getRoleType().getValue()))) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.createAuthorityList(user.getRoleType().getValue())));
        }
    }
    
    private RoleType getRoleType(String userEmail) {
		RoleType roleType = RoleType.USER;
    	
    	if(customProperties.getAdminEmails().contains(userEmail)) {
    		roleType = RoleType.ADMIN;
    	}
    	
		return roleType;
	}
    
    private String convertObjToStr(Object obj) {
    	if(obj != null) {
    		return String.valueOf(obj);
    	} else {
    		return null;
    	}
    }
}
