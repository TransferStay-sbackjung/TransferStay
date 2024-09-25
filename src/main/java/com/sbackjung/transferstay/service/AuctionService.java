package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.Enum.BidType;
import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.Auction;
import com.sbackjung.transferstay.domain.AuctionTransaction;
import com.sbackjung.transferstay.dto.AuctionDataSetDto;
import com.sbackjung.transferstay.dto.AuctionGetDetailDto;
import com.sbackjung.transferstay.dto.AuctionGetListDto;
import com.sbackjung.transferstay.dto.AuctionPostRequestDto;
import com.sbackjung.transferstay.dto.AuctionPostResponseDto;
import com.sbackjung.transferstay.dto.AuctionUpdateRequestDto;
import com.sbackjung.transferstay.dto.AuctionUpdateResponseDto;
import com.sbackjung.transferstay.dto.PlaceBidRequestDto;
import com.sbackjung.transferstay.repository.AuctionRepository;
import com.sbackjung.transferstay.repository.AuctionTransactionRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {

  private final AuctionRepository auctionRepository;
  private final AuctionTransactionRepository auctionTransActionRepository;

  @Transactional
  public AuctionPostResponseDto createAuction(AuctionPostRequestDto request, Long userId
  ) {
    Auction auction = new Auction();
    auction.setUserId(userId);
    auction.setPostId(request.getPostId());

    setAuctionDetails(auction, request);
    return AuctionPostResponseDto.from(auctionRepository.save(auction));
  }

  @Transactional
  public AuctionUpdateResponseDto updateAction(AuctionUpdateRequestDto request, Long userId,
      Long auctionId) {
    // todo : 경매 참여중인 인원이나 시간에 대해서 제한이 필요할것같습니다.
    Auction auction = auctionRepository.findAuctionByActionId(auctionId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST,
            "해당 경매는 존재하지않습니다."));
    if (!auction.getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.UN_AUTHORIZE, "해당 경매를 수정할 권한이 " +
          "없습니다.");
    }

    setAuctionDetails(auction, request);

    return AuctionUpdateResponseDto.from(auctionRepository.save(auction));
  }

  @Transactional
  public Page<AuctionGetListDto> getAuctionList(Pageable pageable, String orderBy) {
    // 동적 정렬 설정 기본은 오름차순 0 1 2 3
    Sort sort = Sort.by("status").ascending();  // 기본 정렬: 상태 오름차순

    if ("desc".equalsIgnoreCase(orderBy)) {
      sort = Sort.by("status").descending();  // 내림차순 정렬
    }

    Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        sort);

    // 상태별로 정렬된 데이터 가져오기
    return auctionRepository.findAll(sortedPageable)
        .map(AuctionGetListDto::from);
  }

  @Transactional
  public AuctionGetDetailDto getAuctionDetails(Long auctionId) {
    Auction auction = auctionRepository.findAuctionByActionId(auctionId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST,
            "해당 경매는 존재하지않습니다."));
    // todo : 종료된 경매에 대해서는 그냥 return or 에러처리?
    return AuctionGetDetailDto.from(auction);
  }

  @Transactional
  public void deleteAuction(Long userId, Long auctionId) {
    Auction auction = auctionRepository.findAuctionByActionId(auctionId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST,
            "해당 경매는 존재하지 않습니다."));
    if (!auction.getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.UN_AUTHORIZE, "해당 경매를 삭제할 권한이 " +
          "없습니다.");
    }
    auctionRepository.delete(auction);
  }

  // 편의성을 위해서 String -> LocalDateTime로 변환해주는 함수
  private static LocalDateTime combineDateAndTime(String date, String time) {
    return LocalDateTime.parse(date + "T" + time);
  }

  // 시작 시간, 마감 시간, 데드라인, 경매가를 설정해주는 메소드
  private void setAuctionDetails(Auction auction, AuctionDataSetDto request) {
    LocalDateTime startTime = combineDateAndTime(request.getStartDate(), request.getStartTime());
    LocalDateTime deadline = combineDateAndTime(request.getDeadlineDate(),
        request.getDeadlineTime());

    auction.setStartTime(startTime);
    auction.setDeadline(deadline);
    auction.setStartPrice(request.getStartPrice());

    // 경매 상태 설정
    if (startTime.isAfter(LocalDateTime.now())) {
      auction.setStatus(AuctionStatus.WAITING);
    } else {
      auction.setStatus(AuctionStatus.IN_PROGRESS);
    }
  }


  // 최초 응찰 service (userId == bidderId)
  public void firstBidding(Auction auction, Long bidderId, PlaceBidRequestDto requestDto) {
    // 입찰가가 시작가 이상인지 확인
    if (requestDto.getSuggestPrice() < auction.getStartPrice()) {
      throw new CustomException(ErrorCode.BAD_REQUEST, "입찰가는 시작가 이상이어야 합니다.");
    }

    // 자동 응찰인지 수동 응찰인지 확인
    if (requestDto.getBidType() == BidType.AUTO) {
      // 자동 응찰 로직
      handleAutoBidding(auction, bidderId, requestDto);
    } else {
      // 수동 응찰 로직
      handleManualBidding(auction, bidderId, requestDto);
    }
  }

  // 재 응찰 service
  public void reBidding(Auction auction, AuctionTransaction auctionTransaction, PlaceBidRequestDto requestDto) {
    if (requestDto.getSuggestPrice() <= auction.getWinningPrice()) {
      throw new CustomException(ErrorCode.BAD_REQUEST, "입찰가는 현재가보다 높아야 합니다.");
    }

    // 자동 응찰인지 수동 응찰인지 확인
    if (requestDto.getBidType() == BidType.AUTO) {
      // 자동 응찰 로직
      handleAutoBidding(auction, auctionTransaction.getBidderId(), requestDto);
    } else {
      // 수동 응찰 로직
      handleManualBidding(auction, auctionTransaction.getBidderId(), requestDto);
    }
  }

  // 수동 응찰 로직
  private void handleManualBidding(Auction auction, Long bidderId, PlaceBidRequestDto requestDto) {

    AuctionTransaction auctionTransaction = AuctionTransaction.builder()
        .auctionId(auction.getActionId())
        .bidderId(bidderId)
        .suggestPrice(requestDto.getSuggestPrice())
        .maxPrice(requestDto.getMaxPrice())
        .type(BidType.MANUAL)
        .build();
    auctionTransActionRepository.save(auctionTransaction);

    // 경매 상태 업데이트 (최고가 및 낙찰자 정보)
    auction.setWinningPrice(requestDto.getSuggestPrice());
    auction.setWinningBidderId(bidderId);
    auctionRepository.save(auction);
  }
  // 자동 응찰 로직
  private void handleAutoBidding(Auction auction, Long bidderId, PlaceBidRequestDto requestDto) {

    Long newBidPrice = calculateAutoBidPrice(auction, requestDto.getMaxPrice());

    // 입찰 기록 생성 또는 갱신
    AuctionTransaction auctionTransaction = AuctionTransaction.builder()
        .auctionId(auction.getActionId())
        .bidderId(bidderId)
        .suggestPrice(newBidPrice)
        .maxPrice(requestDto.getMaxPrice())
        .type(BidType.AUTO)
        .build();
    auctionTransActionRepository.save(auctionTransaction);

    // 경매 상태 업데이트 (최고가 및 낙찰자 정보)
    auction.setWinningPrice(newBidPrice);
    auction.setWinningBidderId(bidderId);
    auctionRepository.save(auction);
  }

  private Long calculateAutoBidPrice(Auction auction, Long maxPrice) {
    final long INCREMENT = 1000L; // 증가 단위 선택
    Long newBidPrice = auction.getWinningPrice() + INCREMENT;
    return Math.min(newBidPrice, maxPrice);
  }
}
