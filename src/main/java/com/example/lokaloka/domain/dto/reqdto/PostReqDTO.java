package com.example.lokaloka.domain.dto.reqdto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReqDTO {
    private Long id;
    private String title;
    private String content;
    private Long user_id;
    private String userEmail;
    private String userName;
    private boolean isDestroyed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentReqDTO> comments;
    private List<LikeReqDTO> likes;
    private List<ImageReqDTO> images;
    private int likeCount;
    private int commentCount;
    private String avatar;
    private List<Long> deleteImageIds;
}