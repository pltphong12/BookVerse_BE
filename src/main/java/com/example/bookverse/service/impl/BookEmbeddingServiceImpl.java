package com.example.bookverse.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bookverse.service.BookEmbeddingService;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;

@Service
public class BookEmbeddingServiceImpl implements BookEmbeddingService {
    private final EmbeddingModel embeddingModel;

    public BookEmbeddingServiceImpl(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Override
    public List<Float> embed(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        Response<Embedding> response = embeddingModel.embed(text);
        return response.content().vectorAsList();
    }
}
