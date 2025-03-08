package com.example.lokaloka.domain.dto.reqdto;

import com.example.lokaloka.domain.enumeration.EGender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileReqDTO {
    @Pattern(regexp = "^(?!\\s*$).+", message = "Please enter your full name.")
    private String full_name; // Không cho phép toàn dấu cách

    private  String email;

    @Pattern(regexp = "^[^\\s]+$", message = "Password must not contain spaces.")
    private String password;

    private String address;

    @Pattern(regexp = "^[0-9]+$", message = "The phone number can only contain digits.")
    @Size(min = 10, max = 10, message = "Invalid phone number.")
    private String phone;

    private EGender gender;

    private LocalDate dob;
    @Pattern(regexp = "^(|\\d{10})$", message = "Invalid emergency number.")
    private String emergency_numbers;

    private LocalDateTime updatedAt;

}
