package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.AuctionGetListDto;
import com.sbackjung.transferstay.dto.AuctionPostRequestDto;
import com.sbackjung.transferstay.dto.AuctionPostResponseDto;
import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.service.AuctionService;
import com.sbackjung.transferstay.utils.UserIdHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import static com.sbackjung.transferstay.utils.UserIdHolder.getUserIdFromToken;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {
    private final AuctionService auctionService;

    @PostMapping("/")
    public ResponseEntity<JsonResponse> createAuction(
            @RequestBody @Valid AuctionPostRequestDto request
    ){
        Long userId = getUserIdFromToken();
        AuctionPostResponseDto data = auctionService.createAuction(request, userId);
        return ResponseEntity.ok(new JsonResponse(200,"경매 생성 완료.",data));
    }

    @GetMapping({"/{orderBy}","/"})
    public ResponseEntity<JsonResponse> getAuctionList(
            @PathVariable(value = "orderBy",required = false) String orderBy,
            Pageable pageable
    ){
        Page<AuctionGetListDto> auctionList =
                auctionService.getAuctionList(pageable,orderBy);
        return ResponseEntity.ok(new JsonResponse(200,"경매 리스트 불러오기 완료.",
                auctionList));
    }
}
