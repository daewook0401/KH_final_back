package com.nomnom.onnomnom.chat.model.vo;

import java.util.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChattingListVo {

	private Long chatNo;
	private String roomNo;
	private String memberNo;
	private String content;
	private Date createDate;
}