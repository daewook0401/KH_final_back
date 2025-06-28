package com.nomnom.onnomnom.member.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class CheckInfoDTO {

    @Pattern(
        regexp = "^(?=.*[a-z])[a-z0-9]{4,20}$",
        message = "아이디는 소문자 영문과 숫자를 포함하여 4~20자 이내여야 합니다. 숫자만으로는 구성할 수 없습니다."
    )
    private String memberId;

    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String memberEmail;

    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9._]{2,20}$",
        message = "닉네임은 2~20자 이내의 한글, 영문, 숫자, '_', '.'만 사용할 수 있습니다."
    )
    private String memberNickName;
}
