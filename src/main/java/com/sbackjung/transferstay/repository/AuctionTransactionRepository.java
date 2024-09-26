package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.Enum.BidType;
import com.sbackjung.transferstay.domain.AuctionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AuctionTransactionRepository extends JpaRepository<AuctionTransaction, Long> {

    Optional<AuctionTransaction> findByAuctionIdAndBidderId (Long auctionId, Long bidderId);
    List<AuctionTransaction> findByAuctionId(Long auctionId);
    List<AuctionTransaction> findByAuctionIdAndType(Long auctionId,
                                                    BidType type);
}
