package com.nomnom.onnomnom.auth.model.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VerifyCodeDTO {
    private String verifyCodeNo;
    private String email;
    private String verifyCode;
    private Date createDate;
}
