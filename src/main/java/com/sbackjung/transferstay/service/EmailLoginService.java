package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailLoginService {
    private final UserRepository userRepository;

}
