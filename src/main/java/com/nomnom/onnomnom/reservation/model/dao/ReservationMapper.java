package com.nomnom.onnomnom.reservation.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.operating.model.vo.OperatingVo;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;
import com.nomnom.onnomnom.reservation.model.dto.ResponseTimeDTO;
import com.nomnom.onnomnom.reservation.model.vo.ReservationVo;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingDTO;

@Mapper
public interface ReservationMapper {

	int insertReservationInfo(ReservationVo reservationVo);
	
	ReservationSettingDTO selectReservationInfo(String restaurantNo);

	ResponseTimeDTO selectTimesInfo(OperatingVo operatingVo);

	ReservationDTO selectCheckAvailableTimes(ReservationVo reservationVo);

	List<ReservationDTO> selectReservationCheck(ReservationVo reservationVo);

	int deleteReservation(String reservationNo);



}
