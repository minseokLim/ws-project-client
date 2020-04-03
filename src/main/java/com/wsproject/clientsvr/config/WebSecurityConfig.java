package com.wsproject.clientsvr.config;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.wsproject.clientsvr.util.OAuth2Provider;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private OAuth2Provider oAuth2Provider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8");
		
		http.authorizeRequests()
				.antMatchers("/login/**", "/oauth2/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.oauth2Login()
				.loginPage("/login")
				.defaultSuccessUrl("/ws-service/main")
				.failureUrl("/error")
			.and()
				.headers().frameOptions().disable()
			.and()
				.exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
			.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
				.deleteCookies("JSESSIONID", "userInfo", "tokenInfo")
				.invalidateHttpSession(true)
			.and()
				.addFilterBefore(filter, CsrfFilter.class)
				.csrf().disable();
	}
	
	/**
	 * application.yml에 있는 oauthclient정보를 통해 빈객체 생성
	 * @param oAuth2ClientProperties
	 * @return oauthclient정보를 관리하는 리파지토리
	 */
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties) {
		List<ClientRegistration> registrations = oAuth2ClientProperties.getRegistration().keySet().stream()
												.map(client -> getRegistration(oAuth2ClientProperties, client))
												.filter(Objects::nonNull)
												.collect(Collectors.toList());
		
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
	private ClientRegistration getRegistration(OAuth2ClientProperties oAuth2ClientProperties, String client) {
		OAuth2ClientProperties.Registration registration = oAuth2ClientProperties.getRegistration().get(client);
		
		return oAuth2Provider.getBuilder(client)
				.clientId(registration.getClientId())
				.clientSecret(registration.getClientSecret())
				.build();
	}
}
