package com.nomnom.onnomnom.reservationSetting.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.operating.model.dto.OperatingDTO;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingRequestDTO;
import com.nomnom.onnomnom.reservationSetting.model.service.ReservationSettingService;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/settings")
@Slf4j
@RequiredArgsConstructor
public class ReservationSettingController {

	private final ReservationSettingService reservationSettingService;
	
	@PostMapping
	public ResponseEntity<ObjectResponseWrapper<String>> insertSetting(@RequestBody ReservationSettingRequestDTO request){
		log.info("ReservationSettingRequestDTO : {}",request);
		return ResponseEntity.ok(reservationSettingService.insertSetting(request));
	}
	
	@GetMapping("/byMemberNo")
	public ResponseEntity<ObjectResponseWrapper<ReservationSettingRequestDTO>> selectSettingByMemberNo() {
		return ResponseEntity.ok(reservationSettingService.selectSettingByMemberNo());
	}
	
	@GetMapping("/byRestaurantNo")
	public ResponseEntity<ObjectResponseWrapper<ReservationSettingRequestDTO>> selectSettingByRestaurantNo(@RequestParam("restaurantNo") String restaurantNo) {
		return ResponseEntity.ok(reservationSettingService.selectSettingByRestaurantNo(restaurantNo));
	}
	
	@PutMapping
	public ResponseEntity<ObjectResponseWrapper<String>> updateSetting(@RequestBody ReservationSettingRequestDTO request) {
		log.info("ReservationSettingRequestDTO : {}",request);
		return ResponseEntity.ok(reservationSettingService.updateSetting(request));
	}
	
	@DeleteMapping
	public ResponseEntity<ObjectResponseWrapper<String>> deleteSetting(@RequestParam("restaurantNo") String restaurantNo){
		log.info("restaurantNo : {}",restaurantNo);
		return ResponseEntity.ok(reservationSettingService.deleteSetting(restaurantNo));
	}
	
	@GetMapping("/restaurant")
	public ResponseEntity<ObjectResponseWrapper<RestaurantDTO>> selectMyRestaurant() {
		log.info("잘 들어 오나아ㅛ???");
		return ResponseEntity.ok(reservationSettingService.selectMyRestaurant());
	}
}
