package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.domain.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    // auctionId로 경매상세정보
    Optional<Auction> findAuctionByActionId(Long auctionId);

    // findAll을 사용해서 불러오고, pageSort를 통해서 정렬
    Page<Auction> findAll(Pageable pageable);

    Optional<Auction> findByPostId (Long postId);

    @Modifying
    @Query("UPDATE Auction a SET a.status = " +
            "CASE " +
            "WHEN a.status = :waiting AND a.startTime <= :currentTime THEN :inProgress " +
            "WHEN a.status = :inProgress AND a.deadline <= :currentTime THEN :bidFail " +
            "END " +
            "WHERE (a.status = :waiting AND a.startTime <= :currentTime) " +
            "OR (a.status = :inProgress AND a.deadline <= :currentTime)")
    int updateAuctionStatus(@Param("inProgress") AuctionStatus inProgress,
                                   @Param("bidFail") AuctionStatus bidFail,
                                   @Param("currentTime") LocalDateTime currentTime,
                                   @Param("waiting") AuctionStatus waiting);
}
