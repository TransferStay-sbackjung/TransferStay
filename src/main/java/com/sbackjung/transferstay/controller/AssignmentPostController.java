package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.AssignmentPostRequest;
import com.sbackjung.transferstay.dto.AssignmentPostResponse;
import com.sbackjung.transferstay.serivce.AssignmentPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assignment-posts")
@RequiredArgsConstructor
public class AssignmentPostController {

  private final AssignmentPostService assignmentPostService;

  @PostMapping("/write")
  public ResponseEntity<AssignmentPostResponse> createAssignmentPost(
      @Valid @RequestBody AssignmentPostRequest request) {
    // TODO: 실제 인증 구현 시 현재 로그인한 사용자의 ID를 가져오도록 수정
    Long userId = 1L; // 임시 userId. 실제 구현 시 이 부분을 수정해야 함
    AssignmentPostResponse response = assignmentPostService.createAssignmentPost(request, userId);
    return ResponseEntity.ok(response);
  }


}