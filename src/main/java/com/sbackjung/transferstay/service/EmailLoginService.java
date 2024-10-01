package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailLoginService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(EmailLoginService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("로그인 시도 이메일: {}", email);

        // 유저 조회 및 존재하지 않을 시 예외 처리
        UserDomain user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지 않습니다: " + email));

        return new UserDetailsDto(user);
    }

    // 비밀번호 확인 메서드
    public boolean authenticate(String email, String rawPassword) {
        // 유저 조회
        UserDomain user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.BAD_REQUEST, "해당 유저는 존재하지 않습니다."));

        // 비밀번호 일치 여부 확인
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
