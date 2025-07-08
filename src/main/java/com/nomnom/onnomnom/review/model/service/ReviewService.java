package com.nomnom.onnomnom.review.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPhotoDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewResponseDTO;

public interface ReviewService {
    // 리뷰 목록 조회
    ObjectResponseWrapper<ReviewResponseDTO> selectReview(String restaurantNo, int currentPage);

    // 리뷰 작성
    ObjectResponseWrapper<String> insertReview(ReviewDTO reviewDTO, List<MultipartFile> photos, MultipartFile billPhoto);

    // 리뷰 수정
    ObjectResponseWrapper<String> updateReview(ReviewDTO reviewDTO, List<MultipartFile> photos);

    // 리뷰 삭제
    ObjectResponseWrapper<String> deleteReview(String reviewNo);

    // 리뷰 사진 등록
    ObjectResponseWrapper<String> insertReviewPhoto(List<ReviewPhotoDTO> reviewPhotoDTO);

    // 리뷰 사진 삭제
    ObjectResponseWrapper<String> deleteReviewPhoto(String reviewNo);

    // 영수증 등록
    ObjectResponseWrapper<String> insertBill(String restaurantNo, MultipartFile billPhoto, String reviewNo, String memberNo);

    // 리뷰 삭제 시 영수증 삭제
    ObjectResponseWrapper<String> deleteBill(String reviewNo);

    ObjectResponseWrapper<ReviewResponseDTO> selectMyReview(int currentPage);
}
