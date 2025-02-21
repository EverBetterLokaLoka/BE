package com.example.lokaloka.domain.dto.reqdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReqDTO {
    private String email;
    private String password;
}
