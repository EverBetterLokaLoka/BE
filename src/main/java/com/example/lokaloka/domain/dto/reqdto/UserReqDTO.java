package com.example.lokaloka.domain.dto.reqdto;

import com.example.lokaloka.domain.enumeration.EGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserReqDTO {
    @NotBlank(message = "Email not empty")
    private String email;
    @NotBlank(message = "Password not empty")
    private String password;
    @NotBlank(message = "FullName not empty")
    private String fullName;
    private String address;
    private String phone;
    private EGender gender;
    private LocalDate dob;
    private String idToken;
    private LocalDateTime updatedAt;

    private boolean isActive; // Giá trị mặc định có thể là true khi người dùng đăng ký
}
