package com.wsproject.clientsvr.dto;

import com.wsproject.clientsvr.domain.enums.WsType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mslim
 * 오늘의 명언
 */
@Getter
@Setter
public class TodaysWs {
	
	private Long userIdx;
	
	private String content;
	
	private String author;
	
	private WsType type;
}
