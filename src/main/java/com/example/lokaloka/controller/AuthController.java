package com.example.lokaloka.controller;

import com.example.lokaloka.service.impl.FirebaseAuthService;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> loginWithGoogle(@RequestHeader("Authorization") String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            return ResponseEntity.ok("Login thành công! UID: " + uid + ", Email: " + email);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
    }
}
