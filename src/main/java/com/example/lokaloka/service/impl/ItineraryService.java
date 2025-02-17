package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.resdto.ItineraryResDTO;
import com.example.lokaloka.domain.entity.Itinerary;
import com.example.lokaloka.domain.entity.Location;
import com.example.lokaloka.domain.entity.Activity;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.mapper.ItineraryMapper;
import com.example.lokaloka.repository.IActivityRepository;
import com.example.lokaloka.repository.IItineraryRepository;
import com.example.lokaloka.repository.ILocationRepository;
import com.example.lokaloka.repository.IUserRepository;
import com.example.lokaloka.service.IItineraryService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ItineraryService implements IItineraryService {
    IItineraryRepository itineraryRepository;
    IUserRepository userRepository;
    ILocationRepository locationRepository;
    IActivityRepository activityRepository;
    ItineraryMapper itineraryMapper;
    LocationService locationService;
    ActivityService activityService;

    @Override
    public List<ItineraryResDTO> getAllItineraries() {
        // get current user is Login
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));

        List<Itinerary> itineraries = itineraryRepository.findByUser(user);
        return itineraries.stream()
                .map(itineraryMapper::toItineraryResDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItineraryResDTO getItineraryById(Long id) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));
        return itineraryMapper.toItineraryResDTO(itinerary);
    }


    @Override
    @Transactional
    public ItineraryResDTO createItinerary(ItineraryResDTO itineraryDTO) {
        //  get current user login
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo mới Itinerary
        Itinerary itinerary = new Itinerary();
        itinerary.setTitle(itineraryDTO.getTitle());
        itinerary.setDescription(itineraryDTO.getDescription());
        itinerary.setPrice(itineraryDTO.getPrice());
        itinerary.setUser(user);
        itinerary.setStatus(0);

        Timestamp now = Timestamp.from(Instant.now());
        itinerary.setCreated_at(now);
        itinerary.setUpdated_at(now);

        Itinerary savedItinerary = itineraryRepository.save(itinerary);

        // Tạo danh sách Location
        if (itineraryDTO.getLocations() != null && !itineraryDTO.getLocations().isEmpty()) {
            List<Location> locations = itineraryDTO.getLocations().stream().map(locationResDTO -> {
                Location location = new Location();
                location.setName(locationResDTO.getName());
                location.setDescription(locationResDTO.getDescription());
                location.setFlag(locationResDTO.isFlag());
                location.setCoordinate_x(locationResDTO.getCoordinate_x());
                location.setCoordinate_y(locationResDTO.getCoordinate_y());
                location.setTime_reminder(locationResDTO.getTime_reminder());

                // Chuyển đổi String thành Timestamp
                location.setTime_start(Timestamp.valueOf(locationResDTO.getTime_start().toLocalDateTime()));
                location.setTime_finish(Timestamp.valueOf(locationResDTO.getTime_finish().toLocalDateTime()));

                location.setCulture(locationResDTO.getCulture());
                location.setRecommended_time(locationResDTO.getRecommended_time());
                location.setPrice(locationResDTO.getPrice());

                location.setItinerary(savedItinerary);

                List<Activity> activities = new ArrayList<>();
                if (locationResDTO.getActivities() != null && !locationResDTO.getActivities().isEmpty()) {
                    activities = locationResDTO.getActivities().stream().map(activityDTO -> {
                        Activity activity = new Activity();
                        activity.setName(activityDTO.getName());
                        activity.setDescription(activityDTO.getDescription());
                        activity.setActivities_possible(activityDTO.getActivities_possible());
                        activity.setPrice(activityDTO.getPrice());
                        activity.setRule(activityDTO.getRule());
                        activity.setRecommend(activityDTO.getRecommend());

                        activity.setLocation(location);
                        return activity;
                    }).collect(Collectors.toList());
                }
                location.setActivities(activities);
                return location;
            }).collect(Collectors.toList());

            // Lưu danh sách Location
            List<Location> savedLocations = locationRepository.saveAll(locations);

            // Lưu danh sách Activity
            List<Activity> allActivities = savedLocations.stream()
                    .flatMap(loc -> loc.getActivities().stream())
                    .collect(Collectors.toList());
            activityRepository.saveAll(allActivities);
        }

        return itineraryMapper.toItineraryResDTO(savedItinerary);
    }

    @Override
    @Transactional
    public ItineraryResDTO updateItinerary(ItineraryResDTO itineraryDTO) {
        Itinerary existingItinerary = itineraryRepository.findById(itineraryDTO.getId())
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        existingItinerary.setTitle(itineraryDTO.getTitle());
        existingItinerary.setDescription(itineraryDTO.getDescription());
        existingItinerary.setPrice(itineraryDTO.getPrice());
        existingItinerary.setStatus(itineraryDTO.getStatus());
        existingItinerary.setUpdated_at(Timestamp.from(Instant.now()));

        Itinerary updatedItinerary = itineraryRepository.save(existingItinerary);
        return itineraryMapper.toItineraryResDTO(updatedItinerary);
    }

    @Override
    public void deleteItineraryById(Long id) {
        if (!itineraryRepository.existsById(id)) {
            throw new RuntimeException("Itinerary not found");
        }
        itineraryRepository.updateStatus(id);
    }
}