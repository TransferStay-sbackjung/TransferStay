package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.config.exception.BadRequestException;
import com.sbackjung.transferstay.dto.SearchRequest;
import com.sbackjung.transferstay.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;

//    @PostMapping("/search")
//    public String search(@RequestPart SearchRequest.createReq req){
//
//        if(req.getFreeField() == null
//                && req.getCheckIn() == null
//                && req.getCheckOut() == null
//                && req.getPerson() == 0
//                && req.getLocation_depth1() == null
//                && req.getLocation_depth2()== null) {
//            return "null";
//        }
//
//        return req.toString();
//    }

    @GetMapping("/search")
    public String search(){
        return "hi";
    }

    @PostMapping("/search")
    public String searchRequest(@RequestParam(value = "freeField", required = false) String freeField,
                                @RequestParam(value = "checkIn", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                @RequestParam(value = "checkIn", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                @RequestParam(value = "person", required = false, defaultValue = "0") int person,
                                @RequestParam(value = "locationDepth1", required = false) String locationDepth1,
                                @RequestParam(value = "locationDepth2", required = false) String locationDepth2
                                ){

        // 모든 파라미터가 null 또는 기본값(예: person의 경우 0)인지 확인
        if (freeField == null && checkIn == null && checkOut == null && person == 0
                && locationDepth1 == null && locationDepth2 == null) {
            throw new BadRequestException("적어도 1개 이상의 필드를 선택해주세요.");
        }

        return freeField+checkIn+checkOut+person+locationDepth1+locationDepth2;

    }
}
