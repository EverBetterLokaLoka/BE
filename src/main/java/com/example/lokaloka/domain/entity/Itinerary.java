package com.example.lokaloka.domain.entity;

import com.example.lokaloka.domain.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(name = "itineraries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    Double price;
    Timestamp created_at;
    Timestamp updated_at;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Khóa ngoại ánh xạ đến bảng "users"
    User user;

    Status status;
}