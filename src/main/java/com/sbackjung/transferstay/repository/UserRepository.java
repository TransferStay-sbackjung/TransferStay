package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDomain,Long> {
}
