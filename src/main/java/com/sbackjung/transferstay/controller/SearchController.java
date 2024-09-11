package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.config.exception.BadRequestException;
import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.dto.SearchResponse;
import com.sbackjung.transferstay.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<JsonResponse> searchRequest(@RequestParam(value = "freeField", required = false) String freeField,
                                                      @RequestParam(value = "checkInDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkInDate,
                                                      @RequestParam(value = "checkOutDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOutDate,
                                                      @RequestParam(value = "personnel", required = false, defaultValue = "0") int personnel,
                                                      @RequestParam(value = "locationDepth1", required = false) String locationDepth1,
                                                      @RequestParam(value = "locationDepth2", required = false) String locationDepth2
    ){

        // 모든 파라미터가 null 또는 기본값(예: person의 경우 0)인지 확인
        if (freeField == null && checkInDate == null && checkOutDate == null && personnel == 0
                && locationDepth1 == null && locationDepth2 == null) {
            throw new BadRequestException("적어도 1개 이상의 필드를 선택해주세요.");
        }

        List<SearchResponse.getSearchResponse> results = searchService.getSearchResult(freeField, checkInDate, checkOutDate, personnel, locationDepth1, locationDepth2);

        return ResponseEntity.ok(new JsonResponse(200, "검색 완료", results));

    }
}
