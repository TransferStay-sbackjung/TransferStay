package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.dto.SearchResponse;
import com.sbackjung.transferstay.service.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/")
@Tag(name = "Search", description = "검색 API")
public class SearchController {

    private final SearchService searchService;

    @Tag(name = "Search", description = "검색 API")
    @GetMapping("/search")
    public ResponseEntity<JsonResponse> searchRequest(@RequestParam(value = "freeField", required = false) String freeField,
                                                      @RequestParam(value = "checkInDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkInDate,
                                                      @RequestParam(value = "checkOutDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOutDate,
                                                      @RequestParam(value = "personnel", required = false, defaultValue = "0") int personnel,
                                                      @RequestParam(value = "locationDepth1", required = false) String locationDepth1,
                                                      @RequestParam(value = "locationDepth2", required = false) String locationDepth2
    ){

        // Controller 로 넘기기 전 처리해야 할 예외 사항들

        // 1. 모든 파라미터가 null 또는 기본값(예: person의 경우 0)인지 확인
        if (freeField == null && checkInDate == null && checkOutDate == null && personnel == 0
                && locationDepth1 == null && locationDepth2 == null) {

            throw new CustomException(ErrorCode.BAD_REQUEST, "적어도 1개 이상의 필드를 선택해주세요.");
        }

        // 2. checkInDate >= checkOutDate 인지 확인
        if(checkInDate != null && checkOutDate != null){ // 비교를 위해선 두 값이 모두 not null 이여야 함.
            if(checkInDate.isAfter(checkOutDate)){
                throw new CustomException(ErrorCode.BAD_REQUEST, "체크인 / 체크아웃 날짜 범위가 잘못 설정되었습니다.");
            }
            if(checkInDate.isEqual(checkOutDate)){
                throw new CustomException(ErrorCode.BAD_REQUEST, "체크인 날짜와 체크아웃 날짜가 동일합니다.");
            }
        }

        // 3. locationDepth1 설정 없이 locationDepth2 값만 들어온 경우 -> locationDepth1 > locationDepth2 이기 때문에 선행 조건 필요
        if(locationDepth1 == null && locationDepth2 != null){
            throw new CustomException(ErrorCode.BAD_REQUEST, "지역 범위 설정이 잘못되었습니다.");
        }

        List<SearchResponse.getSearchResponse> results = searchService.getSearchResult(freeField, checkInDate, checkOutDate, personnel, locationDepth1, locationDepth2);

        if(results.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new JsonResponse(204, "검색 완료", "검색 결과가 존재하지 않습니다."));
        }

        return ResponseEntity.ok(new JsonResponse(200, "검색 완료", results));

    }
}
