package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.LikeAccommodation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeAccommodationRepository extends JpaRepository<LikeAccommodation, Long> {

  Optional<LikeAccommodation> findByUserIdAndAssignmentPostId(Long userId, Long postId);

  void deleteByUserIdAndAssignmentPostId(Long userId, Long postId);

  List<LikeAccommodation> findByUserId(Long userId);
}
