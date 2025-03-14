package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ILocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByItineraryId(Long itineraryId);
//    @Query("SELECT l FROM Location l LEFT JOIN FETCH l.images WHERE l.itinerary.id = :itineraryId")
//    List<Location> findLocationsWithImagesByItineraryId(@Param("itineraryId") Long itineraryId);

//    List<Location> findLocationsWithImagesByItineraryId(Long itineraryId);
    @Query("SELECT l FROM Location l WHERE l.itinerary.id = :itineraryId")
    List<Location> findLocationsWithImagesByItineraryId(@Param("itineraryId") Long itineraryId);
}