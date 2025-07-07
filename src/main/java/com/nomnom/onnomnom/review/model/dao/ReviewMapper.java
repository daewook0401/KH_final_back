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

    // 리뷰 리스트 조회 (페이징)
    List<ReviewDTO> selectReview(String restaurantNo, RowBounds rowBounds);

    // 리뷰 등록
    void insertReview(ReviewDTO reviewDTO);

    // 리뷰 수정
    void updateReview(ReviewDTO reviewDTO);

    // 리뷰 삭제
    void deleteReview(String reviewNo);

    // 리뷰 사진 조회
    List<ReviewPhotoDTO> selectReviewPhoto(String reviewNo);

    // 리뷰 사진 등록 (여러 장)
    void insertReviewPhoto(List<ReviewPhotoDTO> reviewPhotoDTO);

    // 리뷰 사진 삭제
    void deleteReviewPhoto(String reviewNo);

    // 리뷰 작성자 ID 조회 (권한 체크용)
    String selectReviewWriterId(String reviewNo);

    // 영수증 등록
    void insertBill(BillDTO billDTO);

    // 리뷰 삭제 시 영수증 삭제
    void deleteBill(String reviewNo);

    //  리뷰 사진 URL 조회 (S3 삭제용)
    List<String> selectReviewPhotoUrls(String reviewNo);

    //  영수증 이미지 URL 조회 (S3 삭제용)
    String selectBillPhotoUrl(String reviewNo);

    void deleteReviewPhotosByUrls(List<String> photoUrls);
}
