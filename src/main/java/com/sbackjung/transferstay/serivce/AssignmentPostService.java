package com.sbackjung.transferstay.serivce;

import com.sbackjung.transferstay.domain.Accommodation;
import com.sbackjung.transferstay.domain.AssignmentPost;
import com.sbackjung.transferstay.dto.AssignmentPostRequest;
import com.sbackjung.transferstay.dto.AssignmentPostResponse;
import com.sbackjung.transferstay.repository.AccommodationRepository;
import com.sbackjung.transferstay.repository.AssignmentPostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
}
