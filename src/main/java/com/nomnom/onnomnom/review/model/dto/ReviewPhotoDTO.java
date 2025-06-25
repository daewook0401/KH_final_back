package com.nomnom.onnomnom.review.model.dto;

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
public class ReviewPhotoDTO {
  private String reviewPhotoNo;
  private String reviewNo;
  private String reviewPhotoUrl;
}
