package com.sbackjung.transferstay;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@Slf4j
public class TransferStayApplication {

  public static void main(String[] args) {
    // 어플리케이션 구동 전 환경변수 로드
    Dotenv dotenv = Dotenv.configure().directory("./.env").load();
    dotenv.entries().forEach(entry ->
        System.setProperty(entry.getKey(), entry.getValue()));

    dotenv.entries().forEach(entry ->
            log.info(entry.getKey()+" : "+entry.getValue()));
    log.info("RDS : {}",System.getenv("DATABASE_URL"));
    SpringApplication.run(TransferStayApplication.class, args);
  }
}
