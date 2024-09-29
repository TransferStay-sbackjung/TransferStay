package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.Enum.BidType;
import com.sbackjung.transferstay.domain.Auction;
import com.sbackjung.transferstay.domain.AuctionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AuctionTransactionRepository extends JpaRepository<AuctionTransaction, Long> {

    Optional<AuctionTransaction> findByAuctionIdAndBidderId (Long auctionId, Long bidderId);
    List<AuctionTransaction> findByAuctionId(Long auctionId);
    List<AuctionTransaction> findByAuctionIdAndType(Long auctionId,
                                                    BidType type);
    List<AuctionTransaction> findByBidderId(Long bidderId);

}


// 외래키 설정이 되어있지않아서.. 사용불가 JOIN
//    @Query("SELECT a From Auction a JOIN AuctionTransaction at ON a.auctionId" +
//            " = at.auctionId WHERE at.bidderId = :userId")
//    List<Auction> findUserAuctionByBidderId(@Param("userId") Long userId);