package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.service.IUserService;
import com.example.lokaloka.util.ResponseData;
import com.example.lokaloka.util.SuccessCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(userService.getUserById(id))
                        .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<?>> updateUser(@PathVariable Long id, @RequestBody UserReqDTO user) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.UPDATED.getCode())
                        .message(SuccessCode.UPDATED.getMessage())
                        .data(userService.updateUser(id,user))
                        .build());
    }
}