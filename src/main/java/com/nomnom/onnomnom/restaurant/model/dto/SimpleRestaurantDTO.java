package com.nomnom.onnomnom.restaurant.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleRestaurantDTO {
	@JsonProperty("restaurant_no") // JSON으로 변환될 때 이 이름으로 변경됨
    private String restaurantNo;
    private String name;
    private String imageUrl;
}
