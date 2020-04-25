package com.wsproject.clientsvr.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.util.CommonUtil;
import com.wsproject.clientsvr.util.RestUtil;
import com.wsproject.clientsvr.util.RestUtil.RestUtilBuilder;

import lombok.AllArgsConstructor;

/**
 * API server에 대한 호출을 전담하는 컨트롤러
 * @author mslim
 *
 */
@RestController
@AllArgsConstructor
public class GWController {
	
	private static final String API_URL = "apiUrl";
	
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private static final String DELETE = "DELETE";
	
	/**
	 * API server에 대한 요청을 담당하는 공통함수 <br>
	 * 파라미터에 호출할 api url, api에 전송할 data를 포함해서, api 호출에 사용해야할 Http Method를 이용해서 호출하면, API server로 해당 호출을 해준다.<br>
	 * 쿠키에 저장되어있는 token정보와 사용자정보를 이용해서 API server로 적절한 요청을 해준다.
	 * <br>
	 * <pre>
	 * var apiUrl = 'https://api.mslim8803.shop/api/ws-service/wses/{userIdx}';
	 * var data = {
	 * 	userIdx : 1,
	 * 	content : 'test',
	 * };
	 * 
	 * $.ajax({
	 * 	url: '/api?apiUrl=' + encodeURIComponent(apiUrl),
	 * 	type: 'POST',
	 * 	data: JSON.stringify(data),
	 * 	contentType : "application/json; charset=UTF-8",
	 * 	success: function(result) {
	 * 		callbackFunc(result);
	 * 	}
	 * });
	 * </pre>
	 * @param userCookie
	 * @param tokenCookie
	 * @param bodyParam
	 * @param request
	 * @return API server로부터 반환된 리스판스
	 */
	@RequestMapping("/api")
	public ResponseEntity<String> requestAtApi(@CookieValue("userInfo") String userCookie, @CookieValue("tokenInfo") String tokenCookie, 
											   @RequestBody(required = false) Map<String, Object> bodyParam, HttpServletRequest request) {
		
		TokenInfo tokenInfo = CommonUtil.extractCookie(tokenCookie, TokenInfo.class);
		String userIdx = String.valueOf(CommonUtil.extractCookie(userCookie, UserInfo.class).getIdx());
		String apiUrl = request.getParameter(API_URL).replace("{userIdx}", userIdx);
		String method = request.getMethod();
		
		RestUtilBuilder restUtilBuilder = RestUtil.builder().url(apiUrl).tokenInfo(tokenInfo).bodyParam(bodyParam);
		RestUtil restUtil = null;
		
		switch (method) {
		case GET:
			restUtil = restUtilBuilder.build();
			break;
		case POST:
			restUtil = restUtilBuilder.post().build();
			break;
		case PUT:
			restUtil = restUtilBuilder.put().build();
			break;
		case DELETE:
			restUtil = restUtilBuilder.delete().build();
			break;
		}
		
		ResponseEntity<String> response = restUtil.exchange();
		
		// API server로부터 반환된 response를 그대로 반환해도 된다고 생각할 수 있으나, 그럴 경우 클라이언트에서 CORS관련 에러가 발생한다.
		// 따라서 반환된 response를 기반으로 다시 ResponseEntity를 생성하여 반환한다.
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}
}
