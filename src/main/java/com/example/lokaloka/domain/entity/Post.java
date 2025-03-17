package com.example.lokaloka.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.core.util.Json;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.*;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Lob
    private String content;
    private boolean is_destroyed;

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX", timezone = "Asia/Bangkok")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX", timezone = "Asia/Bangkok")
    private LocalDateTime  updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @Lob
    private String image_url;

    @Column(name = "schedule_list", columnDefinition = "TEXT") // hoặc một kiểu phù hợp khác
    private String scheduleList;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(ZoneId.of("Asia/Bangkok"));
        }
        System.out.println("CreatedAt before saving: " + createdAt); // Debug
    }


    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now(ZoneOffset.ofHours(7));
    }
}