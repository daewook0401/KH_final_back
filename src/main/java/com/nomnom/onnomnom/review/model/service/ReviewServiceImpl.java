package com.nomnom.onnomnom.review.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.global.dto.PageInfo;
import com.nomnom.onnomnom.global.pagination.Pagination;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.review.model.dao.ReviewMapper;
import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPaginationDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPhotoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

  private final ReviewMapper reviewMapper;
  private final ResponseWrapperService responseWrapperService;
 
@Override
public ObjectResponseWrapper<ReviewPaginationDTO> selectReview(String restaurantNo, int currentPage) {

    int pageSize = 5;
    int boardNoPerPage = 5;

    int totalReviewCount = reviewMapper.selectReviewCount(restaurantNo);
    PageInfo pageInfo = Pagination.getPageInfo(currentPage, pageSize, boardNoPerPage, totalReviewCount);

    int startRow = (currentPage - 1) * pageSize + 1;
    int endRow = currentPage * pageSize;

    ReviewPaginationDTO paginationDTO = new ReviewPaginationDTO(restaurantNo, startRow, endRow);
    List<ReviewDTO> reviews = reviewMapper.selectReview(paginationDTO);

    paginationDTO.setReviews(reviews);
    paginationDTO.setPageInfo(pageInfo);

    return responseWrapperService.wrapperCreate("S101", "리뷰 조회 성공", paginationDTO);
}



  @Override
  public void insertReview(ReviewDTO reviewDTO) {

      reviewMapper.insertReview(reviewDTO);

      List<ReviewPhotoDTO> photos = reviewDTO.getPhotos();
      if (photos != null && !photos.isEmpty()) {
          for (ReviewPhotoDTO photo : photos) {
              photo.setReviewNo(reviewDTO.getReviewNo());
          }
          reviewMapper.insertReviewPhoto(photos);
      }
  }

  @Override
  public void updateReview(ReviewDTO reviewDTO) {
      reviewMapper.updateReview(reviewDTO);
      List<ReviewPhotoDTO> photos = reviewDTO.getPhotos();
      reviewMapper.deleteReviewPhoto(reviewDTO.getReviewNo());

      if (photos != null && !photos.isEmpty()) {
          for (ReviewPhotoDTO photo : photos) {
              photo.setReviewNo(reviewDTO.getReviewNo());
          }
          reviewMapper.insertReviewPhoto(photos);
      }
  }

  @Override
  public void deleteReview(String reviewNo) {
    reviewMapper.deleteReviewPhoto(reviewNo);
    reviewMapper.deleteReview(reviewNo);
  }

  @Override
  public void insertReviewPhoto(List<ReviewPhotoDTO> reviewPhotoDTO) {
    if (reviewPhotoDTO == null || reviewPhotoDTO.isEmpty()) {
        return;
    }

    reviewMapper.insertReviewPhoto(reviewPhotoDTO);
  }

  @Override
  public void deleteReviewPhoto(String reviewNo) {
    reviewMapper.deleteReviewPhoto(reviewNo);
  }
  
}
