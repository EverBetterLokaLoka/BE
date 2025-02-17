package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.entity.Location;
import com.example.lokaloka.domain.entity.Itinerary;
import com.example.lokaloka.repository.ILocationRepository;
import com.example.lokaloka.repository.IItineraryRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class LocationService {
    ILocationRepository locationRepository;
    IItineraryRepository itineraryRepository;

    public List<Location> createLocationsForItinerary(List<Location> locations, Itinerary itinerary) {
        locations.forEach(location -> {
            location.setItinerary(itinerary);
        });
        return locationRepository.saveAll(locations);
    }
}