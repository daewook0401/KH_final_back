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
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthService authService;

    // 식당 리뷰 목록 조회
    @GetMapping("/restaurants/{restaurantNo}/reviews")
    public ResponseEntity<ObjectResponseWrapper<ReviewResponseDTO>> getReviews(
            @PathVariable String restaurantNo,
            @RequestParam(name = "page", defaultValue = "1") int page) {

        return ResponseEntity.ok(reviewService.selectReview(restaurantNo, page));
    }

    // 내 리뷰 목록 조회 (로그인한 사용자 기준)
    @GetMapping("/reviews/my")
    public ResponseEntity<ObjectResponseWrapper<ReviewResponseDTO>> getMyReviews(
            @RequestParam(name = "page", defaultValue = "1") int page) {

        return ResponseEntity.ok(reviewService.selectMyReview(page));
    }

    // 리뷰 작성
    @PostMapping("/reviews")
    public ResponseEntity<ObjectResponseWrapper<String>> insertReview(
            @RequestPart(value = "review") ReviewDTO reviewDTO,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
            @RequestPart(value = "billPhoto", required = false) MultipartFile billPhoto) {

        CustomUserDetails userDetails = authService.getUserDetails();
        reviewDTO.setMemberNo(userDetails.getMemberNo());

        return ResponseEntity.ok(reviewService.insertReview(reviewDTO, photos, billPhoto));
    }

    // 리뷰 수정
    @PutMapping("/reviews")
    public ResponseEntity<ObjectResponseWrapper<String>> updateReview(
            @RequestPart(value = "review") ReviewDTO reviewDTO,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {

        CustomUserDetails userDetails = authService.getUserDetails();
        reviewDTO.setMemberNo(userDetails.getMemberNo());

        return ResponseEntity.ok(reviewService.updateReview(reviewDTO, photos));
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews")
    public ResponseEntity<ObjectResponseWrapper<String>> deleteReview(
            @RequestParam String reviewNo) {

        CustomUserDetails userDetails = authService.getUserDetails();

        return ResponseEntity.ok(reviewService.deleteReview(reviewNo, userDetails.getMemberNo()));
    }

    // 영수증 등록
    @PostMapping("/restaurants/{restaurantNo}/reviews/bill")
    public ResponseEntity<ObjectResponseWrapper<String>> insertBill(
            @PathVariable String restaurantNo,
            @RequestPart(value = "billPhoto") MultipartFile billPhoto,
            @RequestParam String reviewNo) {

        CustomUserDetails userDetails = authService.getUserDetails();
        String memberNo = userDetails.getMemberNo();

        return ResponseEntity.ok(reviewService.insertBill(restaurantNo, billPhoto, reviewNo, memberNo));
    }

    
}
