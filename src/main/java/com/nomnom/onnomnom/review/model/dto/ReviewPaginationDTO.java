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
public class ReviewPaginationDTO {

    private String restaurantNo;
    private int startRow;
    private int endRow;
    private List<ReviewDTO> reviews;
    private PageInfo pageInfo;


    public ReviewPaginationDTO(String restaurantNo, int startRow, int endRow) {
        this.restaurantNo = restaurantNo;
        this.startRow = startRow;
        this.endRow = endRow;
    }
}
