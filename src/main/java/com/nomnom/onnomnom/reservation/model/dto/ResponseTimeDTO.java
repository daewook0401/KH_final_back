package com.nomnom.onnomnom.reservation.model.dto;

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
public class ResponseTimeDTO {

	private String breakStartTime;
	private String breakEndTime;
	private String reservationStartTime;
	private String reservationEndTime;
	private int interval;
	
}
