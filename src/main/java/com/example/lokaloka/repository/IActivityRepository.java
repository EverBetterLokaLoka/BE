package com.example.lokaloka.repository;

import com.example.lokaloka.domain.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByLocationId(Long locationId);
}