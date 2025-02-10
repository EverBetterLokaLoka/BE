package com.example.lokaloka.controller;


import com.example.lokaloka.uploader.UploaderConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploaderController {

    private final UploaderConfig uploaderConfig;

    public UploaderController(UploaderConfig uploaderConfig) {
        this.uploaderConfig = uploaderConfig;
    }

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = uploaderConfig.uploadFile(file);
            return ResponseEntity.ok().body(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }
}

