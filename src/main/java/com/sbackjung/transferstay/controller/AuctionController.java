package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.AuctionPostRequestDto;
import com.sbackjung.transferstay.dto.AuctionPostResponseDto;
import com.sbackjung.transferstay.service.AuctionService;
import com.sbackjung.transferstay.utils.UserIdHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sbackjung.transferstay.utils.UserIdHolder.getUserIdFromToken;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {
    private final AuctionService auctionService;

    @PostMapping("/")
    public AuctionPostResponseDto createAuction(
            @RequestBody @Valid AuctionPostRequestDto request
    ){
        Long userId = getUserIdFromToken();
        return auctionService.createAuction(request, userId);
    }
}
