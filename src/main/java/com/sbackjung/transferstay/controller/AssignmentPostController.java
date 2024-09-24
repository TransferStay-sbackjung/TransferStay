package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.AssignmentPostRequestDto;
import com.sbackjung.transferstay.dto.AssignmentPostResponseDto;
import com.sbackjung.transferstay.dto.AssignmentPostUpdateRequestDto;
import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.service.AssignmentPostService;
import com.sbackjung.transferstay.utils.UserIdHolder;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assignment-posts")
@RequiredArgsConstructor
@Tag(name = "Assignment Post", description = "양도 게시글 관리 API")
public class AssignmentPostController {

  private final AssignmentPostService assignmentPostService;

  @Operation(summary = "양도 게시글 생성", description = "새로운 양도글을 작성합니다.")
  @PostMapping
  public ResponseEntity<JsonResponse> createAssignmentPost(
      @Valid @RequestBody AssignmentPostRequestDto request) {
    Long userId = UserIdHolder.getUserIdFromToken();
    AssignmentPostResponseDto response = assignmentPostService.createAssignmentPost(request, userId);
    JsonResponse jsonResponse = new JsonResponse(HttpStatus.OK.value(), "양도글이 생성되었습니다.", response);
    return ResponseEntity.ok(jsonResponse);
  }

  @Operation(summary = "특정 양도 게시글 조회", description = "ID로 특정 양도 게시글을 조회합니다.")
  @GetMapping("/{postId}")
  public ResponseEntity<JsonResponse> getAssignmentPost(@PathVariable Long postId) {
    AssignmentPostResponseDto response = assignmentPostService.getAssignmentPost(postId);
    JsonResponse jsonResponse = new JsonResponse(HttpStatus.OK.value(), "양도글이 조회되었습니다.", response);
    return ResponseEntity.ok(jsonResponse);
  }

  @Operation(summary = "모든 양도 게시글 조회", description = "모든 양도 게시글을 조회합니다.")
  @GetMapping
  public ResponseEntity<JsonResponse> getAllAssignmentPosts(Pageable pageable) {
    Page<AssignmentPostResponseDto> response = assignmentPostService.getAllAssignmentPosts(pageable);
    JsonResponse jsonResponse = new JsonResponse(HttpStatus.OK.value(), "모든 양도글이 조회되었습니다.", response);
    return ResponseEntity.ok(jsonResponse);
  }

  @Operation(summary = "양도 게시글 수정", description = "ID로 특정 양도 게시글을 수정합니다.")
  @PutMapping("/{postId}")
  public ResponseEntity<JsonResponse> updateAssignmentPost(
      @PathVariable Long postId,
      @Valid @RequestBody AssignmentPostUpdateRequestDto request) {
    AssignmentPostResponseDto updatedResponse = assignmentPostService.updateAssignmentPost(postId, request);
    JsonResponse jsonResponse = new JsonResponse(HttpStatus.OK.value(), "양도글이 수정되었습니다.", updatedResponse);
    return ResponseEntity.ok(jsonResponse);
  }

  @Operation(summary = "양도 게시글 삭제", description = "ID로 특정 양도 게시글을 삭제합니다.")
  @DeleteMapping("/{postId}")
  public ResponseEntity<JsonResponse> deleteAssignmentPost(@PathVariable Long postId) {
    Long userId = UserIdHolder.getUserIdFromToken();
    assignmentPostService.deleteAssignmentPost(postId,userId);
    JsonResponse jsonResponse = new JsonResponse(HttpStatus.NO_CONTENT.value(), "양도글이 삭제되었습니다.", null);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(jsonResponse);
  }
}