package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.*;
import com.sbackjung.transferstay.service.AssignmentPostService;
import com.sbackjung.transferstay.service.AuctionService;
import com.sbackjung.transferstay.service.LikeAccommodationService;
import com.sbackjung.transferstay.service.UserService;
import com.sbackjung.transferstay.utils.UserIdHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/")
@Slf4j
@Tag(name = "UserProfile", description = "마이페이지 API")
public class UserProfileController {

  private final UserService userService;
  private final AuctionService auctionService;
  private final AssignmentPostService assignmentPostService;
  private final LikeAccommodationService likeAccommodationService;

  @Operation(summary = "사용자 조회", description = "사용자 정보를 조회합니다.")
  @GetMapping
  public ResponseEntity<JsonResponse> getUserProfile() {
    Long userId = UserIdHolder.getUserIdFromToken();
    UserInfoResponseDto responseDto = userService.getUserInfo(userId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "사용자 정보를 조회하였습니다.", responseDto);
    log.info("[API] getUserProfile");
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "사용자 정보 수정", description = "사용자의 정보를 수정합니다")
  @PutMapping
  public ResponseEntity<JsonResponse> update(@RequestBody UserInfoUpdateRequestDto request) {
    Long userId = UserIdHolder.getUserIdFromToken();
    userService.updateUserInfo(userId, request);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "사용자 정보가 수정되었습니다.", null);
    log.info("[API] update 실행 ");
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "사용자가 작성한 게시물 조회", description = "사용자가 작성한 게시물을 조회합니다.")
  @GetMapping("/posts")
  public ResponseEntity<JsonResponse> getUserPosts() {
    Long userId = UserIdHolder.getUserIdFromToken();
    List<AssignmentPostResponseDto> userPosts = assignmentPostService.getUserPosts(userId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "사용자가 작성한 게시물을 조회하였습니다.", userPosts);
    log.info("[API] getUserPosts");
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "사용자가 좋아요 한 게시물 조회", description = "사용자가 좋아요한 게시물을 조회합니다.")
  @GetMapping("/liked-posts")
  public ResponseEntity<JsonResponse> getUserLikedPosts() {
    Long userId = UserIdHolder.getUserIdFromToken();
    List<AssignmentPostResponseDto> likedPosts = likeAccommodationService.getUserLikedPosts(userId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "사용자가 좋아요한 게시물을 조회하였습니다.", likedPosts);
    log.info("[API] getUserLikedPosts");
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "사용자가 참여한 한 경매글 조회", description = "사용자가 참여한 경매를 " +
          "조회합니다.")
  @GetMapping("/auction-posts")
  public ResponseEntity<JsonResponse> getUserAuctionPosts() {
    Long userId = UserIdHolder.getUserIdFromToken();
    List<AuctionGetListDto> userAuctionList = auctionService.getUserAuctionList(userId);
    JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "사용자가 " +
            "참여한 경매를 조회했습니다.", userAuctionList);
    log.info("[API] getUserAuctionPosts");
    return ResponseEntity.ok(response);
  }
}
