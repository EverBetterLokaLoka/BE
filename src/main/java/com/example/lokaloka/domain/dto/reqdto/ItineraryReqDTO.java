package com.example.lokaloka.domain.dto.reqdto;

import com.example.lokaloka.domain.enumeration.Status;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class ItineraryReqDTO {
    Long id;
    String title;
    String description;
    Double price;
    Timestamp created_at;
    Timestamp updated_at;
    Long userId;
    String userName;
    Status status;
    List<LocationReqDTO> locations;
}
