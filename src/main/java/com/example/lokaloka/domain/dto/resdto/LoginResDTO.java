package com.example.lokaloka.domain.dto.resdto;

import com.example.lokaloka.domain.enumeration.EGender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class LoginResDTO {
    private Long id;
    private String email;
    private String full_name;
    private String address;
    private String phone;
    private EGender gender;
    private LocalDate dob;
    private boolean is_active;
    private Timestamp created_at;
    private Timestamp updated_at;
}
