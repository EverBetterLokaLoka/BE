package com.example.lokaloka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestNotificationController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public TestNotificationController(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/send-notification/{userId}")
    public ResponseEntity<String> sendTestNotification(@PathVariable String userId) {
        try {
            // Create a test notification
            Map<String, Object> notification = new HashMap<>();
            notification.put("id", System.currentTimeMillis());
            notification.put("title", "Test Notification");
            notification.put("body", "This is a test notification from the server at " + new Date());
            notification.put("createdAt", new Date().toInstant().toString());
            notification.put("isRead", false);
            notification.put("senderId", 0);
            notification.put("type", "SYSTEM");

            // Convert to JSON
            String jsonNotification = objectMapper.writeValueAsString(notification);

            // Send to the topic destination
            messagingTemplate.convertAndSend("/topic/notifications/" + userId, jsonNotification);

            return ResponseEntity.ok("Test notification sent to user " + userId + " on /topic/notifications/" + userId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending notification: " + e.getMessage());
        }
    }
}