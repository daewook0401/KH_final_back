package com.nomnom.onnomnom.review.model.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.review.model.dao.ReviewMapper;
import com.nomnom.onnomnom.review.model.dto.ReviewDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewValidationService {

    private final ReviewMapper reviewMapper;

    private static final int MAX_IMAGE_COUNT = 8;
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp");

    // 로그인 여부 체크
    public void checkWritePermission() {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null || currentUserId.isBlank()) {
            throw new BaseException(ErrorCode.REVIEW_VALIDATION_FAIL, "로그인 후 작성이 가능합니다.");
        }
    }

    // 리뷰 번호 유효성 체크
    public void validateReviewNo(String reviewNo) {
        if (reviewNo == null || reviewNo.isBlank()) {
            throw new BaseException(ErrorCode.REVIEW_VALIDATION_FAIL, "리뷰 번호가 유효하지 않습니다.");
        }
    }

    // 리뷰 작성자 ID 가져오기 (없으면 예외)
    private String getReviewWriterIdOrThrow(String reviewNo) {
        validateReviewNo(reviewNo);
        String reviewWriterId = reviewMapper.selectReviewWriterId(reviewNo);
        if (reviewWriterId == null) {
            throw new BaseException(ErrorCode.REVIEW_NOT_FOUND, "리뷰를 찾을 수 없습니다.");
        }
        return reviewWriterId;
    }

    // 수정 권한 체크 (현재 로그인 유저가 작성자거나 관리자여야 함)
    public void checkUpdatePermission(String reviewNo) {
        String currentUserId = getCurrentUserId();
        boolean isAdmin = hasRole("ROLE_ADMIN");

        String reviewWriterId = getReviewWriterIdOrThrow(reviewNo);
        if (!isAdmin && !reviewWriterId.equals(currentUserId)) {
            throw new BaseException(ErrorCode.NO_UPDATE_PERMISSION, "수정 권한이 없습니다.");
        }
    }

    // 삭제 권한 체크 (현재 로그인 유저가 작성자거나 관리자여야 함)
    public void checkDeletePermission(String reviewNo) {
        String currentUserId = getCurrentUserId();
        boolean isAdmin = hasRole("ROLE_ADMIN");

        String reviewWriterId = getReviewWriterIdOrThrow(reviewNo);
        if (!isAdmin && !reviewWriterId.equals(currentUserId)) {
            throw new BaseException(ErrorCode.NO_DELETE_PERMISSION, "삭제 권한이 없습니다.");
        }
    }

    // 리뷰 작성 시 유효성 검사
    public void validateReviewInsert(ReviewDTO reviewDTO, List<MultipartFile> photos) {
        validateRating(reviewDTO);
        validatePhotos(photos);
    }

    // 리뷰 수정 시 유효성 검사
    public void validateReviewUpdate(ReviewDTO reviewDTO, List<MultipartFile> photos) {
        validateReviewNo(reviewDTO.getReviewNo());
        validateRating(reviewDTO);
        validatePhotos(photos);
    }

    // 별점 유효성 검사
    private void validateRating(ReviewDTO reviewDTO) {
        if (reviewDTO.getReviewScore() == null || reviewDTO.getReviewScore() < 0.5 || reviewDTO.getReviewScore() > 5.0) {
            throw new BaseException(ErrorCode.REVIEW_VALIDATION_FAIL, "별점은 0.5에서 5.0 사이여야 합니다.");
        }
    }

    // 사진 유효성 검사
    private void validatePhotos(List<MultipartFile> photos) {
        if (photos == null || photos.isEmpty()) return;

        if (photos.size() > MAX_IMAGE_COUNT) {
            throw new BaseException(ErrorCode.FILE_SIZE_EXCEEDED, "이미지는 최대 8개까지 업로드 가능합니다.");
        }

        for (MultipartFile photo : photos) {
            if (!photo.isEmpty() && !isValidExtension(photo.getOriginalFilename())) {
                throw new BaseException(ErrorCode.INVALID_FILE_FORMAT, "지원하지 않는 이미지 형식입니다.");
            }
        }
    }

    // 허용된 확장자 체크
    private boolean isValidExtension(String filename) {
        if (filename == null || !filename.contains(".")) return false;
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    // 현재 로그인한 사용자 ID 반환
    String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        return null;
    }

    // 현재 로그인한 사용자가 특정 권한(role)을 갖고 있는지 체크
    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return false;

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}
