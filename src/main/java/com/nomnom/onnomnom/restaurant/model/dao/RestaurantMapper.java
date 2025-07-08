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

	// 신규 식당 등록 (구현은 XML로 이동)
    int insertRestaurant(RestaurantDTO dto);

    // 메인 페이지용 목록 조회 (구현은 XML로 이동)
    List<SimpleRestaurantDTO> findRestaurantsByMajorCategory(String categoryName);
    
    // 상세 페이지 조회 (구현은 XML로 이동)
    RestaurantDetailDTO findRestaurantById(String restaurantId);

    // 관리자 페이지: 맛집 목록 동적 검색 (구현은 XML로 이동)
    List<AdminRestaurantDTO> search(Map<String, Object> params);
    
    // 관리자 페이지: 특정 식당의 상태 변경 (구현은 XML로 이동)
    int updateRestaurantStatus(@Param("restaurantId") String restaurantId, @Param("status") String status);

}
