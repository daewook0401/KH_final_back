package com.nomnom.onnomnom.operating.model.dto;


import java.time.LocalDateTime;

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
public class OperatingDTO {

	private String operatingHoursNo;
	private String restaurantNo;
	private String weekDay;
	private String startTime;
	private String endTime;
	private String breakStartTime;
	private String breakEndTime;
	private LocalDateTime createDate;
	
}
