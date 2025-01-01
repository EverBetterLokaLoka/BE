package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItineraryRepository  extends JpaRepository<Itinerary, Long> {
}
