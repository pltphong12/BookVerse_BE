package com.example.bookverse.dto.response;

public record RagSearchResult(
    String bookId,
    String title,
    String ragContent,
    double score
){}