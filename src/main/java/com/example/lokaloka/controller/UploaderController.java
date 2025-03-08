package com.example.lokaloka.controller;


import com.example.lokaloka.domain.dto.reqdto.ImageReqDTO;
import com.example.lokaloka.domain.entity.Image;
import com.example.lokaloka.service.impl.ImageService;
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
    private final ImageService imageService;

    public UploaderController(UploaderConfig uploaderConfig, ImageService imageService) {
        this.uploaderConfig = uploaderConfig;
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = uploaderConfig.uploadFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                    .success(true)
                    .status(HttpStatus.CREATED.value())
                    .message("Image uploaded successfully")
                    .data(imageUrl)
                    .build()
            );
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> uploadImage(@RequestBody ImageReqDTO requestDTO) {
        Image savedImage = imageService.saveImage(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Image uploaded successfully")
                .data(savedImage)
                .build()
        );
    }
}

