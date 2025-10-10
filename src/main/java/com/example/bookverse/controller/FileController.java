package com.example.bookverse.controller;

import com.example.bookverse.domain.response.file.ResFileDTO;
import com.example.bookverse.exception.file.StogareException;
import com.example.bookverse.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    @Value("${bookverse.upload-file.base-uri}")
    private String baseUri;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @PreAuthorize("hasAuthority('FILE_UPLOAD')")
    public ResponseEntity<ResFileDTO> upload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, StogareException {
        // file is empty
        if (file == null || file.isEmpty()) {
            throw new StogareException("File is empty");
        }
        // validate extension
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "png", "doc", "docx");
        boolean isValidExtension = allowedExtensions.stream().anyMatch(extension -> fileName.toLowerCase().endsWith(extension));
        if (!isValidExtension) {
            throw new StogareException("Invalid extension");
        }
        // create a directory
        this.fileService.createUploadFolder(baseUri + folder);
        // store a file
        String finalName = this.fileService.store(file, folder);
        ResFileDTO uploadFileDTO = new ResFileDTO(finalName, Instant.now());
        return ResponseEntity.ok().body(uploadFileDTO);
    }
}
