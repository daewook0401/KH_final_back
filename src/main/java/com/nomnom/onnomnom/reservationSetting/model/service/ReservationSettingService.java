package com.nomnom.onnomnom.reservationSetting.model.service;

import java.util.Map;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingRequestDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;

public interface ReservationSettingService {

	ObjectResponseWrapper<String> insertSetting(ReservationSettingRequestDTO request);

	ObjectResponseWrapper<ReservationSettingRequestDTO> selectSetting();

	ObjectResponseWrapper<String> updateSetting(ReservationSettingRequestDTO request);

	ObjectResponseWrapper<String> deleteSetting(String restaurantNo);

	ObjectResponseWrapper<RestaurantDTO> selectMyRestaurant();

}
