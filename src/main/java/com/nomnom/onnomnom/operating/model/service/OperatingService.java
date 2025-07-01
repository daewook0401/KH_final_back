package com.nomnom.onnomnom.operating.model.service;

import java.text.ParseException;
import java.util.List;

import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.operating.model.dto.OperatingDTO;

public interface OperatingService {

	ObjectResponseWrapper<String> insertOperating(List<OperatingDTO> operatingHours) throws ParseException;

	ListResponseWrapper<OperatingDTO> selectOperating(String restaurantNo);

	ObjectResponseWrapper<String> updateOperating(List<OperatingDTO> operatingHours) throws ParseException;
	
	ObjectResponseWrapper<String> deleteOperating(String restaurantNo);


}
