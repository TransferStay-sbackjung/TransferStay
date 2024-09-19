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
    boolean existsByEmail(String email);

    // 예치금 충전에서 동시성 문제 해결을 위해 사용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserDomain u WHERE u.oauthId = :oauthId")
    Optional<UserDomain> findByOauthIdWithLock(@Param("oauthId") String oauthId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserDomain u WHERE u.email = :email")
    Optional<UserDomain> findByEmailWithLock(@Param("email") String email);
}
