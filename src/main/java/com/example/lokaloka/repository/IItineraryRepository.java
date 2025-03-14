package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Itinerary;
import com.example.lokaloka.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface IItineraryRepository  extends JpaRepository<Itinerary, Long> {

    @Query("SELECT i FROM Itinerary i WHERE i.user = :user AND i.isDestroyed = false ")
    List<Itinerary> findActiveItinerariesByUser(@Param("user") User user);


    @Transactional
    @Modifying
    @Query("UPDATE Itinerary i SET i.status = 1 WHERE i.id = :id")
    void updateStatus(@Param("id") Long id);


    // 🔥 Kiểm tra xem tiêu đề có tồn tại với user không
    boolean existsByTitleAndUser(String title, User user);

    @Query("SELECT i FROM Itinerary i WHERE i.id = :id AND i.isDestroyed = false ")
    Optional<Itinerary> findActiveItineraryById(@Param("id") Long id);

}
