package com.wsproject.clientsvr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.clientsvr.dto.TokenInfo;
import com.wsproject.clientsvr.dto.UserInfo;
import com.wsproject.clientsvr.dto.WsPsl;
import com.wsproject.clientsvr.util.CommonUtil;
import com.wsproject.clientsvr.util.RestUtil;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class WsRestController {
	
	@PostMapping("/ws-service/wsPsls")
	public ResponseEntity<String> insertWsPersonal(@CookieValue("userInfo") String userCookie, @CookieValue("tokenInfo") String tokenCookie, @RequestBody WsPsl wsPsl) {
		
		UserInfo userInfo = CommonUtil.extractCookie(userCookie, UserInfo.class);
		TokenInfo tokenInfo = CommonUtil.extractCookie(tokenCookie, TokenInfo.class);
		
		wsPsl.setOwnerIdx(userInfo.getIdx());
		
		RestUtil restUtil = RestUtil.builder().url("/ws-service/v1.0/users/" + userInfo.getIdx() + "/wses")
											  .post().tokenInfo(tokenInfo).bodyParam(wsPsl)
											  .build();
	
		ResponseEntity<String> entity = restUtil.exchange();
		
		return new ResponseEntity<String>(entity.getBody(), HttpStatus.CREATED);
	}
}
