package com.nomnom.onnomnom.auth.model.service;

import java.util.Date;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.auth.model.dao.EmailMapper;
import com.nomnom.onnomnom.auth.model.dto.VerifyCodeDTO;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.member.model.dto.MemberDTO;
import com.nomnom.onnomnom.member.model.service.MemberService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final MemberService memberService;
    private final JavaMailSender sender;
    private final ResponseWrapperService responseWrapperService;
    private final EmailMapper emailMapper;

    @Override
    public ObjectResponseWrapper<String> insertVerifyCode(String email){
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
        emailMapper.insertVerifyCode(verifyDTO);
        return responseWrapperService.wrapperCreate("S100", "인증코드 발송 성공");
    }

    private int verifyCodeCreate(){
        int verifyCode = (int)(Math.random() * (90000))+ 100000;
        return verifyCode;
    }

    @Override
    public ObjectResponseWrapper<String> selectCheckVerifyCode (VerifyCodeDTO verifyCodeDTO){
        VerifyCodeDTO verifyCode = emailMapper.selectVerifyCode(verifyCodeDTO.getEmail());
        if (verifyCode == null){
            throw new BaseException(ErrorCode.VERIFICATION_CODE_NOT_FOUND);
        }
        if (System.currentTimeMillis()>verifyCode.getCreateDate().getTime()+ (3 * 60 * 1000L)){
            throw new BaseException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        if (!(verifyCodeDTO.getVerifyCode().equals(verifyCode.getVerifyCode()))){
            throw new BaseException(ErrorCode.EMAIL_VERIFICATION_MISMATCH);
        }
        return responseWrapperService.wrapperCreate("S100", "이메일 인증 성공");
    }
}
