package com.sbackjung.transferstay.serivce;

import com.sbackjung.transferstay.domain.Accommodation;
import com.sbackjung.transferstay.domain.AssignmentPost;
import com.sbackjung.transferstay.dto.AssignmentPostRequest;
import com.sbackjung.transferstay.dto.AssignmentPostResponse;
import com.sbackjung.transferstay.repository.AccommodationRepository;
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
  private final AccommodationRepository accommodationRepository;

  @Transactional
  public AssignmentPostResponse createAssignmentPost(AssignmentPostRequest request, Long userId) {

    // Accommodation 엔티티 생성 및 저장
    Accommodation accommodation = Accommodation.builder()
        .location(request.getLocation())
        .reservationPlatform(request.getReservationPlatform())
        .checkInDate(request.getCheckInDate())
        .ckeckOutDate(request.getCheckOutDate())
        .build();
    Accommodation savedAccommodation = accommodationRepository.save(accommodation);

    // AssignmentPost 엔티티 생성 및 저장
    AssignmentPost assignmentPost = AssignmentPost.builder()
        .userId(userId)
        .title(request.getTitle())
        .price(request.getPrice())
        .description(request.getDescription())
        .isAuction(request.isAuction())
        .accommodation(savedAccommodation)  // Accommodation 연결
        .build();
    AssignmentPost savedPost = assignmentPostRepository.save(assignmentPost);

    return toResponse(savedPost);
  }
  @Transactional
  public AssignmentPostResponse getAssignmentPost(Long id) {
    AssignmentPost post = assignmentPostRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Assignment post not found"));
    return toResponse(post);
  }

  @Transactional
  public Page<AssignmentPostResponse> getAllAssignmentPosts(Pageable pageable) {
    return assignmentPostRepository.findAll(pageable).map(this::toResponse);
  }

  @Transactional
  public AssignmentPostResponse updateAssignmentPost(Long postId, AssignmentPostRequest request) {
    AssignmentPost assignmentPost = assignmentPostRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Assignment post not found"));

    assignmentPost.update(
        request.getTitle(),
        request.getPrice(),
        request.getDescription(),
        request.isAuction()
    );
    assignmentPost.updateAccommodation(
        request.getLocation(),
        request.getCheckInDate(),
        request.getCheckOutDate(),
        request.getReservationPlatform()
    );

    return toResponse(assignmentPost);
  }

  private AssignmentPostResponse toResponse(AssignmentPost assignmentPost) {
    Accommodation accommodation = assignmentPost.getAccommodation();
    return AssignmentPostResponse.builder()
        .id(assignmentPost.getId())
        .title(assignmentPost.getTitle())
        .price(assignmentPost.getPrice())
        .description(assignmentPost.getDescription())
        .isAuction(assignmentPost.isAuction())
        .createdAt(assignmentPost.getCreatedAt())
        .updatedAt(assignmentPost.getUpdatedAt())
        // accommdation
        .location(accommodation.getLocation())
        .reservationPlatform(accommodation.getReservationPlatform())
        .checkInDate(accommodation.getCheckInDate())
        .checkOutDate(accommodation.getCkeckOutDate())
        .build();
  }

  @Transactional
  public void deleteAssignmentPost(Long postId) {
    // todo : 작성자(user) 유무 및 게시글 작성자인지 확인 후 삭제 로직 추가

    AssignmentPost assignmentPost = assignmentPostRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    assignmentPostRepository.deleteById(postId);

  }

}
