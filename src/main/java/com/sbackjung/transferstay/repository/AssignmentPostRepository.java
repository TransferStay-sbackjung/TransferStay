package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.Enum.PostStatus;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sbackjung.transferstay.domain.AssignmentPost;

public interface AssignmentPostRepository extends JpaRepository<AssignmentPost, Long> {
  // ID로 조회할 때 상태가 DELETED가 아닌 경우만 반환
  Optional<AssignmentPost> findByIdAndStatusNot(Long id, PostStatus status);

  // 전체 조회할 때 상태가 DELETED가 아닌 경우만 반환
  Page<AssignmentPost> findByStatusNot(PostStatus status, Pageable pageable);

  // 사용자가 작성한 게시물 조회
  List<AssignmentPost> findByUserId(Long userId);
}
