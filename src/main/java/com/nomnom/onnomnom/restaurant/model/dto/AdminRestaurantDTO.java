package com.nomnom.onnomnom.restaurant.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRestaurantDTO {
    private String restaurantNo;
    private String restaurantName;
    private String restaurantAddress;
    private String isActive;

}