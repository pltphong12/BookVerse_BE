package com.example.bookverse.service;

import com.example.bookverse.dto.criteria.CriteriaFilterProduct;
import com.example.bookverse.dto.record.SearchAutocompleteResponse;
import com.example.bookverse.dto.response.ResPagination;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    SearchAutocompleteResponse autocomplete(String prefix);

    ResPagination searchAllBooksWithPaginationAndFilter(CriteriaFilterProduct criteriaFilterProduct, Pageable pageable) throws Exception;
}
