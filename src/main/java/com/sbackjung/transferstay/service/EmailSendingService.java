package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.dto.AuthCodeResponse;
import com.sbackjung.transferstay.dto.CodeValidation;
import com.sbackjung.transferstay.dto.EmailAuthDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSendingService {
    private final JavaMailSender javaMailSender;

    private final Map<String, CodeValidation> authMap = new HashMap<>();
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Value("${spring.mail.username}")
    private  String senderEmail;

    public EmailAuthDto sendAuthCodeEmail(String email){
        try{
            String code = generateVerifyCode();
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());

            helper.setFrom(new InternetAddress(senderEmail, "TransferStay"));
            helper.setTo(email);
            helper.setSubject("TransferStay 이메일 인증요청");

            String htmlContent = buildEmailContent(code);
            helper.setText(htmlContent, true); // HTML 이메일을 사용하려면 true로 설정

            authMap.put(code, new CodeValidation(email, LocalDateTime.now()));

            javaMailSender.send(mimeMessage);

            return EmailAuthDto.builder()
                    .to(email)
                    .code(code)
                    .build();
        }catch(MessagingException e){
            log.error("MessagingException occur : {}",e.getMessage());
            throw new CustomException(ErrorCode.INTER_SERVER_ERROR,"이메일 전송에 " +
                    "실패하였습니다.");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException occur : {}",e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST,"인코딩 " +
                    "방식을 확인해주세요.");
        }
    }

    public AuthCodeResponse checkAuthCode(String email, String authCode) {
        CodeValidation validation = authMap.get(authCode);
        if(validation == null){
            throw new CustomException(ErrorCode.BAD_REQUEST,"이메일 전송에 " +
                    "해당 인증번호는 존재하지 않습니다.");
        }
        if(!validation.getAuthorEmail().equals(email)){
            throw new CustomException(ErrorCode.BAD_REQUEST,"이메일 전송에 " +
                    "옳바르지않은 인증번호입니다.");
        }
        if(validation.getCreateAt().isBefore(LocalDateTime.now().minusMinutes(5))){
            throw new CustomException(ErrorCode.BAD_REQUEST,"이메일 전송에 " +
                    "인증시간이 만료되었습니다.");
        }
        // 인증만료시 해당 데이터 삭제
        // todo : 추후에 일정 스케쥴마다 데이터를 삭제하는 로직이 필요할수도
        authMap.remove(authCode);
        return AuthCodeResponse.builder()
                .email(email)
                .isAuth(true)
                .build();
    }

    // 인증 코드를 포함한 HTML 이메일 내용 생성
    // HTML 코드가 너무 복잡하면 스타일이 적용 안되는 경우가있음
    private String buildEmailContent(String code) {
        return "<html>" +
                "<h2>TransferStay 이메일 인증요청</h2>" +
                "<p>안녕하세요,</p>" +
                "<p>아래의 인증 코드를 사용하여 이메일 인증을 완료해 주세요.</p>" +
                "<p>인증 제한 시간은 5분입니다..</p>" +
                "<h3 style='color: #d9534f; background-color: #f0f0f0; padding: 15px; border-radius: 5px; display: inline-block; font-size: 32px;'>" + code + "</h3>" +  // 인증 코드 크기를 32px로 크게 설정
                "<p>감사합니다.</p>" +
                "</body>" +
                "</html>";
    }

    // 6글자의 랜덤 문자열 생성
    public String generateVerifyCode(){
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }
        return code.toString();
    }


}
