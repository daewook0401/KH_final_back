package com.nomnom.onnomnom.review.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.nomnom.onnomnom.review.model.dto.BillDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPhotoDTO;

@Mapper
public interface ReviewMapper {

  // 리뷰 개수 조회
  int selectReviewCount(String restaurantNo);

  // 리뷰 리스트 조회 (페이징용)
  List<ReviewDTO> selectReview(String restaurantNo, RowBounds rowBounds);

  // 리뷰 등록
  void insertReview(ReviewDTO reviewDTO);

  // 리뷰 수정
  void updateReview(ReviewDTO reviewDTO);

  // 리뷰 삭제
  void deleteReview(String reviewNo);

  // 리뷰 사진 조회
  List<ReviewPhotoDTO> selectReviewPhoto(String reviewNo);

  // 리뷰 사진 등록 (여러장)
  void insertReviewPhoto(List<ReviewPhotoDTO> reviewPhotoDTO);

  // 리뷰 사진 삭제
  void deleteReviewPhoto(String reviewNo);

  // 리뷰 작성자 ID 조회 (권한 체크용)
  String selectReviewWriterId(String reviewNo);

  void insertBill(BillDTO billDTO);

  void connectBill(String billNo, String reviewNo);

  List<BillDTO> selectAvailableBills(String memberNo, String restaurantNo);
  
  void deleteBill(String reviewNo);
}
