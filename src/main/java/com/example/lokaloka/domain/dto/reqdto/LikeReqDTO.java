package com.example.lokaloka.domain.dto.reqdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeReqDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String userEmail;
    private LocalDateTime createdAt;
}