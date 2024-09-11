package com.sbackjung.transferstay.dto;

import lombok.*;


public class SearchResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getSearchResponse{
        private Long id;
        private String title;
    }
}
