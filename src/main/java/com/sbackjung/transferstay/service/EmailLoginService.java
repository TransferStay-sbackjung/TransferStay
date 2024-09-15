package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailLoginService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(email);
        UserDomain user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.BAD_REQUEST, "해당 유저는 존재하지않습니다."));

        return new UserDetailsDto(user);
    }

    // 로그인할 때 비밀번호를 직접 확인하는 메서드 (선택 사항)
    public boolean authenticate(String email, String rawPassword) {
        UserDomain user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.BAD_REQUEST, "해당 유저는 존재하지않습니다."));

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
