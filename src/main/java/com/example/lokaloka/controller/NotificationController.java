package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.reqdto.NotificationReqDTO;
import com.example.lokaloka.domain.dto.resdto.NotificationResDTO;
import com.example.lokaloka.service.impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResDTO> createNotification(@RequestBody NotificationReqDTO requestDTO) {
        NotificationResDTO responseDTO = notificationService.createNotification(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{userId}/{foreignId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long userId, @PathVariable Long foreignId) {
        notificationService.deleteNotification(userId, foreignId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResDTO>> getNotifications(@PathVariable Long userId) {
        List<NotificationResDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
}