package com.nomnom.onnomnom.restaurant.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.restaurant.model.dto.RatingInfoDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDetailDTO;
import com.nomnom.onnomnom.restaurant.model.dto.SimpleRestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
@Slf4j
public class RestaurantSearchController {

	private final RestaurantService restaurantService;

    /** 카테고리별 식당 목록 조회 */
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Map<String, Object>> getRestaurantsByCategory(
            @PathVariable("categoryName") String categoryName) {

        List<SimpleRestaurantDTO> restaurantList =
                restaurantService.findRestaurantsByMajorCategory(categoryName);

        Map<String, Object> response = Map.of(
                "header", Map.of("code", "S200", "message", "정상적으로 조회되었습니다."),
                "body",   restaurantList
        );
        return ResponseEntity.ok(response);
    }

    /** 식당 상세 정보 조회 */
    @GetMapping("/{restaurantId}")
    public ResponseEntity<Map<String, Object>> getRestaurantDetail(
            @PathVariable("restaurantId") String restaurantId) {

        log.info("식당 상세 정보 조회 API 호출 - restaurantId: {}", restaurantId);

        RestaurantDetailDTO detail = restaurantService.findRestaurantById(restaurantId);

        Map<String, Object> response = Map.of(
                "header", Map.of("code", "S200", "message", "식당 상세 정보 조회 성공"),
                "body",   detail
        );
        return ResponseEntity.ok(response);
    }

    /** 식당 별점 정보 조회 */
    @GetMapping("/{restaurantId}/rating")
    public ResponseEntity<Map<String, Object>> getRestaurantRating(
            @PathVariable("restaurantId") String restaurantId) {

        log.info("식당 별점 정보 조회 API 호출 - restaurantId: {}", restaurantId);

        RatingInfoDTO ratingInfo = restaurantService.getRatingInfoByRestaurantId(restaurantId);

        Map<String, Object> response = Map.of(
                "header", Map.of("code", "S200", "message", "식당 별점 정보 조회 성공"),
                "body",   ratingInfo
        );
        return ResponseEntity.ok(response);
    }
}
