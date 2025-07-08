package com.nomnom.onnomnom.restaurant.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.restaurant.model.dto.AdminRestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.dto.UpdateStatusRequestDTO;
import com.nomnom.onnomnom.restaurant.model.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/restaurants")
public class RestaurantAdminController {
	
	private final RestaurantService restaurantService;

    // 맛집 목록 조회 (필터링, 검색 포함)
	@GetMapping
	public ResponseEntity<Map<String, Object>> searchRestaurants(
	        @RequestParam(name = "status",  required = false) String status,
	        @RequestParam(name = "keyword", required = false) String keyword) {

	    List<AdminRestaurantDTO> list = restaurantService.searchRestaurants(status, keyword);
	    Map<String, Object> res = Map.of(
	            "header", Map.of("code", "S200", "message", "정상적으로 조회되었습니다."),
	            "body",   list
	    );
	    return ResponseEntity.ok(res);
	}

	@PatchMapping("/{restaurantId}/status")
	public ResponseEntity<Map<String, String>> updateRestaurantStatus(
	        @PathVariable("restaurantId") String restaurantId,
	        @RequestBody UpdateStatusRequestDTO req) {

	    restaurantService.updateRestaurantStatus(restaurantId, req.getStatus());
	    return ResponseEntity.ok(Map.of("message", "상태가 성공적으로 변경되었습니다."));
	}
}
