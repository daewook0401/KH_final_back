package com.nomnom.onnomnom.reservationSetting.model.dto;

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
public class AvailableTimeDTO {

	private String operatingHoursNo;
	private String restaurantNo;
	private String weekDay;
	private String reservationStartTime;
	private String reservationEndTime;
	
}
