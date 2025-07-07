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
public class ReservationSettingDTO {

	private String restaurantNo;
	private int interval;
	private int maxNum;
	private int minNum;
	private int maxTeamNum;
	private String description;
	
}
