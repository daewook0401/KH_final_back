package com.nomnom.onnomnom.review.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nomnom.onnomnom.global.dto.PageInfo;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.pagination.Pagination;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.review.model.dao.ReviewMapper;
import com.nomnom.onnomnom.review.model.dto.ReviewAdminDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewAdminResponseDTO;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewAdminServiceImpl implements ReviewAdminService {

  private final ReviewMapper reviewMapper;
  private final ResponseWrapperService responseWrapperService;
  private final ReviewValidationService reviewValidationService;

  @Override
  public ObjectResponseWrapper<ReviewAdminResponseDTO> selectAllReviews(int currentPage) {
      int pageSize = 10;
      int boardNoPerPage = 5;

      int totalReviewCount = reviewMapper.selectReviewCount();

      PageInfo pageInfo = Pagination.getPageInfo(currentPage, pageSize, boardNoPerPage, totalReviewCount);
      int offset = (currentPage - 1) * pageSize;
      RowBounds rowBounds = new RowBounds(offset, pageSize);

      List<ReviewAdminDTO> reviews = reviewMapper.selectAllReviews(rowBounds);
      ReviewAdminResponseDTO responseDTO = new ReviewAdminResponseDTO(pageInfo, reviews);

      return responseWrapperService.wrapperCreate("S101", "관리자 리뷰 전체 조회 성공", responseDTO);
  }

  @Override
  @Transactional
  public ObjectResponseWrapper<String> updateReviewActiveStatus(String reviewNo, String isActive) {
      reviewValidationService.checkUpdatePermission(reviewNo);

      int result = reviewMapper.updateReviewActiveStatus(reviewNo, isActive);
      if (result > 0) {
          return responseWrapperService.wrapperCreate("S102", "리뷰 활성화 상태 변경 성공");
      } else {
          throw new BaseException(ErrorCode.REVIEW_NOT_FOUND, "리뷰를 찾을 수 없거나 상태 변경에 실패했습니다.");
      }
  }
}
        