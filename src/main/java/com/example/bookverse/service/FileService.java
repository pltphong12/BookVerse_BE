package com.example.bookverse.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;

public interface FileService {
    // Create folder store file
    void createUploadFolder(String folder) throws URISyntaxException;
    // Store
    String store(MultipartFile file, String folder) throws URISyntaxException;
}
