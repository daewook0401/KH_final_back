package com.nomnom.onnomnom.restaurant.model.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RestaurantDetailDTO {
    private String restaurantName;
    private String memberNo;
    private String restaurantAddress;
    private String restaurantDescription;
    private String restaurantMainPhoto;
    private String restaurantCuisineType; // 소분류 카테고리 이름 목록

}
