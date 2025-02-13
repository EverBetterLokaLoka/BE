package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Itinerary;
import com.example.lokaloka.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IItineraryRepository  extends JpaRepository<Itinerary, Long> {

    List<Itinerary> findByUser(User user);
}
