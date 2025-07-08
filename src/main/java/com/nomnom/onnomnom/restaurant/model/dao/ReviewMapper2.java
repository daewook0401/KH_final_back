package com.nomnom.onnomnom.restaurant.model.dao;

import com.nomnom.onnomnom.restaurant.model.dto.RatingInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReviewMapper2 {
    
	@Select("""
		    SELECT
		        NVL(AVG(REVIEW_SCORE), 0) as averageRating,
		        COUNT(*) as reviewCount
		    FROM TB_REVIEW
		    WHERE RESTAURANT_NO = #{restaurantId}
		    """)
		RatingInfoDTO getRatingInfoByRestaurantId(String restaurantId);
}
