package com.wsproject.clientsvr.dto;

import java.time.LocalDateTime;

import com.wsproject.clientsvr.domain.enums.WsType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WsPsl {
	private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private Long ownerIdx;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;
}
