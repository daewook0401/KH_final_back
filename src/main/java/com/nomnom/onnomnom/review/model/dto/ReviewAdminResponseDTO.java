package com.nomnom.onnomnom.review.model.dto;

import java.util.List;

import com.nomnom.onnomnom.global.dto.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAdminResponseDTO {
    private PageInfo pageInfo;            // 페이징 정보
    private List<ReviewAdminDTO> reviews; // 리뷰 리스트
}