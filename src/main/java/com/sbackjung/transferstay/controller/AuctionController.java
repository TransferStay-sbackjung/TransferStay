package com.sbackjung.transferstay.controller;

import static com.sbackjung.transferstay.utils.UserIdHolder.getUserIdFromToken;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.Auction;
import com.sbackjung.transferstay.domain.AuctionTransaction;
import com.sbackjung.transferstay.dto.*;
import com.sbackjung.transferstay.repository.AuctionRepository;
import com.sbackjung.transferstay.repository.AuctionTransactionRepository;
import com.sbackjung.transferstay.service.AuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auction", description = "경매 API")
public class AuctionController {

  private final AuctionService auctionService;
  public final AuctionRepository auctionRepository;
  public final AuctionTransactionRepository auctionTransactionRepository;

  @Operation(summary = "경매 생성", description = "새로운 경매를 생성합니다.")
  @PostMapping("/")
  public ResponseEntity<JsonResponse> createAuction(
      @RequestBody @Valid AuctionPostRequestDto request
  ) {
    log.info("[API] createAuction : {}", request);
    Long userId = getUserIdFromToken();
    AuctionPostResponseDto data = auctionService.createAuction(request, userId);
    return ResponseEntity.ok(new JsonResponse(200, "경매 생성 완료.", data));
  }

  @Operation(summary = "경매 수정", description = "경매 정보를 수정합니다.")
  @PatchMapping("/{auctionId}")
  public ResponseEntity<JsonResponse> updateAuction(
      @PathVariable Long auctionId,
      @RequestBody @Valid AuctionUpdateRequestDto request
  ) {
    Long userId = getUserIdFromToken();
    AuctionUpdateResponseDto data = auctionService.updateAuction(request,
        userId, auctionId);
    return ResponseEntity.ok(new JsonResponse(200, "경매 수정 완료.", data));
  }

  /*
      ?page=0&size=10&sort=startDate,desc
   */
  @Operation(summary = "경매 목록 조회", description = "페이지별 경매 목록을 조회합니다.")
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

  @Operation(summary = "경매 상세 조회", description = "특정 경매의 세부 정보를 조회합니다.")
  @GetMapping("/details/{auctionId}")
  public ResponseEntity<JsonResponse> getAuctionDetail(
      @PathVariable(value = "auctionId") Long auctionId
  ) {
    AuctionGetDetailDto data = auctionService.getAuctionDetails(auctionId);
    return ResponseEntity.ok(new JsonResponse(200, "경매 세부정보 불러오기 완료.",
        data));
  }

  @Operation(summary = "경매 삭제", description = "특정 경매를 삭제합니다.")
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
  @Operation(summary = "응찰하기", description = "경매에 응찰합니다.")
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
        auction.getAuctionId(), userId);

    // auctionTransaction 로그로 값 확인(추후 삭제 에정)
    if (auctionTransaction.isPresent()) {
      log.info("AuctionTransaction found: {}", auctionTransaction.get());
    } else {
      log.info("No AuctionTransaction found for auctionId: {} and userId: {}",
          auction.getAuctionId(), userId);
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

  @Operation(summary = "즉시 구매", description = "경매 물건을 즉시 구매합니다.")
  @PostMapping("/{postId}/purchase")
  public ResponseEntity<JsonResponse> auctionPurchase(
          @PathVariable Long postId
  ){

    AuctionPurchaseDto data = auctionService.auctionPurchase(postId, getUserIdFromToken());
    return ResponseEntity.ok(new JsonResponse(200,"경매 즉시 구매가 완료되었습니다.",data));
  }
}
