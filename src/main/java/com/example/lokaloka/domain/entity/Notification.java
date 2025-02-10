package com.example.lokaloka.domain.entity;
import com.example.lokaloka.domain.enumeration.Event;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    private String description;
    private boolean status;

    Event event;
    Timestamp created_at;
    Timestamp updated_at;
    boolean is_destroyed;

    private Long foreign_id;


}
