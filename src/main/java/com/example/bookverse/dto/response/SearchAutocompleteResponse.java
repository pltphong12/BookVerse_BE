package com.example.bookverse.dto.response;

import java.util.List;

public record SearchAutocompleteResponse(List<String> suggestions, List<ProductHit> products) {
}
