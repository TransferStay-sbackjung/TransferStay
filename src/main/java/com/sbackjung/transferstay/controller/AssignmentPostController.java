package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.AssignmentPostRequestDto;
import com.sbackjung.transferstay.dto.AssignmentPostResponseDto;
import com.sbackjung.transferstay.dto.AssignmentPostUpdateRequestDto;
import com.sbackjung.transferstay.serivce.AssignmentPostService;
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
@RequestMapping("/assignment-posts")
@RequiredArgsConstructor
public class AssignmentPostController {

  private final AssignmentPostService assignmentPostService;

  @PostMapping("/write")
  public ResponseEntity<AssignmentPostResponseDto> createAssignmentPost(
      @Valid @RequestBody AssignmentPostRequestDto request) {
    // TODO: 실제 인증 구현 시 현재 로그인한 사용자의 ID를 가져오도록 수정
    Long userId = 1L; // 임시 userId. 실제 구현 시 이 부분을 수정해야 함
    AssignmentPostResponseDto response = assignmentPostService.createAssignmentPost(request, userId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<AssignmentPostResponseDto> getAssignmentPost(@PathVariable Long postId) {
    AssignmentPostResponseDto response = assignmentPostService.getAssignmentPost(postId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/")
  public ResponseEntity<Page<AssignmentPostResponseDto>> getAllAssignmentPosts(Pageable pageable) {
    Page<AssignmentPostResponseDto> response = assignmentPostService.getAllAssignmentPosts(pageable);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{postId}")
  public ResponseEntity<AssignmentPostResponseDto> updateAssignmentPost(
      @PathVariable Long postId,
      @Valid @RequestBody AssignmentPostUpdateRequestDto request) {
    AssignmentPostResponseDto updatedResponse = assignmentPostService.updateAssignmentPost(postId, request);
    return ResponseEntity.ok(updatedResponse);
  }

  @DeleteMapping("/{postId}")

  public ResponseEntity<AssignmentPostResponseDto> deleteAssignmentPost(@PathVariable Long postId) {
    //todo : 사용자 인증정보 추가

    assignmentPostService.deleteAssignmentPost(postId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


}