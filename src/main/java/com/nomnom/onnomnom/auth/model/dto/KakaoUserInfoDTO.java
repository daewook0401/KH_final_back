package com.nomnom.onnomnom.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class KakaoUserInfoDTO {
	private Long id; // 카카오에서 제공하ㄴ는 사용자 아이디
	private String email;
	private String nickname;
}
