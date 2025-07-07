package com.nomnom.onnomnom.chat.model.vo;

import java.util.Date;

import lombok.Builder;
import lombok.Value;
@Value
@Builder
public class ChattingRoomVo {
	private String roomNo;
	private String memberNo;
	private Date createDate;
}
