package com.nomnom.onnomnom.restaurant.model.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RestaurantCategoryMapMapper {

	@Insert("""
	        INSERT INTO TB_RESTAURANT_CATEGORY_MAP (RESTAURANT_NO, MINOR_CATEGORY_ID)
	        VALUES (#{restaurantNo}, #{minorCategoryId})
	        """)
	    void insertRestaurantCategoryMap(@Param("restaurantNo") String restaurantNo,
	                                     @Param("minorCategoryId") long minorCategoryId);
}
