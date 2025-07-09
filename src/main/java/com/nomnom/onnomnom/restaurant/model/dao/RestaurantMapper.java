package com.nomnom.onnomnom.restaurant.model.dao;

import com.nomnom.onnomnom.restaurant.model.dto.AdminRestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDetailDTO;
import com.nomnom.onnomnom.restaurant.model.dto.SimpleRestaurantDTO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RestaurantMapper {

	// 신규 식당 등록
    int insertRestaurant(RestaurantDTO dto);

    // 메인 페이지용 목록 조회
    List<SimpleRestaurantDTO> findRestaurantsByMajorCategory(String categoryName);
    
    // 상세 페이지 조회
    RestaurantDetailDTO findRestaurantById(String restaurantId);

    // 관리자 페이지: 맛집 목록 동적 검색
    List<AdminRestaurantDTO> search(Map<String, Object> params);
    
    // 관리자 페이지: 특정 식당의 상태 변경
    int updateRestaurantStatus(@Param("restaurantId") String restaurantId, @Param("status") String status);

    // 사장님 권한 수정
    int updateMemberStoreOwner(@Param("restaurantId") String restaurantId, @Param("isStoreOwner") String isStoreOwner);

    // 사장님 권한 수정전 가계 추가로 있는지 확인
    int countActiveRestaurantsByMemberNo(@Param("restaurantId") String restaurantId);

    String findOwnerMemberNoByRestaurantNo(String restaurantNo);
}
