package com.nomnom.onnomnom.review.controller;



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
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPaginationDTO;
import com.nomnom.onnomnom.review.model.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/restaurants/{restaurantNo}/reviews")
public class ReviewController {
  private final ReviewService reviewService;
  private final ResponseWrapperService responseWrapperService;

@GetMapping
public ResponseEntity<ObjectResponseWrapper<ReviewPaginationDTO>> getReviews(
        @PathVariable String restaurantNo,
        @RequestParam(name = "page", defaultValue = "1") int page
) {
    return ResponseEntity.ok(reviewService.selectReview(restaurantNo, page));
}
 
@PostMapping
public ResponseEntity<ObjectResponseWrapper<String>> insertReview(
        @PathVariable String restaurantNo,
        @RequestBody ReviewDTO reviewDTO
) {
    reviewDTO.setRestaurantNo(restaurantNo);
    reviewService.insertReview(reviewDTO);
    return ResponseEntity.ok(responseWrapperService.wrapperCreate("S100", "리뷰 등록 성공", "success"));
}    

@PutMapping("/{reviewNo}")
public ResponseEntity<ObjectResponseWrapper<String>> updateReview(
        @PathVariable String restaurantNo,
        @PathVariable String reviewNo,
        @RequestBody ReviewDTO reviewDTO
) {
    reviewDTO.setRestaurantNo(restaurantNo);
    reviewDTO.setReviewNo(reviewNo);
    
    reviewService.updateReview(reviewDTO);
    return ResponseEntity.ok(responseWrapperService.wrapperCreate("S102","리뷰 수정 성공","success"));
  }
@DeleteMapping("/{reviewNo}")
public ResponseEntity<ObjectResponseWrapper<String>> deleteReview(
        @PathVariable String restaurantNo,
        @PathVariable String reviewNo) {

    reviewService.deleteReview(reviewNo);
    return ResponseEntity.ok(responseWrapperService.wrapperCreate("S103", "리뷰 삭제 성공", "success"));
}
}
