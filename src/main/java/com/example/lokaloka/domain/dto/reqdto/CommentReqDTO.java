package com.example.lokaloka.domain.dto.reqdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReqDTO {
    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private boolean isDestroyed;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
