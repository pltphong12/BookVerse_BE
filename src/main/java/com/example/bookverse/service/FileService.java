package com.example.bookverse.service;

import com.example.bookverse.exception.file.StogareException;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.List;

public interface FileService {
    void createUploadFolder(String folder) throws URISyntaxException;

    String store(MultipartFile file, String folder) throws URISyntaxException, StogareException;

    /**
     * Lưu nhiều file vào cùng {@code folder}. Tên file lưu dạng {@code {batchMillis}-{index}-{tên gốc}} để tránh trùng.
     */
    List<String> storeMultiple(List<MultipartFile> files, String folder) throws URISyntaxException, StogareException;
}
