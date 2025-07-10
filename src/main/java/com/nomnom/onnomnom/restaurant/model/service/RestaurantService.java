package com.nomnom.onnomnom.restaurant.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.restaurant.model.dto.AdminRestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RatingInfoDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDetailDTO;
import com.nomnom.onnomnom.restaurant.model.dto.SimpleRestaurantDTO;

public interface RestaurantService {
	/**
     * @return 생성된 restaurantNo
     */
    String registerRestaurant(RestaurantDTO dto,
                            MultipartFile mainPhoto,
                            String memberNo);
    
    List<SimpleRestaurantDTO> findRestaurantsByMajorCategory(String categoryName);
    
    RestaurantDetailDTO findRestaurantById(String restaurantId);
    RatingInfoDTO getRatingInfoByRestaurantId(String restaurantId);
    
    
    List<AdminRestaurantDTO> searchRestaurants(String status, String keyword);
    void updateRestaurantStatus(String restaurantId, String status);
    
    void someMethodToUpdateStatus(String restaurantId, String status);
}
