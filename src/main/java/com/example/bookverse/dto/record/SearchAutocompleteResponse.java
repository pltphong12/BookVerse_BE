package com.example.bookverse.dto.record;

import java.util.List;

public record SearchAutocompleteResponse(List<String> suggestions, List<ProductHit> products) {
}