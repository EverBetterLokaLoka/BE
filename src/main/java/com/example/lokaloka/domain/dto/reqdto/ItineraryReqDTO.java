package com.example.lokaloka.domain.dto.reqdto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ItineraryReqDTO {
    private String title;
    private String description;
    private Double price;
    private List<ActivityReqDTO> activities;
}
