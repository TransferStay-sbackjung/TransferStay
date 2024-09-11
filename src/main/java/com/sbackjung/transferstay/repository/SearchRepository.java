package com.sbackjung.transferstay.repository;

import com.sbackjung.transferstay.domain.AssignmentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<AssignmentPost, Long> {

    @Query("SELECT p FROM AssignmentPost p WHERE " +
            "(:freeField IS NULL OR p.title LIKE %:freeField%) AND " +
            "(:checkIn IS NULL OR p.checkInDate = :checkIn) AND " +
            "(:checkOut IS NULL OR p.checkOutDate = :checkOut) AND " +
            "(:personnel = 0 OR p.personnel = :personnel) AND " +
            "(:locationDepth1 IS NULL OR p.locationDepth1 = :locationDepth1) AND " +
            "(:locationDepth2 IS NULL OR p.locationDepth2 = :locationDepth2)")
    List<AssignmentPost> searchPosts(@Param("freeField") String freeField,
                                     @Param("checkIn") LocalDate checkInDate,
                                     @Param("checkOut") LocalDate checkOutDate,
                                     @Param("personnel") int personnel,
                                     @Param("locationDepth1") String locationDepth1,
                                     @Param("locationDepth2") String locationDepth2);
}