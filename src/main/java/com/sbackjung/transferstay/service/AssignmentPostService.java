package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.Enum.PostStatus;
import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
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
            .status(PostStatus.PAYMENT_IN_PROGRESS)
            .build();

    AssignmentPost savedPost = assignmentPostRepository.save(assignmentPost);
    return toResponse(savedPost);
  }

  @Transactional
  public AssignmentPostResponseDto getAssignmentPost(Long id) {
    AssignmentPost post = assignmentPostRepository.findByIdAndStatusNot(id, PostStatus.DELETED)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "게시글을 찾을 수 없습니다."));
    return toResponse(post);
  }

  @Transactional
  public Page<AssignmentPostResponseDto> getAllAssignmentPosts(Pageable pageable) {
    return assignmentPostRepository.findByStatusNot(PostStatus.DELETED, pageable).map(this::toResponse);
  }

  @Transactional
  public AssignmentPostResponseDto updateAssignmentPost(Long postId, AssignmentPostUpdateRequestDto request) {
    AssignmentPost assignmentPost = assignmentPostRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "게시글을 찾을 수 없습니다."));

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
  public void deleteAssignmentPost(Long postId, Long userId) {
    AssignmentPost assignmentPost = assignmentPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND,
                    "게시글을 찾을수 없습니다."));

    // 이미 삭제된 게시글일 경우
    if(assignmentPost.getStatus() == PostStatus.DELETED) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND, "이미 삭제된 게시글입니다.");
    }
    // 유저 일치 여부
    if (!assignmentPost.getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.UN_AUTHORIZE, "해당 게시글을 삭제할" +
              " 권한이 없습니다.");
    }
    // status DELETE 로 변경
    assignmentPost.setStatus(PostStatus.DELETED);
  }
}
