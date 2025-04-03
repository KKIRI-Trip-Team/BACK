package com.kkiri_trip.back.domain.schedule.repository;

import com.kkiri_trip.back.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}

