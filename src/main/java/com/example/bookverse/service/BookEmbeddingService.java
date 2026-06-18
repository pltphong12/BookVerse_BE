package com.example.bookverse.service;

import java.util.List;

public interface BookEmbeddingService {
    List<Float> embed(String text);
}
