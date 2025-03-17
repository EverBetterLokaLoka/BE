package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.NotificationReqDTO;
import com.example.lokaloka.domain.dto.resdto.NotificationResDTO;
import com.example.lokaloka.domain.entity.Notification;
import com.example.lokaloka.repository.INotificationRepository;
import com.example.lokaloka.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private INotificationRepository notificationRepository;

    @Autowired
    private IUserRepository userRepository;

    public NotificationResDTO createNotification(NotificationReqDTO requestDTO) {
        Notification notification = new Notification();
        notification.setUser(userRepository.findById(requestDTO.getUserId()).orElse(null));
        notification.setDescription(requestDTO.getDescription());
        notification.setStatus(false);
        notification.setCreated_at(new Timestamp(System.currentTimeMillis()));
        notification.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        notification.setForeign_id(requestDTO.getForeignId());
        Notification savedNotification = notificationRepository.save(notification);

        return convertToDTO(savedNotification);
    }

    public void deleteNotification(Long userId, Long foreignId) {
        notificationRepository.deleteByUserIdAndForeignId(userId, foreignId);
    }

    public List<NotificationResDTO> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private NotificationResDTO convertToDTO(Notification notification) {
        ZoneId zoneId = ZoneId.of("Asia/Bangkok"); // Múi giờ Việt Nam
        return new NotificationResDTO(
                notification.getId(),
                notification.getUser().getId(),
                notification.getDescription(),
                notification.isStatus(),
                notification.getCreated_at().toInstant().atZone(zoneId).toLocalDateTime(),
                notification.getUpdated_at().toInstant().atZone(zoneId).toLocalDateTime(),
                notification.getForeign_id()
        );
    }

}