package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.AuctionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionTransactionRepository extends JpaRepository<AuctionTransaction, Long> {

    Optional<AuctionTransaction> findByAuctionIdAndBidderId (Long auctionId, Long bidderId);
}
