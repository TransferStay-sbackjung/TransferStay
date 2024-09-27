package com.sbackjung.transferstay.scheduler;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.repository.AuctionRepository;
import com.sbackjung.transferstay.service.AuctionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;

    // cron = "초 분 시 일 월 요일" 매분 0초마다 실행
    // 경매물의 상태를 Waiting -> progress, progress -> bid fail로 변경, Success는 경매 성공
    // 로직에서 변경할것이기 때문에 없음
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updateAuctionStatusBidFail(){
        // 경매 성공시 BiD Success 상태로 변경할것이기 때문에, 굳이 바꿔주지 않아도됨
        log.info("Auction Status Update By Trigger at {}",LocalDateTime.now());
        int i = auctionRepository.updateAuctionStatus(
                AuctionStatus.IN_PROGRESS,
                AuctionStatus.BID_FAIL,
                LocalDateTime.now(),
                AuctionStatus.WAITING
        );

        log.info("{} rows updated.",i);
        if(i > 0){
            auctionService.test();
        }else if(i == 0){
            log.info("any rows updated.");
        }

    }

}
