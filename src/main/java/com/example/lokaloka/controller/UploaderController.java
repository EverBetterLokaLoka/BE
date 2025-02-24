package com.example.lokaloka.controller;


import com.example.lokaloka.uploader.UploaderConfig;
import com.example.lokaloka.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploaderController {

    private final UploaderConfig uploaderConfig;

    public UploaderController(UploaderConfig uploaderConfig) {
        this.uploaderConfig = uploaderConfig;
    }

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = uploaderConfig.uploadFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .code(HttpStatus.CREATED.value())
                    .message("Image uploaded successfully")
                    .data(imageUrl)
                    .build()
            );
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }
}

