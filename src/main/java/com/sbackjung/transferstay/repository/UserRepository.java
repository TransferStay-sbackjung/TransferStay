package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

}
