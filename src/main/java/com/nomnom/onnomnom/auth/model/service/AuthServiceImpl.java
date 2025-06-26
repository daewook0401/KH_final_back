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
    private final JavaMailSender sender;
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

    @Override
    public ObjectResponseWrapper<String> selectCheckEmail(String email){
        if (memberService.selectMemberByEmail(email) != null){
            throw new BaseException(ErrorCode.DUPLICATE_MEMBER_EMAIL);
        }
        return sendVerifyCode(email);
    }

    private ObjectResponseWrapper<String> sendVerifyCode(String email){
        int verifyCode = verifyCodeCreate();
        MimeMessage message = sender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(email);
            helper.setSubject("뇸뇸 이메일 인증 번호입니다.");
            helper.setText("""
            <div style="width:100%; background:#f4f4f4; padding:20px; font-family:Arial,sans-serif;">
                <div style="max-width:600px; margin:0 auto; background:#fff; border-radius:8px; overflow:hidden;">
                    <div style="background:#4CAF50; color:#fff; padding:20px; text-align:center;">
                        <h1>Eco-Insight 이메일 인증</h1>
                    </div>
                    <div style="padding:20px; color:#333;">
                        <p>안녕하세요,</p>
                        <p>아래 인증 코드를 입력하여 이메일 인증을 완료해주세요.</p>
                        <div style="text-align:center; margin:20px 0;">
                            <span style="display:inline-block; font-size:24px; font-weight:bold; color:#4CAF50;
                                        padding:10px 20px; border:2px dashed #4CAF50; border-radius:4px;">
                                """ + verifyCode + """
                            </span>
                        </div>
                        <p>인증 코드는 <strong>3분</strong> 동안 유효합니다.</p>
                        <p>감사합니다.</p>
                    </div>
                </div>
            </div>
                    """, true);
            sender.send(message);
        } catch(MessagingException e){
            e.printStackTrace();
            throw new BaseException(ErrorCode.EMAIL_VERIFICATION_MISMATCH, "메일 발송 실패");
        }
        VerifyCodeDTO verifyDTO = VerifyCodeDTO.builder().email(email).verifyCode(String.valueOf(verifyCode)).build();
        authMapper.sendVerifyCode(verifyDTO);
        return responseWrapperService.wrapperCreate("E100", "인증코드 발송 성공");
    }

    private int verifyCodeCreate(){
        int verifyCode = (int)(Math.random() * (90000))+ 100000;
        return verifyCode;
    }
}
