package com.nomnom.onnomnom.operatingHours.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OpeningHoursVo {

	private String operatingHoursNo;
	private String restaurantNo;
	private String weekDay;
	private String startTime;
	private String endTime;
	private String breakStartTime;
	private String breakEndTime;
}
