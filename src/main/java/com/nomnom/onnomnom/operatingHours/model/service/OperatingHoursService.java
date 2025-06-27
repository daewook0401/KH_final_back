package com.nomnom.onnomnom.operatingHours.model.service;

import java.text.ParseException;
import java.util.List;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.operatingHours.model.dto.OperatingHoursDTO;

public interface OperatingHoursService {

	ObjectResponseWrapper<String> insertOperatingHours(List<OperatingHoursDTO> operatingHours) throws ParseException;

}
