package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.domain.AssignmentPost;
import com.sbackjung.transferstay.dto.SearchResponse;
import com.sbackjung.transferstay.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public List<SearchResponse.getSearchResponse> getSearchResult(
            String freeField, LocalDate checkInDate, LocalDate checkOutDate, int personnel, String locationDepth1, String locationDepth2) {

        // 검색 조건으로 db 에서 일치하는 post data 검색 (searchRepository) 후 -> 결과를 AssignmentPost 리스트 형태로 가져옴
        List<AssignmentPost> posts = searchRepository.searchPosts(freeField, checkInDate, checkOutDate, personnel, locationDepth1, locationDepth2);

        // 가져온 posts 들을 stream 을 통해서 필요한 값만 Response 에 할당해서 result list 생성
        return posts.stream()
                .map(post -> new SearchResponse.getSearchResponse(post.getId(), post.getTitle(), post.getPrice()))
                .collect(Collectors.toList());

    }
}
