package com.nomnom.onnomnom.review.model.dao;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;

import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPhotoDTO;

@Mapper
public interface ReviewMapper {
  int selectReviewCount(String restaurantNo);
  
  List<ReviewDTO> selectReview(ReviewDTO reviewDTO);

  void insertReview(ReviewDTO reviewDTO);

  void updateReview(ReviewDTO reviewDTO);

  void deleteReview(String reviewNo);

  void insertReviewPhoto(List<ReviewPhotoDTO> reviewPhotoDTO);

  void deleteReviewPhoto(String reviewNo);
}
