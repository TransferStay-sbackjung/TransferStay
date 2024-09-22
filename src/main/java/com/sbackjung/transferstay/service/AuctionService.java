package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.domain.Auction;
import com.sbackjung.transferstay.dto.AuctionPostRequestDto;
import com.sbackjung.transferstay.dto.AuctionPostResponseDto;
import com.sbackjung.transferstay.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionPostResponseDto createAuction(AuctionPostRequestDto request, Long userId
    ) {
        Auction action = new Auction();
        LocalDateTime startTime = combineDateAndTime(request.getStartDate(),
                request.getStartTime());
        LocalDateTime deadline = combineDateAndTime(request.getDeadlineDate(),
                request.getDeadlineTime());
        action.setUserId(userId);
        action.setPostId(request.getPostId());
        action.setStartTime(startTime);
        action.setDeadline(deadline);
        action.setStartPrice(request.getStartPrice());
        if(startTime.isAfter(LocalDateTime.now())){
            action.setStatus(AuctionStatus.WAiTING);
        }else{
            action.setStatus(AuctionStatus.IN_PROGRESS);
        }
        return AuctionPostResponseDto.from(auctionRepository.save(action));
    }

    // 편의성을 위해서 String -> LocalDateTime로 변환해주는 함수
    private static LocalDateTime combineDateAndTime(String date,String time){
        return LocalDateTime.parse(date + "T" + time);
    }
}
