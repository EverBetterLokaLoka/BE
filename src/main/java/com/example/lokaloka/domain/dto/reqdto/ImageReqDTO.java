package com.example.lokaloka.domain.dto.reqdto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageReqDTO {
    private Long id;
    private String content;
    private Integer shares;
    private Long locationId;
    private Long userId;
    private Long postId;
    private Long mapId;
    private Long activityId;
    private String userEmail;
    private String type;
    private LocalDateTime created_at;
    private LocalDateTime  updated_at;
}