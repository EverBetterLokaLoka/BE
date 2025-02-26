package com.example.lokaloka.domain.dto.reqdto;

import com.example.lokaloka.domain.enumeration.EGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserReqDTO {
    @NotBlank(message = "Please enter your email.")
    @Email(message = "Please enter a valid email address.")
    private String email;
    @NotBlank(message = "Please enter your password.")
    private String password;
    @NotBlank(message = "Please enter your confirm password.")
    private String passwordConfirm;
    @NotBlank(message = "Please enter your full name.")
    private String fullName;
    private String address;
    private String phone;
    private EGender gender;
    private LocalDate dob;
    private String idToken;
    private LocalDateTime updatedAt;

    private boolean isActive; // Giá trị mặc định có thể là true khi người dùng đăng ký
}
