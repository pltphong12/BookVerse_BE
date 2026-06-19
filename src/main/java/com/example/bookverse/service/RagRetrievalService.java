package com.example.bookverse.service;

import java.util.List;

import com.example.bookverse.dto.record.RagSearchResult;

public interface RagRetrievalService {
    List<RagSearchResult> retrieve(String question, int topK);
}
