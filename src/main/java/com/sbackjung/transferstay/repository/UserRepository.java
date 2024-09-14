package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDomain,Long> {
    Optional<UserDomain> findByEmail(String email);
    Optional<UserDomain> findByOauthId(String oauthId);
}
