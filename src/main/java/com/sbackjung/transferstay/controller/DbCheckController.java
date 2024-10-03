package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.repository.UserRepository;
import com.sbackjung.transferstay.utils.UserIdHolder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DbCheckController {
    private final DataSource dataSource;
    private final UserRepository userRepository;

    @PostConstruct
    public void checkDbConnectionOnStartup() {
        checkDbConnection();
    }

    @GetMapping("/api/test")
    public String mainCheck(){
        return "프론트엔드 Test ";
    }

    @GetMapping("/check-db")
    public void checkDbConnection(){
        try{
            Connection con = dataSource.getConnection();
            log.info("Database connection is OK");
        } catch (SQLException e) {
            log.error("Database connection failed: {}",e.getMessage());
        }
    }
}
