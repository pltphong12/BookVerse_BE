package com.example.bookverse.controller;

import com.example.bookverse.dto.response.ResFileDTO;
import com.example.bookverse.exception.file.StogareException;
import com.example.bookverse.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {
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
        if (file == null || file.isEmpty()) {
            throw new StogareException("File is empty");
        }
        String finalName = this.fileService.store(file, folder);
        return ResponseEntity.ok().body(new ResFileDTO(finalName, Instant.now()));
    }

    /**
     * Upload nhiều file một lần. Form-data: cùng tên part {@code files} (input multiple) + {@code folder}.
     * Ví dụ relativePath cho sách: {@code folder + "/" + fileName} giống upload đơn.
     */
    @PostMapping("/files/batch")
    @PreAuthorize("hasAuthority('FILE_UPLOAD')")
    public ResponseEntity<List<ResFileDTO>> uploadBatch(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, StogareException {
        List<MultipartFile> nonNull = files == null ? List.of() : files.stream().filter(f -> f != null).toList();
        List<String> names = this.fileService.storeMultiple(nonNull, folder);
        Instant uploadedAt = Instant.now();
        List<ResFileDTO> result = names.stream().map(n -> new ResFileDTO(n, uploadedAt)).toList();
        return ResponseEntity.ok(result);
    }
}
