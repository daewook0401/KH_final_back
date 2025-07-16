package com.nomnom.onnomnom.restaurant.controller;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.service.RestaurantService;

import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@RestController
@RequestMapping("/api/stores/register")
@Validated
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    
    @PostMapping
    public ResponseEntity<Map<String, Map<String, String>>> insertRestaurant(
            @Valid @ModelAttribute RestaurantDTO restaurant,
            @RequestPart(name = "restaurantMainPhoto") MultipartFile restaurantMainPhoto
    ) {
        try {
            log.info("🔐 [Controller] 가게 등록 요청 수신");

            CustomUserDetails loginMember = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String memberNo = loginMember.getMemberNo();
            log.info("📦 [Controller] 레스토랑 서비스 호출 시작");

            restaurantService.registerRestaurant(restaurant, restaurantMainPhoto, memberNo);

            log.info("✅ [Controller] 레스토랑 서비스 호출 완료");

            Map<String, String> header = Map.of(
                "code",    "S100",
                "message", "가게 등록 성공"
            );
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("header", header));

        } catch (BaseException ex) {
            log.error("❌ [Controller] BaseException 발생 - {}", ex.getMessage());

            Map<String, String> header = Map.of(
                "code",    ex.getErrorCode().getCode(),
                "message", ex.getMessage()
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("header", header));

        } catch (Exception ex) {
            log.error("💥 [Controller] 알 수 없는 예외 발생", ex);

            Map<String, String> header = Map.of(
                "code",    ErrorCode.REQUEST_RESULT_FAILURE.getCode(),
                "message", ErrorCode.REQUEST_RESULT_FAILURE.getDefaultMessage()
            );
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("header", header));
        }
    }
    

    
    

}
