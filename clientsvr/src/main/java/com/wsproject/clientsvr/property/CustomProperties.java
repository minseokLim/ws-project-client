package com.wsproject.clientsvr.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/** appication.yml에서 관리되는 커스텀 프로퍼티
 * @author mslim
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
	String apiBaseUri;
	String authsvrBaseUri;
}
