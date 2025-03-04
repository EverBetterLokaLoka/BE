package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.reqdto.ProfileReqDTO;
import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.service.IUserService;
import com.example.lokaloka.util.ApiResponse;
import com.example.lokaloka.util.ResponseData;
import com.example.lokaloka.util.SuccessCode;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserRestController {


    private IUserService userService;

    @GetMapping("/check-login")
    public String checkLogin(Authentication authentication ) {
        return userService.checkLoginStatus(authentication);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<?>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .success(true)
                        .status(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(userService.getUserById(id))
                        .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody ProfileReqDTO user, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(errorMessage)
                            .build());
        }

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("Profile updated successfully")
                        .data(userService.updateUser(id, user))
                        .build());
    }


}