package com.nomnom.onnomnom.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.review.model.dto.ReviewAdminResponseDTO;
import com.nomnom.onnomnom.review.model.service.ReviewAdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class ReviewAdminController {
  private final ReviewAdminService reviewAdminService;

    @GetMapping("/reviews")
    public ResponseEntity<ObjectResponseWrapper<ReviewAdminResponseDTO>> getAllReviews(
            @RequestParam(name = "page", defaultValue = "1") int page) {

        return ResponseEntity.ok(reviewAdminService.selectAllReviews(page));
    }
    @PatchMapping("/reviews")
    public ResponseEntity<ObjectResponseWrapper<String>> updateReviewActiveStatus(
            @RequestParam String reviewNo,
            @RequestParam String isActive) {

        return ResponseEntity.ok(reviewAdminService.updateReviewActiveStatus(reviewNo, isActive));
    }

}
