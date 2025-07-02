package com.nomnom.onnomnom.reservationSetting.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReservationSettingRequestDTO {
	
	    private List<AvailableTimeDTO> reservation;      
	    private ReservationSettingDTO  settingInfo;     
}