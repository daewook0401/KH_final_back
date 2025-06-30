package com.nomnom.onnomnom.review.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.dto.PageInfo;
import com.nomnom.onnomnom.global.pagination.Pagination;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.FileService;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.review.model.dao.ReviewMapper;
import com.nomnom.onnomnom.review.model.dto.ReviewDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewPhotoDTO;
import com.nomnom.onnomnom.review.model.dto.ReviewResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewValidationService reviewValidationService;
    private final FileService fileService;
    private final ResponseWrapperService responseWrapperService;

    @Override
    public ObjectResponseWrapper<ReviewResponseDTO> selectReview(String restaurantNo, int currentPage) {
        int pageSize = 5;
        int boardNoPerPage = 5;
        int totalReviewCount = reviewMapper.selectReviewCount(restaurantNo);

        PageInfo pageInfo = Pagination.getPageInfo(currentPage, pageSize, boardNoPerPage, totalReviewCount);

        int offset = (currentPage - 1) * pageSize;
        RowBounds rowBounds = new RowBounds(offset, pageSize);

        List<ReviewDTO> reviews = reviewMapper.selectReview(restaurantNo, rowBounds);
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO(pageInfo, reviews);

        return responseWrapperService.wrapperCreate("S101", "리뷰 조회 성공", reviewResponseDTO);
    }

    @Override
    @Transactional
    public ObjectResponseWrapper<String> insertReview(ReviewDTO reviewDTO, List<MultipartFile> photos) {
        reviewValidationService.checkWritePermission();
        reviewValidationService.validateReviewInsert(reviewDTO, photos);

        String currentUserId = reviewValidationService.getCurrentUserId();
        reviewDTO.setMemberNo(currentUserId);

        reviewMapper.insertReview(reviewDTO);
        uploadReviewPhotos(reviewDTO.getReviewNo(), photos);

        return responseWrapperService.wrapperCreate("S100", "리뷰 등록 성공", "success");
    }

    @Override
    @Transactional
    public ObjectResponseWrapper<String> updateReview(ReviewDTO reviewDTO, List<MultipartFile> photos) {
        reviewValidationService.checkUpdatePermission(reviewDTO.getReviewNo());
        reviewValidationService.validateReviewUpdate(reviewDTO, photos);

        reviewMapper.updateReview(reviewDTO);
        reviewMapper.deleteReviewPhoto(reviewDTO.getReviewNo());
        uploadReviewPhotos(reviewDTO.getReviewNo(), photos);

        return responseWrapperService.wrapperCreate("S102", "리뷰 수정 성공", "success");
    }

    @Override
    @Transactional
    public ObjectResponseWrapper<String> deleteReview(String reviewNo) {
        reviewValidationService.checkDeletePermission(reviewNo);

        reviewMapper.deleteReviewPhoto(reviewNo);
        reviewMapper.deleteReview(reviewNo);

        return responseWrapperService.wrapperCreate("S103", "리뷰 삭제 성공", "success");
    }

    @Override
    public ObjectResponseWrapper<String> insertReviewPhoto(List<ReviewPhotoDTO> reviewPhotoDTO) {
        if (reviewPhotoDTO != null && !reviewPhotoDTO.isEmpty()) {
            reviewMapper.insertReviewPhoto(reviewPhotoDTO);
        }
        return responseWrapperService.wrapperCreate("S102", "리뷰 이미지 등록 성공", "success");
    }

    @Override
    public ObjectResponseWrapper<String> deleteReviewPhoto(String reviewNo) {
        reviewMapper.deleteReviewPhoto(reviewNo);
        return responseWrapperService.wrapperCreate("S103", "리뷰 이미지 삭제 성공", "success");
    }

    private void uploadReviewPhotos(String reviewNo, List<MultipartFile> photos) {
        if (photos == null || photos.isEmpty()) return;

        List<ReviewPhotoDTO> photoDTOs = photos.stream()
                .filter(photo -> !photo.isEmpty())
                .map(photo -> {
                    String url = fileService.imageUpLoad(List.of(photo)).get(0);
                    ReviewPhotoDTO dto = new ReviewPhotoDTO();
                    dto.setReviewNo(reviewNo);
                    dto.setReviewPhotoUrl(url);
                    return dto;
                })
                .toList();

        if (!photoDTOs.isEmpty()) {
            reviewMapper.insertReviewPhoto(photoDTOs);
        }
    }
}
