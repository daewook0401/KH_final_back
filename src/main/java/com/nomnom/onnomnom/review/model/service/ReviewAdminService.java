package com.nomnom.onnomnom.review.model.service;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.review.model.dto.ReviewAdminResponseDTO;

public interface ReviewAdminService {

    // 관리자 - 리뷰 전체 조회 (페이지 번호로 페이징 처리)
    ObjectResponseWrapper<ReviewAdminResponseDTO> selectAllReviews(int currentPage);

    // 관리자 - 리뷰 활성화 상태 변경
    ObjectResponseWrapper<String> updateReviewActiveStatus(String reviewNo, String isActive);
}
