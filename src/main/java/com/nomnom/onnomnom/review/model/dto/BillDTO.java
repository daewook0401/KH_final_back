package com.nomnom.onnomnom.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
  private String billNo;
  private String memberNo;
  private String restaurantNo;
  private String reviewNo;
  private String billPass;
  private String imageUrl;
}