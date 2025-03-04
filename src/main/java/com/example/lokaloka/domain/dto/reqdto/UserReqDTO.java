package com.example.lokaloka.domain.dto.reqdto;

import com.example.lokaloka.domain.enumeration.EGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserReqDTO {
    @NotBlank(message = "Please enter your email.")
    @Email(message = "Please enter a valid email address.")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Please enter a valid email address.") // Không cho phép chỉ có khoảng trắng
    private String email;
    @NotBlank(message = "Please enter your password.")
    @Pattern(regexp = "^[^\\s]+$", message = "Passwords do not match. Please try again.")
    private String password;
    @NotBlank(message = "Please enter your confirm password.")
    @Pattern(regexp = "^[^\\s]+$", message = "Passwords do not match. Please try again.")
    private String confirm_password;
    @NotBlank(message = "Please enter your full name.")
    private String full_name;
    private String address;
    private String phone;
    private EGender gender;
    private LocalDate dob;
    private String idToken;
    private LocalDateTime updatedAt;
    private boolean isActive;
    private int age;
    private String emergency_numbers;
}
