package com.nomnom.onnomnom.reservation.model.service;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingDTO;

public interface ReservationService {

	/**
	 *  1. reserveDay에서 요일을 빼냄 monday, restaurantNo으로 operatingHoursNo으로 예약 가능한 시간들 조회하기
	 *  2. reserveTime이 예약 가능한 시간 리스트에 있는 시간이라면 insert하기 
	 *  3. 근데 insert 전에 restaurantNo으로 numberofGuests개수 구해서 restaurantNo,reserveDay,reserveTime을 where절에 조건으로 둬서
	 *   count해서 numberofGuests보다 작을 때 insert할 수 있어야함.....
	 *
	 * @param reservationDTO
	 * @return
	 */
	ObjectResponseWrapper<String> insertReservation(ReservationDTO reservationDTO);
	
	
	ObjectResponseWrapper<ReservationSettingDTO> selectReservationInfo(String restaurantNo);
	
	/*
	 *  요일별 예약 가능 시각 반환
	 *  
	 */
	ObjectResponseWrapper<String> selectReservation(String restaurantNo, String reserveDay);
	
	
	ObjectResponseWrapper<String> deleteReservation(String reservationNo);





	
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
