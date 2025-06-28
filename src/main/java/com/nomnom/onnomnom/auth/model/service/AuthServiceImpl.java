package com.nomnom.onnomnom.auth.model.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.auth.model.dao.AuthMapper;
import com.nomnom.onnomnom.auth.model.dto.LoginInfo;
import com.nomnom.onnomnom.auth.model.dto.LoginResponseDTO;
import com.nomnom.onnomnom.auth.model.dto.MemberLoginDTO;
import com.nomnom.onnomnom.auth.model.dto.VerifyCodeDTO;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.exception.CustomAuthenticationException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.member.model.service.MemberService;
import com.nomnom.onnomnom.token.model.service.TokenService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final ResponseWrapperService responseWrapperService;
    private final MemberService memberService;

    private final AuthMapper authMapper;

    @Override
    public ObjectResponseWrapper<LoginResponseDTO> tokens(MemberLoginDTO memberLoginInfo) {
        Authentication authentication = null;
        try{
            authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    memberLoginInfo.getMemberId(), 
                    memberLoginInfo.getMemberPw()));
        } catch(AuthenticationException e) {
            throw new CustomAuthenticationException(ErrorCode.ID_PASSWORD_MISMATCH);
        }
        CustomUserDetails loginMember = (CustomUserDetails)authentication.getPrincipal();

        LoginResponseDTO loginResponse = LoginResponseDTO
                .builder()
                .loginInfo(LoginInfo.builder()
                                    .memberNo(loginMember.getMemberNo())
                                    .username(loginMember.getUsername())
                                    .memberRole(loginMember.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER"))
                                    .isStoreOwner(loginMember.getIsStoreOwner())
                                    .build())
                .tokens(tokenService.generateToken(loginMember.getUsername(), loginMember.getMemberNo()))
                .build();
        
        return responseWrapperService.wrapperCreate("E100", "로그인 성공", loginResponse);
    }



    
}
