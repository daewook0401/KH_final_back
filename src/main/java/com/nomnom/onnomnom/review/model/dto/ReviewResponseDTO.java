package com.nomnom.onnomnom.review.model.dto;

import java.util.List;

import com.nomnom.onnomnom.global.dto.PageInfo;

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
public class ReviewResponseDTO {
  private PageInfo pageInfo;
  private List<ReviewDTO> reviews;
}
