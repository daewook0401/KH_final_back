package com.nomnom.onnomnom.operatingHours.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OperatingHoursDTO {

	private String operatingHoursNo;
	private String restaurantNo;
	private String weekDay;
	private String startTime;
	private String endTime;
	private String breakStartTime;
	private String breakEndTime;
	
}
