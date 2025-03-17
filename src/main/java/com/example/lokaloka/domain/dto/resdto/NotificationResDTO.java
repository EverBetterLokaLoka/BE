package com.example.lokaloka.domain.dto.resdto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResDTO {
    private Long id;
    private Long userId;
    private String description;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long foreignId;
}
