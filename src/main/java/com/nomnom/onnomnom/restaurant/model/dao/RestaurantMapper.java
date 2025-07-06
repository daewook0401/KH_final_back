package com.nomnom.onnomnom.restaurant.model.dao;

import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface RestaurantMapper {

    /* 신규 식당 등록 */
    @Options(useGeneratedKeys = true,         // PK 자동 세팅
             keyProperty    = "restaurantNo", // DTO 필드명
             keyColumn      = "RESTAURANT_NO" // DB 컬럼명
    )
    @Insert("""
        INSERT INTO TB_RESTAURANT (
            EUPMYEONDONG,
            MEMBER_NO,
            RESTAURANT_NAME,
            RESTAURANT_DESCRIPTION,
            RESTAURANT_ADDRESS,
            RESTAURANT_POST_CODE,
            RESTAURANT_PHONE_NUMBER,
            RESTAURANT_MAIN_PHOTO
        ) VALUES (
            #{restaurantDongCode},
            #{memberNo},
            #{restaurantName},
            #{restaurantDescription},
            #{restaurantAddress},
            #{restaurantPostalCode},
            #{restaurantPhoneNumber},
            #{photoUrl}
        )
        """)
    int insertRestaurant(RestaurantDTO dto);
}
