package com.example.lokaloka.domain.dto.reqdto;

import com.example.lokaloka.domain.entity.User;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class PostReqDTO {
    private Long id;
    private String title;
    private String content;
    private User user;
    private boolean isDestroyed;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}