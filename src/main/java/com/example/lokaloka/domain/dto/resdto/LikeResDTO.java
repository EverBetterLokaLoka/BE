package com.example.lokaloka.domain.dto.resdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeResDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private Timestamp createdAt;
}
