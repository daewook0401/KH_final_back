package com.nomnom.onnomnom.reservationSetting.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AvailableTimeVo {
	
	private String operatingHoursNo;
	private String restaurantNo;
	private String weekDay;
	private String reservationStartTime;
	private String reservationEndTime;
	
}
