package com.example.lokaloka.domain.dto.reqdto;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationReqDTO {
    String name;
    String description;
    boolean flag;
    String coordinate_x;
    String coordinate_y;
    String time_reminder;
    String time_start;
    String time_finish;
    String culture;
    String recommended_time;
    Double price;
    List<ActivityReqDTO> activities;
}
