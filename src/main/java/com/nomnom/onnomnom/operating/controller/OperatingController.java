package com.nomnom.onnomnom.operating.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.operating.model.dto.OperatingDTO;
import com.nomnom.onnomnom.operating.model.service.OperatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/operatings")
public class OperatingController {

	private final OperatingService operatingService;
	
	@PostMapping
	public ResponseEntity<ObjectResponseWrapper<String>> insertOperatingHours(@RequestBody List<OperatingDTO> operatingHours) throws ParseException{
		log.info("operatingHours : {}",operatingHours);
		return ResponseEntity.ok(operatingService.insertOperating(operatingHours));
	}
	
	@GetMapping
	public ResponseEntity<ListResponseWrapper<OperatingDTO>> selectOperatingHours(@RequestParam(name="restaurantNo") String restaurantNo) {
		return ResponseEntity.status(HttpStatus.CREATED).body(operatingService.selectOperating(restaurantNo));
	}
	
	@GetMapping("/memberNo")
	public ResponseEntity<ListResponseWrapper<OperatingDTO>> selectOperatingByMemberNo() {
		return ResponseEntity.status(HttpStatus.CREATED).body(operatingService.selectOperatingByMemberNo());
	}
	
	@PutMapping
	public ResponseEntity<ObjectResponseWrapper<String>> updateOperatingHours(@RequestBody List<OperatingDTO> operatingHours) throws ParseException {
		return ResponseEntity.status(HttpStatus.CREATED).body(operatingService.updateOperating(operatingHours));
	}
	
	@DeleteMapping
	public ResponseEntity<ObjectResponseWrapper<String>> deleteOperatingHours(@RequestParam(name="restaurantNo") String restaurantNo) {
		return ResponseEntity.status(HttpStatus.CREATED).body(operatingService.deleteOperating(restaurantNo));
	}
	
}
