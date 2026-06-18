package com.example.bookverse.service.impl;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.bookverse.dto.response.RagSearchResult;
import com.example.bookverse.elasticsearch.BookDocument;
import com.example.bookverse.service.BookEmbeddingService;
import com.example.bookverse.service.RagRetrievalService;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Service
public class RagRetrievalServiceImpl implements RagRetrievalService {
    private static final String BOOKS_INDEX = "books";
    private static final int RRF_K = 60;
    private static final int SEARCH_SIZE = 10;
    private final ElasticsearchClient elasticsearchClient;
    private final BookEmbeddingService bookEmbeddingService;

    public RagRetrievalServiceImpl(
            ElasticsearchClient elasticsearchClient,
            BookEmbeddingService bookEmbeddingService) {
        this.elasticsearchClient = elasticsearchClient;
        this.bookEmbeddingService = bookEmbeddingService;
    }

    @Override
    public List<RagSearchResult> retrieve(String question, int topK) {
        if (question == null || question.isBlank()) {
            return List.of();
        }
        try {
            List<Hit<BookDocument>> bm25Hits = searchByKeyword(question);
            List<Float> queryEmbedding = bookEmbeddingService.embed(question);
            List<Hit<BookDocument>> vectorHits = searchByVector(queryEmbedding);
            return mergeByRrf(bm25Hits, vectorHits, topK);
        } catch (IOException e) {
            throw new RuntimeException("Không thể truy xuất dữ liệu RAG từ Elasticsearch", e);
        }
    }

    private List<Hit<BookDocument>> searchByKeyword(String question) throws IOException {
        SearchResponse<BookDocument> response = elasticsearchClient.search(s -> s
                .index(BOOKS_INDEX)
                .size(SEARCH_SIZE)
                .source(src -> src.filter(f -> f.includes("id", "title", "ragContent")))
                .query(q -> q.multiMatch(mm -> mm
                        .query(question)
                        .fields("title^4", "authors^3", "category^2", "publisher", "supplier", "description",
                                "ragContent^2")
                        .type(TextQueryType.BestFields))),
                BookDocument.class);
        return response.hits().hits();
    }

    private List<Hit<BookDocument>> searchByVector(List<Float> queryEmbedding) throws IOException {
        if (queryEmbedding == null || queryEmbedding.isEmpty()) {
            return List.of();
        }
        SearchResponse<BookDocument> response = elasticsearchClient.search(s -> s
                .index(BOOKS_INDEX)
                .size(SEARCH_SIZE)
                .source(src -> src.filter(f -> f.includes("id", "title", "ragContent")))
                .knn(k -> k
                        .field("embedding")
                        .queryVector(queryEmbedding)
                        .k(SEARCH_SIZE)
                        .numCandidates(50)),
                BookDocument.class);
        return response.hits().hits();
    }

    private List<RagSearchResult> mergeByRrf(
            List<Hit<BookDocument>> bm25Hits,
            List<Hit<BookDocument>> vectorHits,
            int topK) {
        Map<String, RankedDocument> merged = new LinkedHashMap<>();
        applyRrfScore(merged, bm25Hits);
        applyRrfScore(merged, vectorHits);
        return merged.values().stream()
                .sorted(Comparator.comparingDouble(RankedDocument::score).reversed())
                .limit(topK)
                .map(item -> {
                    BookDocument doc = item.document();
                    return new RagSearchResult(
                            doc.getId(),
                            doc.getTitle(),
                            doc.getRagContent(),
                            item.score());
                })
                .toList();
    }

    private void applyRrfScore(Map<String, RankedDocument> merged, List<Hit<BookDocument>> hits) {
        for (int i = 0; i < hits.size(); i++) {
            Hit<BookDocument> hit = hits.get(i);
            BookDocument doc = hit.source();
            if (doc == null || doc.getId() == null) {
                continue;
            }
            double score = 1.0 / (RRF_K + i + 1);
            merged.compute(doc.getId(), (id, existing) -> {
                if (existing == null) {
                    return new RankedDocument(doc, score);
                }
                return new RankedDocument(existing.document(), existing.score() + score);
            });
        }
    }

    private record RankedDocument(BookDocument document, double score) {
    }
}
