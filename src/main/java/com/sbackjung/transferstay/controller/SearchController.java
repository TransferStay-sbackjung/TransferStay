package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;

    @RequestMapping ("/search")
    public void search(){
        searchService.search();
    }
}
