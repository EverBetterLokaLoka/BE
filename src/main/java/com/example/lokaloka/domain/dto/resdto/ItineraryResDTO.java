package com.example.lokaloka.domain.dto.resdto;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các giá trị null khi serialize
public class ItineraryResDTO {
    private Long id;
    private String title;
    private String description;
}
