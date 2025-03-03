package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.reqdto.LoginReqDTO;
import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.entity.BlacklistedToken;
import com.example.lokaloka.repository.IBlacklistedTokenRepository;
import com.example.lokaloka.service.impl.GoogleAuthService;
import com.example.lokaloka.util.ApiResponse;
import com.example.lokaloka.util.SuccessCode;
import com.google.cloud.Timestamp;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthRestController {

    private final GoogleAuthService googleAuthService;
    private final IBlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    public AuthRestController(GoogleAuthService googleAuthService, IBlacklistedTokenRepository blacklistedTokenRepository) {
        this.googleAuthService = googleAuthService;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerWithGoogle(@Valid @RequestBody UserReqDTO userReqDTO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        return googleAuthService.registerUserWithGoogle(userReqDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDTO loginRequest) {
        return googleAuthService.loginUser(loginRequest);
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody String idToken) {
        return googleAuthService.verifyGoogleToken(idToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("Logging out token: {}", token);

            if (!blacklistedTokenRepository.existsByToken(token)) {
                BlacklistedToken blacklistedToken = new BlacklistedToken();
                blacklistedToken.setToken(token);
                blacklistedToken.setExpiryTime();

                blacklistedTokenRepository.save(blacklistedToken);
                log.info("Token successfully blacklisted");
                return ResponseEntity.status(SuccessCode.SUCCESS.getCode()).body(
                        ApiResponse.builder()
                                .success(false)
                                .status(SuccessCode.SUCCESS.getCode())
                                .message("Sign Out successfully")
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.builder()
                                .success(false)
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Token already invalidated")
                                .build()
                );
            }
        }
        log.error("Invalid token format in logout request");
        return ResponseEntity.badRequest().body("Invalid token format");
    }
}