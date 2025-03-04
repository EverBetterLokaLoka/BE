package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.resdto.ItineraryResDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IItineraryService {
    List<ItineraryResDTO> getAllItineraries();
    ItineraryResDTO getItineraryById(Long id);
    ItineraryResDTO createItinerary(ItineraryResDTO itineraryDTO);
    ItineraryResDTO updateItinerary(ItineraryResDTO itinerary);
    void deleteItineraryById(Long id);
}