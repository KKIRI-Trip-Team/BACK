package com.kkiri_trip.back.domain.jpa.schedule.repository;

import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}

