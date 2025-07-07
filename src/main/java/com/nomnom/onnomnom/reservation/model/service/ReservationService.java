package com.nomnom.onnomnom.reservation.model.service;

import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;
import com.nomnom.onnomnom.reservation.model.dto.ResponseResultDTO;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingDTO;

public interface ReservationService {

	ObjectResponseWrapper<String> insertReservation(ReservationDTO reservationDTO);
	
	ObjectResponseWrapper<ReservationSettingDTO> selectReservationInfo(String restaurantNo);
	
	ObjectResponseWrapper<ResponseResultDTO> selectReservation(String restaurantNo, String reserveDay);
	
	ListResponseWrapper<ReservationDTO> selectReservationCheck(String restaurantNo);
	
	ObjectResponseWrapper<String> deleteReservation(String reservationNo);



}
