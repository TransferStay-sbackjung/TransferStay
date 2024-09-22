package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.*;
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

    @PatchMapping("/{auctionId}")
    public ResponseEntity<JsonResponse> updateAuction(
            @PathVariable Long auctionId,
            @RequestBody @Valid AuctionUpdateRequestDto request
    ){
        Long userId = getUserIdFromToken();
        AuctionUpdateResponseDto data = auctionService.updateAction(request,
                userId,auctionId);
        return ResponseEntity.ok(new JsonResponse(200,"경매 수정 완료.",data));
    }

    /*
        ?page=0&size=10&sort=startDate,desc
     */
    @GetMapping({"/{orderBy}","/"})
    public ResponseEntity<JsonResponse> getAuctionList(
            @PathVariable(value = "orderBy",required = false) String orderBy,
            Pageable pageable
    ){
        // todo : 현재 최고 경매가를 보여주고자한다면, 메소드를 추가해야할것같습니다.
        Page<AuctionGetListDto> auctionList =
                auctionService.getAuctionList(pageable,orderBy);
        return ResponseEntity.ok(new JsonResponse(200,"경매 리스트 불러오기 완료.",
                auctionList));
    }

    @GetMapping("/details/{auctionId}")
    public ResponseEntity<JsonResponse> getAuctionDetail(
            @PathVariable(value = "auctionId") Long auctionId
    ){
        // todo : 아마 경매에 참여한 사람들의 정보도 불러와야 할듯합니다.
        AuctionGetDetailDto data = auctionService.getAuctionDetails(auctionId);
        return ResponseEntity.ok(new JsonResponse(200,"경매 세부정보 불러오기 완료.",
                data));
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<JsonResponse> deleteAuction(
            @PathVariable(value = "auctionId") Long auctionId
    ){
        // todo : 아마 경매에 참여한 사람들의 상태도 봐야할듯합니다.
        Long userId = getUserIdFromToken();
        auctionService.deleteAuction(userId,auctionId);
        return ResponseEntity.ok(new JsonResponse(200,"경매 삭제 완료.",null));
    }
}
