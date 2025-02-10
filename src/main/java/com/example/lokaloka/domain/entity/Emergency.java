package com.example.lokaloka.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(name = "emergencies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Emergency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contact_name;
    private String contact_phone;
    private String relationship;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    Timestamp created_at;
    Timestamp updated_at;
}
