package com.example.lokaloka.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase/firebase-service-account.json")) {

            if (serviceAccount == null) {
                throw new RuntimeException("Không tìm thấy file firebase-service-account.json, kiểm tra lại!");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi khởi tạo Firebase", e);
        }
    }
}
