package com.nomnom.onnomnom.reservation.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.operating.model.dto.OperatingDTO;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;
import com.nomnom.onnomnom.reservation.model.service.ReservationService;

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
	
	@GetMapping
	public ResponseEntity<ObjectResponseWrapper<String>> selectReservation(@RequestBody ReservationDTO reservationDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.selectReservation(reservationDTO));
	} 
	
	@PutMapping
	public ResponseEntity<ObjectResponseWrapper<String>> deleteReservation(@RequestBody ReservationDTO reservationDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.deleteReservation(reservationDTO));
	} 
	
	
	
	
	
	
	
	
	
	
	
}
