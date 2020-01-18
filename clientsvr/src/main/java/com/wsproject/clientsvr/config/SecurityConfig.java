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
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.wsproject.clientsvr.oauth2.CustomOAuth2Provider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8");
		
		// TODO 권한별 허용 url 설정 필요(ADMIN, USER)
		http.authorizeRequests()
				.antMatchers("/", "/login/**", "/css/**", "/images/**", "/js/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.oauth2Login()
				.defaultSuccessUrl("/loginSuccess")
				.failureUrl("/loginFailure")
			.and()
				.headers().frameOptions().disable()
			.and()
				.exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//			.and()
//				.formLogin()
//				.successForwardUrl("/formLoginSuccess")
			.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true) // TODO need to do
			.and()
				.addFilterBefore(filter, CsrfFilter.class)
				.csrf().disable();
	}
	
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

		if("facebook".equals(client)) {
			return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
					.clientId(registration.getClientId())
					.clientSecret(registration.getClientSecret())
					// picture을 받아오기 위해 별도로 userInfoUri 설정
					.userInfoUri("https://graph.facebook.com/me?fields=id,name,email,picture")
					.build();
		} else if("google".equals(client)) {
			return CommonOAuth2Provider.GOOGLE.getBuilder(client)
					.clientId(registration.getClientId())
					.clientSecret(registration.getClientSecret())
					.build();
		} else if("kakao".equals(client)) {
			return CustomOAuth2Provider.KAKAO.getBuilder(client)
					.clientId(registration.getClientId())
					.build();
		}
		
		return null;
	}
}
