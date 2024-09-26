package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.Enum.BidType;
import com.sbackjung.transferstay.Enum.EscrowStatus;
import com.sbackjung.transferstay.Enum.PostStatus;
import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.*;
import com.sbackjung.transferstay.dto.*;
import com.sbackjung.transferstay.repository.*;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
  private final UserRepository userRepository;
  private final AssignmentPostRepository assignmentPostRepository;
  private final EscrowRepository escrowRepository;
  private final TransactionRepository transactionRepository;

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
  public AuctionUpdateResponseDto updateAuction(AuctionUpdateRequestDto request, Long userId,
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
    List<AuctionBidderDto> bidders = auctionTransActionRepository.findByAuctionId(auctionId)
            .stream().map(AuctionBidderDto::fromEntity).toList();
    // todo : 종료된 경매에 대해서는 그냥 return or 에러처리?
    return AuctionGetDetailDto.from(auction,bidders);
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
    auction.setPurchasePrice(request.getPurchasePrice());

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
    Long hoPrice = calculateHoPrice(auction.getStartPrice());

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

    updateAutoBiddingAfterBid(auction.getActionId(),
            requestDto.getSuggestPrice(),hoPrice);
  }

  // 자동 응찰 로직
  private void handleAutoBidding(Auction auction, Long bidderId, PlaceBidRequestDto requestDto) {

    Long newBidPrice = calculateAutoBidPrice(auction, requestDto.getMaxPrice());
    Long hoPrice = calculateHoPrice(auction.getStartPrice());

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

    updateAutoBiddingAfterBid(auction.getActionId(),
            requestDto.getSuggestPrice(),hoPrice);
  }

  @Transactional
  public AuctionPurchaseDto auctionPurchase(Long postId, Long userId) {
    // 경매글 상태 확인
    Auction auction = auctionRepository.findByPostId(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "해당 " +
                    "경매는 존재하지 않습니다."));
    if(!auction.getStatus().equals(AuctionStatus.IN_PROGRESS)){
      throw new CustomException(ErrorCode.BAD_REQUEST,"해당 경매는 진행중이지않습니다.");
    } 
    // 유저 잔액 확인
    UserDomain user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.UN_AUTHORIZE,
                    "유저 정보가 없습니다."));

    Long userBalance = user.getAmount();
    Long price = auction.getPurchasePrice();

    if(userBalance < price){
      throw new CustomException(ErrorCode.BAD_REQUEST,"잔액이 부족합니다.");
    }

    AssignmentPost post = assignmentPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST,
                    "게시글은 찾을 수 없습니다."));
    // 에스크로 생성
    Escrow escrow = Escrow.builder()
            .assignmentPost(post)
            .buyerId(userId)
            .sellerId(post.getSellerId())
            .amount(price)
            .status(EscrowStatus.IN_PROGRESS)
            .build();
    escrowRepository.save(escrow);
    // 송금 트랜잭션 생성
    Transaction transaction = Transaction.builder()
            .userId(userId)
            .amount(price)
            .type(true)
            .description("결제")
            .balance(userBalance - price)
            .build();
    transactionRepository.save(transaction);
    // 사용자 금액 차감 
    user.setAmount(user.getAmount() - auction.getPurchasePrice());
    userRepository.save(user);
    // 경매 상태 변경
    auction.setStatus(AuctionStatus.BID_SUCCESS);
    auction.setWinningBidderId(userId);
    auction.setWinningPrice(price);
    auctionRepository.save(auction);
    // 게시글 상태 변경
    // todo : 결제중? 거래중? 숙박 다녀와야야 거래가 완료되니까
    post.setStatus(PostStatus.PAYMENT_IN_PROGRESS);
    assignmentPostRepository.save(post);

    return AuctionPurchaseDto.builder()
            .postId(postId)
            .auctionId(auction.getActionId())
            .buyerId(userId)
            .sellerId(post.getSellerId())
            .amount(price)
            .build();
  }

  /*
      자동 응찰 갱신 과정
      
      1. 새로운 응찰자가(자동,수동포함) 응찰
      
      2. 최종응찰가, 최종응찰자를 결정하기위해 반복
      
      2-1. Auto 를 설정한 사람중에서, 현재 입찰가 보다 높은
      MaxPrice 를 가진사람이 있으면 갱신하고 다음 Auto 응찰자로 넘어감

      2-2. 호가가 너무 적어서 최대 응찰자가 기준이 되지 못하는것을
      방지하기위해서 update 가 발생할떄까지,for 문을 계속 반복

      3. 마지막으로 같은 응찰가를 불렀지만 순서차이로 되지 못할수도
      있기 때문에, stream 을 통해서, 가장 먼저 응찰한 사람 기준으로 응찰자를
      한번 더 선택

      4. Auction 응찰자,응찰가 업데이트 및 AuctionTransAuction에 수정된
      응찰가들 업데이트.
   */

  @Transactional
  public void updateAutoBiddingAfterBid(Long auctionId, Long newBidPrice,
                                         Long hoPrice){
    List<AuctionTransaction> autoBidders = auctionTransActionRepository.findByAuctionIdAndType(auctionId, BidType.AUTO);
    // 예외처리는 위에서 일괄적으로 진행 
    Optional<Auction> auction = auctionRepository.findById(auctionId);
    if (auction.isEmpty()) {
      throw new CustomException(ErrorCode.BAD_REQUEST,"경매를 찾을 수 없습니다.");
    }
    // 호가로 다음 입찰가 결정
    Long nextBidPrice = newBidPrice + hoPrice;
    Long lastBidPrice = newBidPrice;
    // 현재 최고 입찰자
    Long currWinnerId = auction.get().getWinningBidderId();

    // 자동 응찰자를 대상으로 입찰 반복 -> 한 바퀴 돌고, 최고 입찰자가 먹지
    // 못하는것을 방지하기위해서.
    boolean isBidUpdated = true;
    
    // 자동응찰과정
    while(isBidUpdated){
      isBidUpdated = false;

      for(AuctionTransaction ta : autoBidders){
        if(ta.getMaxPrice() >= nextBidPrice){
          ta.setSuggestPrice(nextBidPrice);

          // 입찰 가격 상승 및 최종 입찰가 마지막 입찰자 설정
          nextBidPrice += hoPrice;
          lastBidPrice = nextBidPrice;

          currWinnerId = ta.getBidderId();
          isBidUpdated = true; // 입찰이 발생했음으로 반복
        }
      }
    }

    auctionTransActionRepository.saveAll(autoBidders);

    final Long lastBidPrice2= lastBidPrice;
    Optional<AuctionTransaction> winner = autoBidders.stream().filter(
            ta -> ta.getMaxPrice().equals(lastBidPrice2))
            .min(Comparator.comparing(AuctionTransaction::getCreatedAt));

    if(winner.isPresent()){
      currWinnerId = winner.get().getBidderId();
    }

    auction.get().setWinningBidderId(currWinnerId);
    auction.get().setWinningPrice(lastBidPrice);
    auctionRepository.save(auction.get());
  }

  private Long calculateAutoBidPrice(Auction auction, Long maxPrice) {
    final long INCREMENT = 1000L; // 증가 단위 선택
    Long newBidPrice = auction.getWinningPrice() + INCREMENT;
    return Math.min(newBidPrice, maxPrice);
  }

  /*
    우리의 호가

    0 ~ 4.99만원 : 1,000

    5 ~ 9.99만원 : 2,000

    10 ~ 29.99만원 : 5,000

    30 ~ : 10,000
   */
  private Long calculateHoPrice(Long price){
    if(price < 50000L){
      return 1000L;
    }else if(price < 100000L){
      return 2000L;
    }else if(price < 300000L){
      return 5000L;
    }else{
      return 10000L;
    }
  }


  @Transactional
  public void test() {
    List<Auction> byDeadlineAfter =
            auctionRepository.findByDeadlineAfterAndDeadlineBefore(LocalDateTime.now(),LocalDateTime.now().plusMinutes(5));
    if(!byDeadlineAfter.isEmpty()){
      for(Auction a : byDeadlineAfter){
        System.out.println(a.getStatus());
      }
    }

  }


}
