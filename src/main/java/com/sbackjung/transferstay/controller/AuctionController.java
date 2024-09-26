package com.sbackjung.transferstay.controller;

import static com.sbackjung.transferstay.utils.UserIdHolder.getUserIdFromToken;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.Auction;
import com.sbackjung.transferstay.domain.AuctionTransaction;
import com.sbackjung.transferstay.dto.AuctionGetDetailDto;
import com.sbackjung.transferstay.dto.AuctionGetListDto;
import com.sbackjung.transferstay.dto.AuctionPostRequestDto;
import com.sbackjung.transferstay.dto.AuctionPostResponseDto;
import com.sbackjung.transferstay.dto.AuctionUpdateRequestDto;
import com.sbackjung.transferstay.dto.AuctionUpdateResponseDto;
import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.dto.PlaceBidRequestDto;
import com.sbackjung.transferstay.repository.AuctionRepository;
import com.sbackjung.transferstay.repository.AuctionTransactionRepository;
import com.sbackjung.transferstay.service.AuctionService;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

  private final AuctionService auctionService;
  public final AuctionRepository auctionRepository;
  public final AuctionTransactionRepository auctionTransactionRepository;

  @PostMapping("/")
  public ResponseEntity<JsonResponse> createAuction(
      @RequestBody @Valid AuctionPostRequestDto request
  ) {
    Long userId = getUserIdFromToken();
    AuctionPostResponseDto data = auctionService.createAuction(request, userId);
    return ResponseEntity.ok(new JsonResponse(200, "경매 생성 완료.", data));
  }

  @PatchMapping("/{auctionId}")
  public ResponseEntity<JsonResponse> updateAuction(
      @PathVariable Long auctionId,
      @RequestBody @Valid AuctionUpdateRequestDto request
  ) {
    Long userId = getUserIdFromToken();
    AuctionUpdateResponseDto data = auctionService.updateAction(request,
        userId, auctionId);
    return ResponseEntity.ok(new JsonResponse(200, "경매 수정 완료.", data));
  }

  /*
      ?page=0&size=10&sort=startDate,desc
   */
  @GetMapping({"/{orderBy}", "/"})
  public ResponseEntity<JsonResponse> getAuctionList(
      @PathVariable(value = "orderBy", required = false) String orderBy,
      Pageable pageable
  ) {
    // todo : 현재 최고 경매가를 보여주고자한다면, 메소드를 추가해야할것같습니다.
    Page<AuctionGetListDto> auctionList =
        auctionService.getAuctionList(pageable, orderBy);
    return ResponseEntity.ok(new JsonResponse(200, "경매 리스트 불러오기 완료.",
        auctionList));
  }

  @GetMapping("/details/{auctionId}")
  public ResponseEntity<JsonResponse> getAuctionDetail(
      @PathVariable(value = "auctionId") Long auctionId
  ) {
    AuctionGetDetailDto data = auctionService.getAuctionDetails(auctionId);
    return ResponseEntity.ok(new JsonResponse(200, "경매 세부정보 불러오기 완료.",
        data));
  }

  @DeleteMapping("/{auctionId}")
  public ResponseEntity<JsonResponse> deleteAuction(
      @PathVariable(value = "auctionId") Long auctionId
  ) {
    // todo : 아마 경매에 참여한 사람들의 상태도 봐야할듯합니다.
    Long userId = getUserIdFromToken();
    auctionService.deleteAuction(userId, auctionId);
    return ResponseEntity.ok(new JsonResponse(200, "경매 삭제 완료.", null));
  }

  // 응찰하기
  @PostMapping("/{postId}/bidding")
  public ResponseEntity<JsonResponse> bidding(@PathVariable("postId") Long postId,
      @RequestBody PlaceBidRequestDto requestDto) {

    // 경매 유효성 체크
    Auction auction = auctionRepository.findByPostId(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "해당 경매를 찾을 수 없습니다."));

    if (auction.getStatus() != AuctionStatus.IN_PROGRESS) {
      throw new CustomException(ErrorCode.BAD_REQUEST,
          "진행 중인 경매가 아닙니다 :: " + auction.getStatus().toString());
    }

    // 로직 수정
    if (auction.getStartPrice() >= requestDto.getSuggestPrice()
        || auction.getStartPrice() > requestDto.getMaxPrice()) {
      throw new CustomException(ErrorCode.BAD_REQUEST, "제안가 또는 최대 제안가가 시작가보다 낮거나 잘못되었습니다.");
    }

    Long userId = getUserIdFromToken();

    Optional<AuctionTransaction> auctionTransaction = auctionTransactionRepository.findByAuctionIdAndBidderId(
        auction.getActionId(), userId);

    // auctionTransaction 로그로 값 확인(추후 삭제 에정)
    if (auctionTransaction.isPresent()) {
      log.info("AuctionTransaction found: {}", auctionTransaction.get());
    } else {
      log.info("No AuctionTransaction found for auctionId: {} and userId: {}",
          auction.getActionId(), userId);
    }

    if (auctionTransaction.isEmpty()) {
      // -> 최초 응찰 service
      auctionService.firstBidding(auction, userId, requestDto);
    } else {
      // -> 재 응찰 service
      auctionService.reBidding(auction, auctionTransaction.get(), requestDto);
    }

    return ResponseEntity.ok(new JsonResponse(200, "응찰이 완료되었습니다.", null));
  }

}
