package com.example.lokaloka.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Timestamp expiryTime;

    public void setExpiryTime() {
        this.expiryTime = Timestamp.from(Instant.now().plus(1, ChronoUnit.HOURS));
    }
}
