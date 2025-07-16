package com.nomnom.onnomnom.review.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAdminDTO {
    private String reviewNo;             // 리뷰 고유번호
    private String memberNo;             // 회원 고유번호
    private String memberId;           // 회원 아이디
    private String memberNickname;     // 회원 닉네임
    private String restaurantNo;         // 음식점 고유번호
    private String restaurantName;     // 음식점 이름
    private int reviewScore;           // 별점
    private String reviewContent;      // 리뷰 내용
    private Date createDate;           // 작성시간
    private String isActive;           // 리뷰 활성화 여부
}
