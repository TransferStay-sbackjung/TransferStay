package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.domain.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    // findAll을 사용하여 정렬을 Pageable로 처리
    Page<Auction> findAll(Pageable pageable);
}
