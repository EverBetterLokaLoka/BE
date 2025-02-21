package com.example.lokaloka.domain.dto.resdto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResDTO {
    private Long id;
    private String email;
    private String fullName;
    private String address;
    private String phone;
    private LocalDate dob;
    private boolean isActive;
    private List<ItineraryResDTO> itineraries;
}
