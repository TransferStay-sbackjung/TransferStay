package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.Escrow;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EscrowRepository extends JpaRepository<Escrow, Long> {
  Optional<Escrow> findByAssignmentPostId(Long assignmentPostId);
}
