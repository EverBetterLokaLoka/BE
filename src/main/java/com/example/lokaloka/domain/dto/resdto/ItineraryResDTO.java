package com.example.lokaloka.domain.dto.resdto;

import com.example.lokaloka.domain.enumeration.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItineraryResDTO {
    Long id;
    String title;
    String description;
    Double price;
    Timestamp created_at;
    Timestamp updated_at;
    Long userId;
    String userName;
    int status;
    List<LocationResDTO> locations;
}