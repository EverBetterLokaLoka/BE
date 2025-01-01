package com.example.lokaloka.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
}
