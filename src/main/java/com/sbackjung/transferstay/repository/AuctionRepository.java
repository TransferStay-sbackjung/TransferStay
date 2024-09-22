package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Long, Auction> {
}
