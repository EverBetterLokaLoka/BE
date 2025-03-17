package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user.id = :userId AND n.foreign_id = :foreignId")
    void deleteByUserIdAndForeignId(@Param("userId") Long userId, @Param("foreignId") Long foreignId);

}