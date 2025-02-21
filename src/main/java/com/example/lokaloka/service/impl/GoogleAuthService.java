package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.LoginReqDTO;
import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.domain.enumeration.EGender;
import com.example.lokaloka.repository.IUserRepository;
import com.example.lokaloka.util.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class GoogleAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    public ResponseEntity<?> verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                Optional<User> existingUser = userRepository.findByEmail(email);
                User user;

                if (existingUser.isPresent()) {
                    user = existingUser.get();
                } else {
                    user = User.builder()
                            .email(email)
                            .full_name(name)
                            .is_active(true)
                            .build();
                    user = userRepository.save(user);
                }

                String jwtToken = jwtTokenUtil.generateToken(user.getEmail(), user.getFull_name());

                return ResponseEntity.ok(
                        ResponseData.builder()
                                .code(SuccessCode.GET_SUCCESS.getCode())
                                .message("Authentication successful")
                                .data(user)
                                .token(jwtToken)
                                .build()
                );
            } else {
                return ResponseEntity.badRequest().body("Invalid ID token.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error verifying Google token: " + e.getMessage());
        }
    }

    public ResponseEntity<?> registerUserWithGoogle(UserReqDTO userReqDTO) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(userReqDTO.getEmail());

            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .code(ErrorCode.CREATE_USER_FAILED.getCode())
                                .message("User already exists with email: " + userReqDTO.getEmail())
                                .build()
                );
            }

            User newUser = User.builder()
                    .email(userReqDTO.getEmail())
                    .full_name(userReqDTO.getFullName())
                    .address(userReqDTO.getAddress())
                    .phone(userReqDTO.getPhone())
                    .gender(userReqDTO.getGender() != null ? userReqDTO.getGender() : EGender.OTHER)
                    .dob(userReqDTO.getDob())
                    .is_active(true)
                    .created_at(Timestamp.valueOf(LocalDateTime.now()))
                    .password(passwordEncoder.encode(userReqDTO.getPassword()))
                    .build();

            userRepository.save(newUser);

            String token = jwtTokenUtil.generateToken(newUser.getEmail(), newUser.getFull_name());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseData.builder()
                            .code(SuccessCode.CREATED.getCode())
                            .message("User registered successfully")
                            .data(newUser)
                            .token(token)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .code(ErrorCode.CREATE_USER_FAILED.getCode())
                            .message("Registration failed due to an error: " + e.getMessage())
                            .build()
            );
        }
    }

    public ResponseEntity<?> loginUser(LoginReqDTO loginRequest) {
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponse.builder()
                                .code(ErrorCode.ERROR_EMAIL.getCode())
                                .message("Email is incorrect")
                                .build()
                );
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.builder()
                                .code(ErrorCode.ERROR_PASSWORD.getCode())
                                .message("Password is incorrect")
                                .build()
                );
            }

            String token = jwtTokenUtil.generateToken(user.getEmail(), user.getFull_name());

            return ResponseEntity.ok(
                    ResponseData.builder()
                            .code(SuccessCode.GET_SUCCESS.getCode())
                            .message("Login successful!")
                            .data(user)
                            .token(token)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .code(ErrorCode.LOGIN_FAILED.getCode())
                            .message("Login failed due to an error: " + e.getMessage())
                            .build()
            );
        }
    }
}