package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.UserDomain;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDomain,Long> {
    Optional<UserDomain> findByEmail(String email);
    Optional<UserDomain> findByOauthId(String oauthId);
    Optional<UserDomain> findByUserId(Long userId);
    boolean existsByEmail(String email);

}
