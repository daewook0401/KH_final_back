package com.nomnom.onnomnom.reservationSetting.model.service;

import java.util.Map;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingRequestDTO;

public interface ReservationSettingService {

	ObjectResponseWrapper<String> insertSetting(ReservationSettingRequestDTO request);

	ObjectResponseWrapper<ReservationSettingRequestDTO> selectSetting(String restaurantNo);

	ObjectResponseWrapper<String> updateSetting(ReservationSettingRequestDTO request);

	ObjectResponseWrapper<String> deleteSetting(String restaurantNo);

}
