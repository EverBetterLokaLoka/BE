package com.example.lokaloka.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private boolean is_destroyed;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    Timestamp created_at;
    Timestamp updated_at;
}