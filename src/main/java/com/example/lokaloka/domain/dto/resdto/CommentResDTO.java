package com.example.lokaloka.domain.dto.resdto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResDTO {

    private Long id;
    private String content;
    private Long postId; // Post ID mà comment này liên kết tới
    private boolean isDestroyed;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
