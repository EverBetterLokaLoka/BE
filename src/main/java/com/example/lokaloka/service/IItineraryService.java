package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.resdto.ItineraryResDTO;

import java.util.List;

public interface IItineraryService {
    List<ItineraryResDTO> getAllItineraries();
    ItineraryResDTO getItineraryById(Long id);
    ItineraryResDTO createItinerary(ItineraryResDTO itinerary);
    void deleteItineraryById(Long id);
}
