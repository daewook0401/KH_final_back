package com.nomnom.onnomnom.operatingHours.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.operatingHours.model.dto.OperatingHoursDTO;
import com.nomnom.onnomnom.operatingHours.model.service.OperatingHoursService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/operatinghours")
public class OperatingHoursController {

	private OperatingHoursService operatingHoursService;
	
	@PostMapping
	public ResponseEntity<ObjectResponseWrapper<String>> insertOperatingHours(@RequestBody List<OperatingHoursDTO> operatingHours) throws ParseException{
		return ResponseEntity.status(HttpStatus.CREATED).body(operatingHoursService.insertOperatingHours(operatingHours));
	}
	
}
