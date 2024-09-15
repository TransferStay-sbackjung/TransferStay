package com.sbackjung.transferstay.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "test_search")
public class TestSearch {

    //title, location_depth1, location_depth2, check_in_date, check_out_date, personnel

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "location_depth1")
    private String locationDepth1;

    @Column(name= "location_depth2")
    private String locationDepth2;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    private int personnel;

}
