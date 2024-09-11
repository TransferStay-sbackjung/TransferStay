package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.TestSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<TestSearch, Long> {

    @Query("SELECT p FROM TestSearch p WHERE " +
            "(:freeField IS NULL OR p.title LIKE %:freeField%) AND " +
            "(:checkInDate IS NULL OR p.checkInDate = :checkInDate) AND " +
            "(:checkOutDate IS NULL OR p.checkOutDate = :checkOutDate) AND " +
            "(:personnel = 0 OR p.personnel = :personnel) AND " +
            "(:locationDepth1 IS NULL OR p.locationDepth1 = :locationDepth1) AND " +
            "(:locationDepth2 IS NULL OR p.locationDepth2 = :locationDepth2)")
    List<TestSearch> searchPosts(@Param("freeField") String freeField,
                                 @Param("checkInDate") LocalDate checkInDate,
                                 @Param("checkOutDate") LocalDate checkOutDate,
                                 @Param("personnel") int personnel,
                                 @Param("locationDepth1") String locationDepth1,
                                 @Param("locationDepth2") String locationDepth2);
}