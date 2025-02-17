package com.example.lokaloka.domain.dto.resdto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationResDTO {
    Long id;
    String name;
    String description;
    boolean flag;
    String coordinate_x;
    String coordinate_y;
    String time_reminder;
    Timestamp time_start;
    Timestamp time_finish;
    String culture;
    String recommended_time;
    Double price;
    Long itineraryId;
    List<ActivityResDTO> activities;
}
