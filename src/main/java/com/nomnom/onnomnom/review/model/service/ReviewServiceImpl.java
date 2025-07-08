package com.nomnom.onnomnom.review.model.service;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.auth.model.service.AuthService;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.dto.PageInfo;
import com.nomnom.onnomnom.global.pagination.Pagination;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.global.service.S3Service;
import com.nomnom.onnomnom.review.model.dao.ReviewMapper;
import com.nomnom.onnomnom.review.model.dto.BillDTO;
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
    private final ResponseWrapperService responseWrapperService;
    private final S3Service s3Service;
    private final AuthService authService;

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
    public ObjectResponseWrapper<ReviewResponseDTO> selectMyReview(int currentPage) {
        CustomUserDetails userDetails = authService.getUserDetails();
        String memberNo = userDetails.getMemberNo();

        int pageSize = 5;
        int boardNoPerPage = 5;
        int totalReviewCount = reviewMapper.selectMyReviewCount(memberNo);

        PageInfo pageInfo = Pagination.getPageInfo(currentPage, pageSize, boardNoPerPage, totalReviewCount);
        int offset = (currentPage - 1) * pageSize;
        RowBounds rowBounds = new RowBounds(offset, pageSize);

        List<ReviewDTO> reviews = reviewMapper.selectMyReview(memberNo, rowBounds);
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO(pageInfo, reviews);

        return responseWrapperService.wrapperCreate("S101", "내 리뷰 조회 성공", reviewResponseDTO);
    }

    @Override
    @Transactional
    public ObjectResponseWrapper<String> insertReview(ReviewDTO reviewDTO, List<MultipartFile> photos, MultipartFile billPhoto) {
        reviewValidationService.checkWritePermission();
        reviewValidationService.validateReviewInsert(reviewDTO, photos);
        reviewValidationService.validateBillPhoto(billPhoto);

        CustomUserDetails userDetails = authService.getUserDetails();
        reviewDTO.setMemberNo(userDetails.getMemberNo());

        reviewMapper.insertReview(reviewDTO);

        uploadReviewPhotos(reviewDTO.getReviewNo(), photos);

        insertBill(reviewDTO.getRestaurantNo(), billPhoto, reviewDTO.getReviewNo(), userDetails.getMemberNo());

        return responseWrapperService.wrapperCreate("S100", "리뷰 및 영수증 등록 성공", "success");
    }

    @Override
    @Transactional
    public ObjectResponseWrapper<String> updateReview(ReviewDTO reviewDTO, List<MultipartFile> photos) {
        reviewValidationService.checkUpdatePermission(reviewDTO.getReviewNo());
        reviewValidationService.validateReviewUpdate(reviewDTO, photos);

        reviewMapper.updateReview(reviewDTO);

        List<String> existingPhotoUrls = reviewMapper.selectReviewPhotoUrls(reviewDTO.getReviewNo());
        for (String url : existingPhotoUrls) {
            s3Service.deleteFile(url);
        }
        reviewMapper.deleteReviewPhoto(reviewDTO.getReviewNo());

        uploadReviewPhotos(reviewDTO.getReviewNo(), photos);

        return responseWrapperService.wrapperCreate("S102", "리뷰 수정 성공", "success");
    }

    @Override
    @Transactional
    public ObjectResponseWrapper<String> deleteReview(String reviewNo) {
        reviewValidationService.checkDeletePermission(reviewNo);

        String billPhotoUrl = reviewMapper.selectBillPhotoUrl(reviewNo);
        if (billPhotoUrl != null) {
            s3Service.deleteFile(billPhotoUrl);
        }
        reviewMapper.deleteBill(reviewNo);

        List<String> photoUrls = reviewMapper.selectReviewPhotoUrls(reviewNo);
        for (String url : photoUrls) {
            s3Service.deleteFile(url);
        }
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
    @Transactional
    public ObjectResponseWrapper<String> deleteReviewPhoto(String reviewNo) {
        List<String> photoUrls = reviewMapper.selectReviewPhotoUrls(reviewNo);
        for (String url : photoUrls) {
            s3Service.deleteFile(url);
        }
        reviewMapper.deleteReviewPhoto(reviewNo);
        return responseWrapperService.wrapperCreate("S103", "리뷰 이미지 삭제 성공", "success");
    }

    @Override
    @Transactional
    public ObjectResponseWrapper<String> insertBill(String restaurantNo, MultipartFile billPhoto, String reviewNo, String memberNo) {
        String url = s3Service.upLoad(billPhoto);

        BillDTO billDTO = new BillDTO();
        billDTO.setReviewNo(reviewNo);
        billDTO.setRestaurantNo(restaurantNo);
        billDTO.setMemberNo(memberNo);
        billDTO.setImageUrl(url);
        billDTO.setBillPass("Y");

        reviewMapper.insertBill(billDTO);
        return responseWrapperService.wrapperCreate("S100", "영수증 등록 성공", "success");
    }

    @Override
    @Transactional
    public ObjectResponseWrapper<String> deleteBill(String reviewNo) {
        reviewValidationService.checkDeletePermission(reviewNo);

        String billPhotoUrl = reviewMapper.selectBillPhotoUrl(reviewNo);
        if (billPhotoUrl != null) {
            s3Service.deleteFile(billPhotoUrl);
        }

        reviewMapper.deleteBill(reviewNo);
        return responseWrapperService.wrapperCreate("S103", "영수증 삭제 성공", "success");
    }

    private void uploadReviewPhotos(String reviewNo, List<MultipartFile> photos) {
        if (photos == null || photos.isEmpty()) return;

        List<String> urls = photos.stream()
            .filter(photo -> !photo.isEmpty())
            .map(photo -> s3Service.upLoad(photo))
            .toList();

        List<ReviewPhotoDTO> photoDTOs = IntStream.range(0, urls.size())
            .mapToObj(i -> {
                ReviewPhotoDTO dto = new ReviewPhotoDTO();
                dto.setReviewNo(reviewNo);
                dto.setReviewPhotoUrl(urls.get(i));
                return dto;
            })
            .toList();

        reviewMapper.insertReviewPhoto(photoDTOs);
    }

   
}
