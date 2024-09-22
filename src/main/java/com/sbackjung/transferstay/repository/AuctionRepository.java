package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    // auctionId로 경매상세정보
    Optional<Auction> findAuctionByActionId(Long auctionId);

    // findAll을 사용해서 불러오고, pageSort를 통해서 정렬
    Page<Auction> findAll(Pageable pageable);

    Optional<Auction> findByPostId (Long postId);
}
