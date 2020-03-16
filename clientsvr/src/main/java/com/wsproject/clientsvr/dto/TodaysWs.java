package com.wsproject.clientsvr.dto;

import com.wsproject.clientsvr.dto.enums.WsType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodaysWs {
	
	private Long userIdx;
	
	private String content;
	
	private String author;
	
	private WsType type;
}
