package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Itinerary;
import com.example.lokaloka.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IItineraryRepository  extends JpaRepository<Itinerary, Long> {

    List<Itinerary> findByUser(User user);

    @Transactional
    @Modifying
    @Query("UPDATE Itinerary i SET i.status = 1 WHERE i.id = :id")
    void updateStatus(@Param("id") Long id);

}
