package com.sbackjung.transferstay;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TransferStayApplication {

    public static void main(String[] args) {
        // 어플리케이션 구동 전 환경변수 로드
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(),entry.getValue()));

        SpringApplication.run(TransferStayApplication.class, args);
    }

}
