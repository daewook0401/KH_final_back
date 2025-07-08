package com.nomnom.onnomnom.restaurant.model.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.restaurant.model.dao.CategoryMapper;
import com.nomnom.onnomnom.restaurant.model.dao.RestaurantCategoryMapMapper;
import com.nomnom.onnomnom.restaurant.model.dao.RestaurantMapper;
import com.nomnom.onnomnom.restaurant.model.dao.ReviewMapper2;
import com.nomnom.onnomnom.restaurant.model.dto.AdminRestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.dto.MinorCategoryDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RatingInfoDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDetailDTO;
import com.nomnom.onnomnom.restaurant.model.dto.SimpleRestaurantDTO;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

	private final ReviewMapper2 reviewMapper;
	
	private final RestaurantMapper restaurantMapper;
    // private final RestaurantTagMapper restaurantTagMapper; // 기존 매퍼는 삭제
    private final RestaurantCategoryMapMapper restaurantCategoryMapMapper; // 새 매퍼로 교체
    private final CategoryMapper categoryMapper; // 새 매퍼 추가
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Override
    @Transactional
    public String registerRestaurant(RestaurantDTO dto,
                                     MultipartFile mainPhoto,
                                     String memberNo) {
        log.info("🍽️ [1단계] 식당 등록 시작 - 회원번호: {}", memberNo);

        // 1) 사진 → S3 업로드 (기존과 동일)
        String key = "restaurants/" + UUID.randomUUID() + "_" + mainPhoto.getOriginalFilename();
        log.info("📸 [2단계] S3 업로드 시작 - 파일명: {}", key);
        try {
            PutObjectRequest por = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(mainPhoto.getContentType())
                .build();
            s3Client.putObject(por,
                RequestBody.fromInputStream(
                    mainPhoto.getInputStream(),
                    mainPhoto.getSize()
                ));
            log.info("✅ [2단계] S3 업로드 성공");
        } catch (IOException e) {
            log.error("❌ [2단계] S3 업로드 실패", e);
            throw new BaseException(ErrorCode.S3_SERVICE_FAILURE);
        }

        // 2) URL 생성 (기존과 동일)
        String photoUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
        log.info("🖼️ [3단계] 업로드 이미지 URL 생성 완료 - {}", photoUrl);

        // 3) DTO 세팅 후 TB_RESTAURANT insert (기존과 동일)
        dto.setPhotoUrl(photoUrl);
        dto.setMemberNo(memberNo);
        log.info("📝 [4단계] 식당 정보 DB 등록 시도 - {}", dto);
        int inserted = restaurantMapper.insertRestaurant(dto);
        if (inserted != 1) {
            log.error("❌ [4단계] 식당 정보 DB 등록 실패");
            throw new BaseException(ErrorCode.STORE_REGISTRATION_ERROR);
        }
        String restaurantNo = dto.getRestaurantNo();
        log.info("✅ [4단계] 식당 정보 DB 등록 완료 - 식당번호: {}", restaurantNo);


        // --- 4) 태그 등록 로직을 새로운 DB 구조에 맞게 전면 수정 ---
        log.info("🏷️ [5단계] 카테고리 정보 DB 등록 시작");
        List<String> categoryNames = dto.getRestaurantCuisineType(); // 예: ["한식", "치킨", "가성비"]

        if (categoryNames != null && !categoryNames.isEmpty()) {
            
            // 5-1. 대분류 ID 조회 (리스트의 첫 번째 값을 대분류 이름으로 간주)
            String majorCategoryName = categoryNames.get(0);
            Long majorCategoryId = categoryMapper.findMajorCategoryIdByName(majorCategoryName);

            if (majorCategoryId == null) {
                log.error("존재하지 않는 대분류입니다: {}", majorCategoryName);
                throw new BaseException(ErrorCode.STORE_REGISTRATION_ERROR);
            }

            // 5-2. 소분류 목록을 순회하며 'Find or Create' 후 식당과 연결
            // i=1 부터 시작하여 두 번째 요소부터 소분류로 처리
            for (int i = 1; i < categoryNames.size(); i++) {
                String minorCategoryName = categoryNames.get(i);
                Long minorCategoryId = categoryMapper.findMinorCategoryIdByName(minorCategoryName);

                if (minorCategoryId == null) {
                    // DB에 없는 소분류 -> 새로 생성
                    MinorCategoryDTO newMinorCategory = new MinorCategoryDTO();
                    newMinorCategory.setMajorCategoryId(majorCategoryId);
                    newMinorCategory.setMinorCategoryName(minorCategoryName);
                    
                    categoryMapper.insertMinorCategory(newMinorCategory);
                    minorCategoryId = newMinorCategory.getMinorCategoryId(); // 생성된 PK 가져오기
                }

                // 5-3. 식당과 소분류를 연결
                restaurantCategoryMapMapper.insertRestaurantCategoryMap(restaurantNo, minorCategoryId);
            }
             log.info("✅ [5단계] 카테고리 정보 DB 등록 완료");
        }

        log.info("🎉 [완료] 식당 등록 전체 완료 - 식당번호: {}", restaurantNo);
        return restaurantNo;
    }
    
    @Override
    public List<SimpleRestaurantDTO> findRestaurantsByMajorCategory(String categoryName) {
        return restaurantMapper.findRestaurantsByMajorCategory(categoryName);
    }
    
    @Override
    public RestaurantDetailDTO findRestaurantById(String restaurantId) {
        return restaurantMapper.findRestaurantById(restaurantId);
    }

    @Override
    public RatingInfoDTO getRatingInfoByRestaurantId(String restaurantId) {
        return reviewMapper.getRatingInfoByRestaurantId(restaurantId);
    }
    
    @Override
    public List<AdminRestaurantDTO> searchRestaurants(String status, String keyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("keyword", keyword);
        return restaurantMapper.search(params);
    }
    
    @Override
    @Transactional
    public void updateRestaurantStatus(String restaurantId, String status) {
        int result = restaurantMapper.updateRestaurantStatus(restaurantId, status);
        if (result == 0) {
            throw new BaseException(ErrorCode.RESTAURANT_NOT_FOUND);
        }
    }
}
