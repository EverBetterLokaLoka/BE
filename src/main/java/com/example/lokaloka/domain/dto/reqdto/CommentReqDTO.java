package com.example.lokaloka.domain.dto.reqdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReqDTO {
    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private String userEmail;
    private String userName;
    private boolean isDestroyed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String avatar;
}