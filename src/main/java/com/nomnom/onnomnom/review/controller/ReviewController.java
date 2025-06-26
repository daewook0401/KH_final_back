package com.nomnom.onnomnom.review.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.review.model.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/restaurants")
public class ReviewController {
  private final ReviewService reviewService;

  @GetMapping("/{restaurantNo}/reviews")
public ResponseEntity<ListResponseWrapper<String>> getReviews(
        @PathVariable String restaurantNo,
        @RequestParam(name = "page", defaultValue = "1") int page
) {
    // ListResponseWrapper<String> result = reviewService.selectReview(restaurantNo, page);
    return ResponseEntity.ok(null);
}
 
    

}
