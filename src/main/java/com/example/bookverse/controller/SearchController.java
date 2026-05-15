package com.example.bookverse.controller;

import com.example.bookverse.dto.criteria.CriteriaFilterProduct;
import com.example.bookverse.dto.response.ResPagination;
import com.example.bookverse.dto.response.SearchAutocompleteResponse;
import com.example.bookverse.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/autocomplete")
    public ResponseEntity<SearchAutocompleteResponse> autocomplete(@RequestParam String query) {
        SearchAutocompleteResponse res = this.searchService.autocomplete(query);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/products")
    public ResponseEntity<ResPagination> getAllBooksWithPaginationAndFilterProduct(
        @ModelAttribute CriteriaFilterProduct criteriaFilterProduct,
        Pageable pageable) throws Exception {
        ResPagination resPagination = this.searchService.searchAllBooksWithPaginationAndFilter(criteriaFilterProduct,
            pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }
}
