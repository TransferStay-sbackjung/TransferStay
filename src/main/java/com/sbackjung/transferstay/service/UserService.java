package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.UserJoinForm;
import com.sbackjung.transferstay.dto.UserJoinResponse;
import com.sbackjung.transferstay.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserJoinResponse userJoin(UserJoinForm request) {
        // 이메일 중복 및 비밀번호 화인
        checkEmailAndPassword(request);
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        UserDomain user =  new UserDomain();
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setPhone(request.getPhoneNumber());
        UserDomain save = userRepository.save(user);
        return UserJoinResponse.builder()
                .userId(save.getUserId())
                .email(save.getEmail())
                .build();
    }

    public void checkEmailAndPassword(UserJoinForm form){
        if(userRepository.existsByEmail(form.getEmail())){
            throw new CustomException(
                    ErrorCode.EMAIL_EXITS, "해당 이메일의 사용자가 이미 존재합니다.");
        }
        if(!form.getPassword().equals(form.getPasswordCheck())){
            throw new CustomException(
                    ErrorCode.PASSWORD_NOT_MATCH, "비밀번호와 확인 비밀번호가 일치하지않습니다.");
        }
    }
}
