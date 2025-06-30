package com.nomnom.onnomnom.auth.model.dto;

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
public class LoginInfo {
    private String memberNo;
    private String username;
    private String memberRole;
    private String isStoreOwner;
}