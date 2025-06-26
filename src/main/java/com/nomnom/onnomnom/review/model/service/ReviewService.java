package com.nomnom.onnomnom.review.model.service;

import java.util.List;


import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPhotoDTO;

public interface ReviewService {
   // 리뷰 목록 조회
    ListResponseWrapper<ReviewDTO> selectReview(String restaurantNo);

    // 리뷰 작성
    void insertReview(ReviewDTO reviewDTO);

    // 리뷰 수정
    void updateReview(ReviewDTO reviewDTO);

    // 리뷰 삭제
    void deleteReview(String reviewNo);

    // 리뷰 사진 등록
    void insertReviewPhoto(List<ReviewPhotoDTO> reviewPhotoDTO);

    // 리뷰 사진 삭제
    void deleteReviewPhoto(String reviewNo);
}
