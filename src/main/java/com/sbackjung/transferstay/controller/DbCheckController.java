package com.sbackjung.transferstay.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DbCheckController {
    private final DataSource dataSource;

    @PostConstruct
    public void checkDbConnectionOnStartup() {
        checkDbConnection();
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
