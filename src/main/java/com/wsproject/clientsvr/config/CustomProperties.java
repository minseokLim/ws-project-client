package com.wsproject.clientsvr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * application.yml에 custom으로 추가한 속성을 불러오는 class
 * @author mslim
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
	String apiBaseUri; // API server의 base uri. API Server의 zuul server에 접근할 수 있는, 외부로 노출되어 있는 uri
	String authsvrBaseUri; // 인증서버의 base uri. 인증/인가를 위한 화면이 인증서버에 존재하는데, 이 화면은 zuul server를 통해 접근하는 것이 불가능
						   // TODO 향후 인증/인가 화면도 클라이언트로 가져와서 모든 통신은 zuul server를 통해서만 할 수 있게 했으면 함
}
