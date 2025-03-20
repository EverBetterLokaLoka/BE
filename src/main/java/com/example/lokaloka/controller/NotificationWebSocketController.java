package com.example.lokaloka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Gửi thông báo đến tất cả người dùng
    @MessageMapping("/broadcast")
    @SendTo("/topic/friendship")
    public String broadcastMessage(String message) {
        return message;
    }

    // Gửi thông báo đến một người dùng cụ thể
    public void sendNotificationToUser(Long userId, String message) {
        System.out.println("Gửi thông báo đến người dùng " + userId + ": " + message);
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/notification",
                message
        );
    }
}

