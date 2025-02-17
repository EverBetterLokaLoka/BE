package com.example.lokaloka.domain.dto.resdto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityResDTO {
    Long id;
    String name;
    String description;
    Long locationId;
    String activities_possible;
    Double price;
    String rule;
    String recommend;
}
