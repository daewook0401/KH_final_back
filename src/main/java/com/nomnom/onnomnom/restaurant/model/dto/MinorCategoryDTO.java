package com.nomnom.onnomnom.restaurant.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinorCategoryDTO {
	private long minorCategoryId;
    private long majorCategoryId;
    private String minorCategoryName;
}
