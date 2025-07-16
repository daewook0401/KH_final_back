package com.nomnom.onnomnom.restaurant.model.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RestaurantDTO {

	// MyBatis @SelectKey 로 채워질 PK
    private String restaurantNo;

    @NotBlank(message = "가게 이름은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9 ]{2,16}$",
             message = "가게 이름은 2~16자 이내의 한글, 영문, 숫자만 사용 가능합니다.")
    private String restaurantName;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String restaurantAddress;

    @NotBlank(message = "읍면동 코드는 필수 입력값입니다.")
    private String restaurantDongCode;   // <-- React 의 emdCode → 이 필드와 매핑

    @NotBlank(message = "우편번호는 필수 입력값입니다.")
    private String restaurantPostalCode;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String restaurantPhoneNumber;

    @NotBlank(message = "가게 설명은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9 ]{2,96}$",
    		message = "가게 설명은 2~96자 이내의 한글, 영문, 숫자만 사용 가능합니다.")
    private String restaurantDescription;

    @NotNull(message = "태그 리스트는 null일 수 없습니다.")
    @Size(min = 2, max = 6, message = "대분류 포함 2~6개여야 합니다.")
    private List<String> restaurantCuisineType;

    // S3 업로드 후 URL 담을 필드
    private String photoUrl;

    // 로그인한 사용자 PK (컨트롤러에서 세팅)
    private String memberNo;
    
}
