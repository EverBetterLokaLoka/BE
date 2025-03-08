package com.example.lokaloka.domain.dto.resdto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResDTO {
    private Long id;
    private String email;

    private String full_name;
    private String password;
    private String address;
    private String phone;
    private String gender;
    private LocalDate dob;
    private String emergency_numbers;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private List<ItineraryResDTO> itineraries;
    private String avatar;

}
