package com.nomnom.onnomnom.operating.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.operating.model.dto.OperatingDTO;
import com.nomnom.onnomnom.operating.model.vo.OperatingVo;

@Mapper
public interface OperatingMapper {

	int insertOperatingHours(OperatingVo openingHoursVo);

	int insertBreaktime(OperatingVo breakTimeVo);

	List<OperatingDTO> selectOperatingInfo(String restaurantNo);

	int deleteOperatingInfo(String restaurantNo);
	int deleteBreaktimeInfo(String restaurantNo);

	int findOperatingHours(OperatingVo operatingVo);
	int findBreaktime(OperatingVo operatingVo);
	int updateOperatingHours(OperatingVo operatingVo);
	int updateBreaktime(OperatingVo breakTimeVo);

	String findOperatingNo(OperatingVo operatingVo);

	int selectCountByRestaurantNo(String restaurantNo);

}
