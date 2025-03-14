package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.resdto.ItineraryResDTO;
import com.example.lokaloka.domain.entity.*;
import com.example.lokaloka.mapper.ItineraryMapper;
import com.example.lokaloka.repository.*;
import com.example.lokaloka.service.IItineraryService;
import com.example.lokaloka.util.ApiResponse;
import com.example.lokaloka.util.CustomException;
import com.example.lokaloka.util.ResponseData;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
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
    IImageRepository imageRepository;
    ActivityService activityService;

    @Override
    public List<ItineraryResDTO> getAllItineraries() {
        // 🔥 Lấy email của user từ SecurityContextHolder
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        // 🔥 Tìm user theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found"));

        // 🔥 Lấy danh sách itinerary của user hiện tại
        List<Itinerary> itineraries = itineraryRepository.findActiveItinerariesByUser(user);

        // 🔥 Convert sang DTO
        return itineraries.stream()
                .map(itineraryMapper::toItineraryResDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ItineraryResDTO getItineraryById(Long id) {
        Itinerary itinerary = itineraryRepository.findActiveItineraryById(id)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));
        return itineraryMapper.toItineraryResDTO(itinerary);
    }

//    @Override
//    @Transactional
//    public ItineraryResDTO createItinerary(ItineraryResDTO itineraryDTO) {
//        // 🔥 Lấy email của user từ SecurityContextHolder
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email;
//        if (principal instanceof UserDetails) {
//            email = ((UserDetails) principal).getUsername();
//        } else {
//            email = principal.toString();
//        }
//
//        // 🔥 Tìm user theo email
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found"));
//
//        // 🔥 Kiểm tra title không được để trống
//        if (itineraryDTO.getTitle() == null || itineraryDTO.getTitle().trim().isEmpty()) {
//            throw new CustomException(HttpStatus.BAD_REQUEST, "Itinerary title cannot be empty.");
//        }
//
//        // 🔥 Kiểm tra title không được trùng với user hiện tại
//        boolean exists = itineraryRepository.existsByTitleAndUser(itineraryDTO.getTitle(), user);
//        if (exists) {
//            throw new CustomException(HttpStatus.BAD_REQUEST, "Itinerary title already exists.");
//        }
//
//        // Tạo mới Itinerary
//        Itinerary itinerary = new Itinerary();
//        itinerary.setTitle(itineraryDTO.getTitle());
//        itinerary.setDescription(itineraryDTO.getDescription());
//        itinerary.setPrice(itineraryDTO.getPrice());
//        itinerary.setUser(user);
//        itinerary.setStatus(0);
//        itinerary.setAddress(itineraryDTO.getAddress());
//        Timestamp now = Timestamp.from(Instant.now());
//        itinerary.setCreated_at(now);
//        itinerary.setUpdated_at(now);
//        itinerary.setInit_date(itineraryDTO.getInit_date());
//
//        Itinerary savedItinerary = itineraryRepository.save(itinerary);
//
//        // Xử lý danh sách location
//        if (itineraryDTO.getLocations() != null && !itineraryDTO.getLocations().isEmpty()) {
//            List<Location> locations = itineraryDTO.getLocations().stream().map(locationResDTO -> {
//                Location location = new Location();
//                location.setName(locationResDTO.getName());
//                location.setDescription(locationResDTO.getDescription());
//                location.setFlag(locationResDTO.isFlag());
//                location.setCoordinate_x(locationResDTO.getCoordinate_x());
//                location.setCoordinate_y(locationResDTO.getCoordinate_y());
//                location.setTime_reminder(locationResDTO.getTime_reminder());
//                location.setDay(locationResDTO.getDay());
//
//                location.setTime_start(Timestamp.valueOf(locationResDTO.getTime_start().toLocalDateTime()));
//                location.setTime_finish(Timestamp.valueOf(locationResDTO.getTime_finish().toLocalDateTime()));
//
//                location.setCulture(locationResDTO.getCulture());
//                location.setRecommended_time(locationResDTO.getRecommended_time());
//                location.setPrice(locationResDTO.getPrice());
//
//                location.setItinerary(savedItinerary);
//
//                List<Activity> activities = new ArrayList<>();
//                if (locationResDTO.getActivities() != null && !locationResDTO.getActivities().isEmpty()) {
//                    activities = locationResDTO.getActivities().stream().map(activityDTO -> {
//                        Activity activity = new Activity();
//                        activity.setName(activityDTO.getName());
//                        activity.setDescription(activityDTO.getDescription());
//                        activity.setActivities_possible(activityDTO.getActivities_possible());
//                        activity.setPrice(activityDTO.getPrice());
//                        activity.setRule(activityDTO.getRule());
//                        activity.setRecommend(activityDTO.getRecommend());
//
//                        activity.setLocation(location);
//                        return activity;
//                    }).collect(Collectors.toList());
//                }
//                location.setActivities(activities);
//                return location;
//            }).collect(Collectors.toList());
//
//            // Lưu danh sách Location
//            List<Location> savedLocations = locationRepository.saveAll(locations);
//
//            // Lưu danh sách Activity
//            List<Activity> allActivities = savedLocations.stream()
//                    .flatMap(loc -> loc.getActivities().stream())
//                    .collect(Collectors.toList());
//            activityRepository.saveAll(allActivities);
//        }
//
//        return itineraryMapper.toItineraryResDTO(savedItinerary);
//    }

    @Override
    @Transactional
    public ItineraryResDTO createItinerary(ItineraryResDTO itineraryDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        // Tìm user theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found"));

        // Kiểm tra title không được để trống
        if (itineraryDTO.getTitle() == null || itineraryDTO.getTitle().trim().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Itinerary title cannot be empty.");
        }

        // Kiểm tra title không được trùng với user hiện tại
        boolean exists = itineraryRepository.existsByTitleAndUser(itineraryDTO.getTitle(), user);
        if (exists) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Itinerary title already exists.");
        }

        // Tạo đối tượng itinerary mới
        Itinerary itinerary = new Itinerary();
        itinerary.setTitle(itineraryDTO.getTitle());
        itinerary.setDescription(itineraryDTO.getDescription());
        itinerary.setPrice(itineraryDTO.getPrice());
        itinerary.setUser(user);
        itinerary.setStatus(0);
        itinerary.setAddress(itineraryDTO.getAddress());
        Timestamp now = Timestamp.from(Instant.now());
        itinerary.setCreated_at(now);
        itinerary.setUpdated_at(now);
        itinerary.setInit_date(itineraryDTO.getInit_date());
        Itinerary savedItinerary = itineraryRepository.save(itinerary);

        // Xử lý danh sách location
        if (itineraryDTO.getLocations() != null && !itineraryDTO.getLocations().isEmpty()) {
            List<Location> locations = itineraryDTO.getLocations().stream().map(locationResDTO -> {
                Location location = new Location();
                location.setName(locationResDTO.getName());
                location.setDescription(locationResDTO.getDescription());
                location.setFlag(locationResDTO.isFlag());
                location.setCoordinate_x(locationResDTO.getCoordinate_x());
                location.setCoordinate_y(locationResDTO.getCoordinate_y());
                location.setTime_reminder(locationResDTO.getTime_reminder());
                location.setDay(locationResDTO.getDay());
                location.setTime_start(Timestamp.valueOf(locationResDTO.getTime_start().toLocalDateTime()));
                location.setTime_finish(Timestamp.valueOf(locationResDTO.getTime_finish().toLocalDateTime()));
                location.setCulture(locationResDTO.getCulture());
                location.setRecommended_time(locationResDTO.getRecommended_time());
                location.setPrice(locationResDTO.getPrice());
                location.setImage_url(locationResDTO.getImage());

                // Gán giá trị cho itinerary_id
                location.setItinerary(savedItinerary); // Đảm bảo rằng itinerary không phải là null

                Location savedLocation = locationRepository.save(location);

                // Xử lý Activities
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

                        activity.setLocation(savedLocation); // Gán location đã lưu vào activity
                        return activity;
                    }).collect(Collectors.toList());
                }

                // Cập nhật activities cho savedLocation sau khi đã lưu
                savedLocation.setActivities(activities);
                // Lưu activities vào database
                activityRepository.saveAll(activities);

                return savedLocation;
            }).collect(Collectors.toList());

            // Lưu danh sách Location vào CSDL
            locationRepository.saveAll(locations);
        }

        return itineraryMapper.toItineraryResDTO(savedItinerary); // Trả về DTO của itinerary đã lưu
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
        existingItinerary.setStart_date(itineraryDTO.getStart_date());

        Itinerary updatedItinerary = itineraryRepository.save(existingItinerary);
        return itineraryMapper.toItineraryResDTO(updatedItinerary);
    }

    @Override
    public ResponseEntity<?> deleteItineraryById(Long id) {
        Itinerary existingItinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        if(existingItinerary.isDestroyed()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseData.builder()
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Itinerary have been deleted")
                            .build());
        }
        existingItinerary.setDestroyed(true);
        itineraryRepository.save(existingItinerary);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ResponseData.builder()
                        .success(true)
                        .status(HttpStatus.NO_CONTENT.value())
                        .message("Itinerary delete successfully")
                        .build());
    }
}