package com.nomnom.onnomnom.member.model.dto;

import java.util.Date;

import com.nomnom.onnomnom.member.model.entity.MemberEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class MemberDTO {
    private String memberNo;

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])[a-z0-9]{4,20}$",
        message = "아이디는 소문자 영문과 숫자를 포함하여 4~20자 이내여야 합니다. 숫자만으로는 구성할 수 없습니다."
    )
    private String memberId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @Pattern(
        regexp = "^(?=(.*[a-z]){1,})(?=(.*[A-Z]){1,})(?=(.*\\d){1,})(?=(.*[!@#$%^&*()_+=-]){1,}).{8,20}$",
        message = "비밀번호는 대소문자, 숫자, 특수문자 중 3가지 이상을 포함해야 합니다."
    )
    private String memberPw;
    
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String memberEmail;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String memberName;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9._]{2,20}$",
        message = "닉네임은 2~20자 이내의 한글, 영문, 숫자, '_', '.'만 사용할 수 있습니다."
    )
    private String memberNickName;
    private String memberPh;
    private Date memberEnrollDate;
    private Date memberModifiedDate;
    private String MemberRole;
    private String memberSelfie;
    private String isActive;
    private String isStoreOwner;

    public static MemberDTO fromEntity(MemberEntity e){
        return new MemberDTO(
            e.getMemberNo(),
            e.getMemberId(),
            e.getMemberPw(),
            e.getMemberEmail(),
            e.getMemberName(),
            e.getMemberNickName(),
            e.getMemberPh(),
            e.getMemberEnrollDate(),
            e.getMemberModifiedDate(),
            e.getMemberRole(),
            e.getMemberSelfie(),
            e.getIsActive(),
            e.getIsStoreOwner()
        );
    }
}
