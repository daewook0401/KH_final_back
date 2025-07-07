package com.nomnom.onnomnom.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.auth.model.service.AuthService;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewResponseDTO;
import com.nomnom.onnomnom.review.model.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants/{restaurantNo}/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthService authService;

    // 리뷰 목록 조회
    @GetMapping
    public ResponseEntity<ObjectResponseWrapper<ReviewResponseDTO>> getReviews(
            @PathVariable String restaurantNo,
            @RequestParam(name = "page", defaultValue = "1") int page) {

        return ResponseEntity.ok(reviewService.selectReview(restaurantNo, page));
    }

    // 리뷰 작성
    @PostMapping
    public ResponseEntity<ObjectResponseWrapper<String>> insertReview(
            @PathVariable String restaurantNo,
            @RequestPart("review") ReviewDTO reviewDTO,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
            @RequestPart(value = "billPhoto", required = false) MultipartFile billPhoto) {

        reviewDTO.setRestaurantNo(restaurantNo);
        return ResponseEntity.ok(reviewService.insertReview(reviewDTO, photos, billPhoto));
    }

    // 리뷰 수정
    @PutMapping("/{reviewNo}")
    public ResponseEntity<ObjectResponseWrapper<String>> updateReview(
            @PathVariable String restaurantNo,
            @PathVariable String reviewNo,
            @RequestPart("review") ReviewDTO reviewDTO,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {
                
        reviewDTO.setRestaurantNo(restaurantNo);
        reviewDTO.setReviewNo(reviewNo);
        return ResponseEntity.ok(reviewService.updateReview(reviewDTO, photos));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<ObjectResponseWrapper<String>> deleteReview(
            @PathVariable(name = "restaurantNo") String restaurantNo,
            @PathVariable(name = "reviewNo") String reviewNo) {
        return ResponseEntity.ok(reviewService.deleteReview(reviewNo));
    }

    // 영수증 등록
    @PostMapping("/bill")
    public ResponseEntity<ObjectResponseWrapper<String>> insertBill(
            @PathVariable("restaurantNo") String restaurantNo,
            @RequestPart("billPhoto") MultipartFile billPhoto,
            @RequestParam String reviewNo) {

        // 로그인 정보에서 memberNo 얻기
        CustomUserDetails userDetails = authService.getUserDetails();
        String memberNo = userDetails.getMemberNo();

        return ResponseEntity.ok(reviewService.insertBill(restaurantNo, billPhoto, reviewNo, memberNo));
    }
}
