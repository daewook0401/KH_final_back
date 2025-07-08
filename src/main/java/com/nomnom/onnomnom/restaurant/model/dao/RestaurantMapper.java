package com.nomnom.onnomnom.restaurant.model.dao;

import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDetailDTO;
import com.nomnom.onnomnom.restaurant.model.dto.SimpleRestaurantDTO;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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
    
    @Select("""
            SELECT DISTINCT
                R.RESTAURANT_NO as restaurantNo,
                R.RESTAURANT_NAME as name,
                R.RESTAURANT_MAIN_PHOTO as imageUrl
            FROM TB_RESTAURANT R
            JOIN TB_RESTAURANT_CATEGORY_MAP MAP ON R.RESTAURANT_NO = MAP.RESTAURANT_NO
            JOIN TB_MINOR_CATEGORY MINOR ON MAP.MINOR_CATEGORY_ID = MINOR.MINOR_CATEGORY_ID
            JOIN TB_MAJOR_CATEGORY MAJOR ON MINOR.MAJOR_CATEGORY_ID = MAJOR.MAJOR_CATEGORY_ID
            WHERE MAJOR.MAJOR_CATEGORY_NAME = #{categoryName}
            AND R.IS_ACTIVE = 'Y'
            AND ROWNUM <= 100
            ORDER BY R.RESTAURANT_NO DESC
            """)
    List<SimpleRestaurantDTO> findRestaurantsByMajorCategory(String categoryName);
    
    @Select("""
            SELECT
                R.RESTAURANT_NAME,
                R.RESTAURANT_ADDRESS,
                R.RESTAURANT_DESCRIPTION,
                R.RESTAURANT_MAIN_PHOTO,
                (SELECT LISTAGG(MC.MINOR_CATEGORY_NAME, ',') WITHIN GROUP (ORDER BY MC.MINOR_CATEGORY_NAME)
                 FROM TB_RESTAURANT_CATEGORY_MAP MAP
                 JOIN TB_MINOR_CATEGORY MC ON MAP.MINOR_CATEGORY_ID = MC.MINOR_CATEGORY_ID
                 WHERE MAP.RESTAURANT_NO = R.RESTAURANT_NO) AS restaurantCuisineType
            FROM TB_RESTAURANT R
            WHERE R.RESTAURANT_NO = #{restaurantId}
            AND R.IS_ACTIVE = 'Y'
            """)
        RestaurantDetailDTO findRestaurantById(String restaurantId);
}
