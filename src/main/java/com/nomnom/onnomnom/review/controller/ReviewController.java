package com.nomnom.onnomnom.review.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nomnom.onnomnom.review.model.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReviewController {
  private final ReviewService reviewService;
}
