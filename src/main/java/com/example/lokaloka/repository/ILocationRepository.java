package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByItineraryId(Long itineraryId);
}