package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.ImageReqDTO;
import com.example.lokaloka.domain.entity.Image;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.repository.IImageRepository;
import com.example.lokaloka.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Service
public class ImageService {

    private final IImageRepository imageRepository;
    private final IUserRepository userRepository;

    public ImageService(IImageRepository imageRepository, IUserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Image saveImage(ImageReqDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName(); // Assuming the email is stored as the principal

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Chỉ xóa ảnh có type là "avatar" của user
        imageRepository.deleteByUserIdAndType(user.getId(), "avatar");

        // Lưu ảnh mới
        Image newImage = Image.builder()
                .user(user)
                .content(requestDTO.getContent())
                .type("avatar")
                .created_at(LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Bangkok")))
                .updated_at(LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Bangkok")))
                .build();

        return imageRepository.save(newImage);
    }
}