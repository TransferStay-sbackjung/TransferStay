package com.sbackjung.transferstay.scheduler;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.repository.AuctionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;

    // cron = "초 분 시 일 월 요일" 매분 0초마다 실행
    // 경매물의 상태를 Waiting -> progress, progress -> bid fail로 변경, Success는 경매 성공
    // 로직에서 변경할것이기 때문에 없음
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updateAuctionStatusBidFail(){
        // 경매 성공시 BiD Success 상태로 변경할것이기 때문에, 굳이 바꿔주지 않아도됨
        log.info("Auction Status Update By Trigger at {}",LocalDateTime.now());
        auctionRepository.updateAuctionStatus(
                AuctionStatus.IN_PROGRESS,
                AuctionStatus.BID_FAIL,
                LocalDateTime.now(),
                AuctionStatus.WAITING);
    }

}
