package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.entity.Activity;
import com.example.lokaloka.domain.entity.Location;
import com.example.lokaloka.repository.IActivityRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ActivityService {
    IActivityRepository activityRepository;

    public List<Activity> createActivitiesForLocation(List<Activity> activities, Location location) {
        Timestamp now = Timestamp.from(Instant.now());
        activities.forEach(activity -> {
            activity.setLocation(location);
            activity.setCreated_at(now);
            activity.setUpdated_at(now);
        });
        return activityRepository.saveAll(activities);
    }
}