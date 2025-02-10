package com.example.lokaloka.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(name = "images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private  int shares;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    @ManyToOne
    @JoinColumn(name = "map_id",nullable = false)
    Map map;

    @ManyToOne
    @JoinColumn(name = "activity_id",nullable = false)
    Activity activity;

    Timestamp  created_at;
    Timestamp  updated_at;

}

