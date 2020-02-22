package com.wsproject.clientsvr.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

import com.wsproject.clientsvr.property.CustomProperties;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OAuth2Provider {
	
	private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code";
//	private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";
	
	private CustomProperties properties;
	
	public Builder getBuilder(String registrationId) {
		ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.POST, DEFAULT_REDIRECT_URL);
        builder.scope("profile");
        builder.authorizationUri(properties.getAuthsvrBaseUri() + "/oauth/authorize");
        builder.tokenUri(properties.getApiBaseUri() + "/authsvr/oauth/token");
        builder.userInfoUri(properties.getApiBaseUri() + "/user-service/v1.0/users/me");
        builder.userNameAttributeName("idx");
        builder.clientName("WS-Project");
		return builder;
	}		 

	protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method, String redirectUri) {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
		builder.clientAuthenticationMethod(method);
		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
		builder.redirectUriTemplate(redirectUri);
		return builder;
	}
}
