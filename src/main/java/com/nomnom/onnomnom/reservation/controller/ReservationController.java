package com.nomnom.onnomnom.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;
import com.nomnom.onnomnom.reservation.model.dto.ResponseResultDTO;
import com.nomnom.onnomnom.reservation.model.service.ReservationService;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("api/reservation")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
	
	private final ReservationService reservationService;
	
	@PostMapping
	public ResponseEntity<ObjectResponseWrapper<String>> insertReservation(@RequestBody ReservationDTO reservationDTO) {
		log.info("reservationDTO : {}",reservationDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.insertReservation(reservationDTO));
	} 
	
	@GetMapping("/info")
	public ResponseEntity<ObjectResponseWrapper<ReservationSettingDTO>> selectReservationInfo(@RequestParam(name="restaurantNo") String restaurantNo) {
		log.info("restaurantNo : {}",restaurantNo);
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.selectReservationInfo(restaurantNo));
	}
	
	@GetMapping
	public ResponseEntity<ObjectResponseWrapper<ResponseResultDTO>> selectReservation(@RequestParam(name="restaurantNo") String restaurantNo,
																		   @RequestParam(name="reserveDay") String reserveDay) {

		return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.selectReservation(restaurantNo,reserveDay));
	} 
	
	@PutMapping
	public ResponseEntity<ObjectResponseWrapper<String>> deleteReservation(@RequestParam(name="restaurantNo") String reservationNo) {
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.deleteReservation(reservationNo));
	} 
	
	
	
	
	
	
	
	
	
	
	
}
