package com.nomnom.onnomnom.chat.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChattingRoom {
	
	private String roomNo;
	private String memberNo;
	private Date createDate;
}
