package com.nomnom.onnomnom.reservationSetting.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReservationSettingVo {

	private String restaurantNo;
	private int interval;
	private int maxNum;
	private int minNum;
	private int maxTeamNum;
	private String description;
}
