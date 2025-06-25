package com.nomnom.onnomnom.review.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPhotoDTO;

@Mapper
public interface ReviewMapper {
  //리뷰 리스트 조회
  List<ReviewDTO> selectReview(@Param("restaurantNo") String restaurantNo,
                               @Param("memberNo") String memberNo);
  //리뷰 작성
  void insertReview(ReviewDTO reviewDTO);
  //리뷰 수정
  void updateReview(ReviewDTO reviewDTO);
  //리뷰 삭제
  void deleteReview(String reviewNo);
  //리뷰 사진 등록
  void insertReviewPhoto(List<ReviewPhotoDTO> reviewPhotoDTO);
 //리뷰 사진 삭제
  void deleteReviewPhoto(String reviewNo);
}
