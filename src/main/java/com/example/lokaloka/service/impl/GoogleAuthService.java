package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.LoginReqDTO;
import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.dto.resdto.LoginResDTO;
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
import java.util.regex.Pattern;

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

                String jwtToken = jwtTokenUtil.generateToken(user.getEmail(), user.getFull_name(),user.getId().toString());

                return ResponseEntity.ok(
                        ResponseData.builder()
                                .status(SuccessCode.GET_SUCCESS.getCode())
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
            // 🔥 Kiểm tra password và confirm_password có khớp không
            if (!userReqDTO.getPassword().equals(userReqDTO.getConfirm_password())) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .success(false)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Passwords do not match. Please try again.")
                                .build()
                );
            }

            // 🔥 Kiểm tra email đã tồn tại chưa
            Optional<User> existingUser = userRepository.findByEmail(userReqDTO.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        ApiResponse.builder()
                                .success(false)
                                .status(HttpStatus.CONFLICT.value())
                                .message("An account already exists with the same email address.")
                                .build()
                );
            }

            // 🔥 Tạo user mới
            User newUser = User.builder()
                    .email(userReqDTO.getEmail())
                    .full_name(userReqDTO.getFull_name())
                    .address(userReqDTO.getAddress())
                    .phone(userReqDTO.getPhone())
                    .gender(userReqDTO.getGender() != null ? userReqDTO.getGender() : EGender.OTHER)
                    .dob(userReqDTO.getDob())
                    .is_active(true)
                    .created_at(Timestamp.valueOf(LocalDateTime.now()))
                    .password(passwordEncoder.encode(userReqDTO.getPassword()))
                    .build();

            userRepository.save(newUser);

            // 🔥 Tạo token JWT và truyền id
            String token = jwtTokenUtil.generateToken(newUser.getEmail(), newUser.getFull_name(), newUser.getId().toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseData.builder()
                            .success(true)
                            .status(HttpStatus.CREATED.value())
                            .message("Sign Up successfully")
                            .data(newUser)
                            .token(token)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .status(ErrorCode.CREATE_USER_FAILED.getCode())
                            .message("Registration failed due to an error: " + e.getMessage())
                            .build()
            );
        }
    }


    public ResponseEntity<?> loginUser(LoginReqDTO loginRequest) {
        try {
            // Biểu thức chính quy kiểm tra email hợp lệ
            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            Pattern pattern = Pattern.compile(emailRegex);


            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElse(null);

            if(loginRequest.getEmail() == null && loginRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .status(ErrorCode.CREATE_USER_FAILED.getCode())
                                .success(false)
                                .message("Please enter your email and password.")
                                .build()
                );
            }

            if(loginRequest.getEmail() == null ) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .status(ErrorCode.CREATE_USER_FAILED.getCode())
                                .success(false)
                                .message("Please enter your email.")
                                .build()
                );
            }
            if (!pattern.matcher(loginRequest.getEmail()).matches()) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .success(false)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Please enter a valid email address.")
                                .build()
                );
            }
            if(loginRequest.getPassword() == null ) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .status(ErrorCode.CREATE_USER_FAILED.getCode())
                                .success(false)
                                .message("Please enter your password.")
                                .build()
                );
            }

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.builder()
                                .success(false)
                                .status(ErrorCode.UNAUTHORIZED.getCode())
                                .message("Invalid email or password. Please try again")
                                .build()
                );
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.builder()
                                .success(false)
                                .status(ErrorCode.UNAUTHORIZED.getCode())
                                .message("Invalid email or password. Please try again")
                                .build()
                );
            }
            // Convert the User entity to a UserDTO to exclude itineraries
            LoginResDTO userDTO = LoginResDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .full_name(user.getFull_name())
                    .address(user.getAddress())
                    .phone(user.getPhone())
                    .gender(user.getGender())
                    .dob(user.getDob())
                    .is_active(user.is_active())
                    .created_at(user.getCreated_at())
                    .updated_at(user.getUpdated_at())
                    .build();

            String token = jwtTokenUtil.generateToken(user.getEmail(), user.getFull_name(),user.getId().toString());

            return ResponseEntity.ok(
                    ResponseData.builder()
                            .success(true)
                            .status(HttpStatus.OK.value())
                            .message("Sign In successfully")
                            .data(userDTO)
                            .token(token)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .status(ErrorCode.UNAUTHORIZED.getCode())
                            .success(false)
                            .message("Login failed due to an error: " + e.getMessage())
                            .build()
            );
        }
    }
}