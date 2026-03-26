package com.example.bookverse.service.impl;

import com.example.bookverse.exception.file.StogareException;
import com.example.bookverse.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class FileServiceImpl implements FileService {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "doc", "docx");

    @Value("${bookverse.upload-file.base-uri}")
    private String baseUri;

    @Override
    public void createUploadFolder(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    @Override
    public String store(MultipartFile file, String folder) throws URISyntaxException, StogareException {
        validateFile(file);
        createUploadFolder(baseUri + folder);
        String storedFileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        copyToDisk(file, folder, storedFileName);
        return storedFileName;
    }

    @Override
    public List<String> storeMultiple(List<MultipartFile> files, String folder)
            throws URISyntaxException, StogareException {
        if (files == null || files.isEmpty()) {
            throw new StogareException("No files uploaded");
        }
        createUploadFolder(baseUri + folder);
        long batchId = System.currentTimeMillis();
        List<String> storedNames = new ArrayList<>();
        int index = 0;
        for (MultipartFile file : files) {
            validateFile(file);
            String storedFileName = batchId + "-" + index + "-" + file.getOriginalFilename();
            copyToDisk(file, folder, storedFileName);
            storedNames.add(storedFileName);
            index++;
        }
        return storedNames;
    }

    private void validateFile(MultipartFile file) throws StogareException {
        if (file == null || file.isEmpty()) {
            throw new StogareException("File is empty");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            throw new StogareException("File name is invalid");
        }
        String lower = fileName.toLowerCase(Locale.ROOT);
        boolean allowed = ALLOWED_EXTENSIONS.stream().anyMatch(lower::endsWith);
        if (!allowed) {
            throw new StogareException("Invalid extension");
        }
    }

    private void copyToDisk(MultipartFile file, String folder, String storedFileName) throws URISyntaxException {
        URI uri = new URI(baseUri + folder + "/" + storedFileName);
        Path path = Paths.get(uri);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
