package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.config.exception.BadRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    public void search() {

        //String freeField, LocalDate checkIn, LocalDate checkOut, int person, String location_depth1, String location_depth2


        // 예외 처리
        log.info("[REJECT] 입력 값 오류");
        throw new BadRequestException("(예시) 체크인, 아웃 날짜가 잘못 설정되었습니다.");
    }
}
