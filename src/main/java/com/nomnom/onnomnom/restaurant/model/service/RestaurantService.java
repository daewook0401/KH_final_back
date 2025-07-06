package com.nomnom.onnomnom.restaurant.model.service;

import org.springframework.web.multipart.MultipartFile;

import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;

public interface RestaurantService {
	/**
     * @return 생성된 restaurantNo
     */
    String registerRestaurant(RestaurantDTO dto,
                            MultipartFile mainPhoto,
                            String memberNo);
}
