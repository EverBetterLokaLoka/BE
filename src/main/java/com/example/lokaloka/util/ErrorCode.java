package com.example.lokaloka.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_NOT_FOUND(404, "User Not Found", HttpStatus.NOT_FOUND),
    ERROR_EMAIL(4001, "Email Not Found", HttpStatus.NOT_FOUND),
    ERROR_PASSWORD(4002, "Password is incorrect", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "Login failed", HttpStatus.UNAUTHORIZED),
    CREATE_USER_FAILED(400,"User created failed", HttpStatus.BAD_REQUEST);
    int code;
    String message;
    HttpStatus httpStatus;
}
