package com.nomnom.onnomnom.reservationSetting.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.reservationSetting.model.dto.AvailableTimeDTO;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingDTO;
import com.nomnom.onnomnom.reservationSetting.model.vo.AvailableTimeVo;
import com.nomnom.onnomnom.reservationSetting.model.vo.ReservationSettingVo;

@Mapper
public interface ReservationSettingMapper {

	int insertAvailableTime(AvailableTimeVo availableTimeVo);
	int insertSettingInfo(ReservationSettingVo reservationSettingVo);

	List<AvailableTimeDTO> selectAvailableTime(String restaurantNo);
	ReservationSettingDTO selectSettingInfo(String restaurantNo);
	
	int updateSettingInfo(ReservationSettingVo reservationSettingVo);
	int updateAvailableTime(AvailableTimeVo availableTimeVo);

	int deleteAvailableTime(String restaurantNo);
	int deleteSettingInfo(String restaurantNo);

}
