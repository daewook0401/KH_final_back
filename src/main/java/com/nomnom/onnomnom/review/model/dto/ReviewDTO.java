package com.nomnom.onnomnom.review.model.dto;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {
  private String reviewNo;
    private String restaurantNo;
    private String memberNo;
    private String memberNickname;
    private String reviewContent;
    private int reviewScore;
    private Date createDate;
    private List<ReviewPhotoDTO> photos;
}
