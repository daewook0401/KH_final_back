package com.nomnom.onnomnom.reservation.model.service;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;

public interface ReservationService {

	ObjectResponseWrapper<String> insertReservation(ReservationDTO reservationDTO);

	ObjectResponseWrapper<String> selectReservation(ReservationDTO reservationDTO);

	ObjectResponseWrapper<String> deleteReservation(ReservationDTO reservationDTO);
	
	/* 1. 예약 조회
	 * 2. 예약 등록
	 * 3. 예약 삭제
	 * 4. 예약 설정 등록
	 * 5. 예약 설정 조회
	 * 6. 예약 설정 수정
	 * 7. 예약 설정 삭제
	 * 
	 */
}
