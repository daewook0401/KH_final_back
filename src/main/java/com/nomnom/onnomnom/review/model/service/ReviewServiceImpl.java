package com.nomnom.onnomnom.review.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPhotoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

  @Override
  public List<ReviewDTO> selectReview(String restaurantNo, String memberNo) {
    return null;
  }

  @Override
  public void insertReview(ReviewDTO reviewDTO) {
    
  }

  @Override
  public void updateReview(ReviewDTO reviewDTO) {

    
  }

  @Override
  public void deleteReview(String reviewNo) {
    
  }

  @Override
  public void insertReviewPhoto(List<ReviewPhotoDTO> reviewPhotoDTO) {
    
  }

  @Override
  public void deleteReviewPhoto(String reviewNo) {

    
  }
  
}
