package com.nomnom.onnomnom.auth.model.service;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.auth.model.dao.EmailMapper;
import com.nomnom.onnomnom.auth.model.dto.VerifyCodeDTO;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BaseException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
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
            <div style="width:100%; background:#fff8f0; padding:20px; font-family:'Segoe UI', Arial, sans-serif;">
            	<div style="max-width:600px; margin:0 auto; background:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 4px 8px rgba(0,0,0,0.1);">
            	<div style="background:#f28c38; color:#ffffff; padding:20px; text-align:center;">
            		<h1 style="margin:0; font-size:28px;">맛집찾기 이메일 인증</h1>
            	</div>
            	<div style="padding:30px; color:#333;">
            		<p style="font-size:16px; margin:0 0 15px;">안녕하세요, 회원님!</p>
            		<p style="font-size:16px; line-height:1.5;">맛있는 여정을 시작하기 위해 아래 인증 코드를 입력하여 이메일 인증을 완료해주세요.</p>
            		<div style="text-align:center; margin:25px 0;">
            		    <span style="display:inline-block; font-size:26px; font-weight:bold; color:#f28c38; 
                            padding:12px 24px; border:2px dashed #f28c38; border-radius:6px; background:#fff3e6;">
            		        """ + verifyCode + """
            		    </span>
            		</div>
            		  	  <p style="font-size:16px; margin:15px 0;">인증 코드는 <strong>3분</strong> 동안 유효합니다.</p>
            		  	  <p style="font-size:16px; margin:0;">맛있는 경험을 함께 만들어가요!</p>
            		  	  <p style="font-size:16px; margin:15px 0 0;">nomnom 팀 드림</p>
            		  </div>
            		  <div style="background:#f28c38; color:#ffffff; text-align:center; padding:10px; font-size:14px;">
            		  	  <p style="margin:0;">맛집찾기 | 최고의 맛집을 찾아드립니다!</p>
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
