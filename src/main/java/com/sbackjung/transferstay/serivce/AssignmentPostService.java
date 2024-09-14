package com.sbackjung.transferstay.serivce;

import com.sbackjung.transferstay.Enum.PostStatus;
import com.sbackjung.transferstay.domain.AssignmentPost;
import com.sbackjung.transferstay.dto.AssignmentPostRequestDto;
import com.sbackjung.transferstay.dto.AssignmentPostResponseDto;
import com.sbackjung.transferstay.dto.AssignmentPostUpdateRequestDto;
import com.sbackjung.transferstay.repository.AssignmentPostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentPostService {

  private final AssignmentPostRepository assignmentPostRepository;

  @Transactional
  public AssignmentPostResponseDto createAssignmentPost(AssignmentPostRequestDto request, Long userId) {
    AssignmentPost assignmentPost = AssignmentPost.builder()
        .userId(userId)
        .title(request.getTitle())
        .price(request.getPrice())
        .description(request.getDescription())
        .isAuction(request.getIsAuction())
        .locationDepth1(request.getLocationDepth1())
        .locationDepth2(request.getLocationDepth2())
        .reservationPlatform(request.getReservationPlatform())
        .checkInDate(request.getCheckInDate())
        .checkOutDate(request.getCheckOutDate())
        .reservationCode(request.getReservationCode())
        .reservationName(request.getReservationName())
        .reservationPhone(request.getReservationPhone())
        .status(PostStatus.PROGRESS)
        .build();

    AssignmentPost savedPost = assignmentPostRepository.save(assignmentPost);
    return toResponse(savedPost);
  }

  @Transactional
  public AssignmentPostResponseDto getAssignmentPost(Long id) {
    AssignmentPost post = assignmentPostRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Assignment post not found"));
    return toResponse(post);
  }

  @Transactional
  public Page<AssignmentPostResponseDto> getAllAssignmentPosts(Pageable pageable) {
    return assignmentPostRepository.findAll(pageable).map(this::toResponse);
  }

  @Transactional
  public AssignmentPostResponseDto updateAssignmentPost(Long postId, AssignmentPostUpdateRequestDto request) {
    AssignmentPost assignmentPost = assignmentPostRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Assignment post not found"));

    assignmentPost.update(request);
    return toResponse(assignmentPost);
  }

  private AssignmentPostResponseDto toResponse(AssignmentPost assignmentPost) {
    return AssignmentPostResponseDto.builder()
        .id(assignmentPost.getId())
        .title(assignmentPost.getTitle())
        .price(assignmentPost.getPrice())
        .description(assignmentPost.getDescription())
        .isAuction(assignmentPost.isAuction())
        .locationDepth1(assignmentPost.getLocationDepth1())
        .locationDepth2(assignmentPost.getLocationDepth2())
        .reservationPlatform(assignmentPost.getReservationPlatform())
        .checkInDate(assignmentPost.getCheckInDate())
        .checkOutDate(assignmentPost.getCheckOutDate())
        .reservationCode(assignmentPost.getReservationCode())
        .reservationName(assignmentPost.getReservationName())
        .reservationPhone(assignmentPost.getReservationPhone())
        .status(assignmentPost.getStatus())
        .createdAt(assignmentPost.getCreatedAt())
        .updatedAt(assignmentPost.getUpdatedAt())
        .build();
  }

  @Transactional
  public void deleteAssignmentPost(Long postId) {
    // todo : 작성자(user) 유무 및 게시글 작성자인지 확인 후 삭제 로직 추가
    AssignmentPost assignmentPost = assignmentPostRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

    // 유저 일치 여부
    // if (!assignmentPost.getUserId().equals(userId)) {
    //            throw new IllegalArgumentException("You are not authorized to delete this post");

    assignmentPostRepository.deleteById(postId);
  }
}
