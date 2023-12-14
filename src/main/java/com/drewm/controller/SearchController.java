package com.drewm.controller;

import com.drewm.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<?> search(@RequestParam String query, Authentication authentication, @RequestParam(required = false, defaultValue = "5") Integer limit) {
        return ResponseEntity.ok(searchService.search(query, authentication, limit));
    }
}
