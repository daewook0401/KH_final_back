package com.nomnom.onnomnom.restaurant.model.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.nomnom.onnomnom.restaurant.model.dto.MinorCategoryDTO;

@Mapper
public interface CategoryMapper {

	 // 대분류 이름으로 대분류 ID 조회
    @Select("SELECT MAJOR_CATEGORY_ID FROM TB_MAJOR_CATEGORY WHERE MAJOR_CATEGORY_NAME = #{majorCategoryName}")
    Long findMajorCategoryIdByName(String majorCategoryName);

    // 소분류 이름으로 소분류 ID 조회
    @Select("SELECT MINOR_CATEGORY_ID FROM TB_MINOR_CATEGORY WHERE MINOR_CATEGORY_NAME = #{minorCategoryName}")
    Long findMinorCategoryIdByName(String minorCategoryName);

    // 새로운 소분류를 DB에 INSERT 하고, 생성된 PK를 DTO에 다시 세팅
    @Options(useGeneratedKeys = true, keyProperty = "minorCategoryId", keyColumn = "MINOR_CATEGORY_ID")
    @Insert("""
        INSERT INTO TB_MINOR_CATEGORY (MAJOR_CATEGORY_ID, MINOR_CATEGORY_NAME)
        VALUES (#{majorCategoryId}, #{minorCategoryName})
        """)
    int insertMinorCategory(MinorCategoryDTO minorCategory);
}
