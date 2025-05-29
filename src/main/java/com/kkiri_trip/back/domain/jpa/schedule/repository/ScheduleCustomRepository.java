package com.kkiri_trip.back.domain.jpa.schedule.repository;

import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleCustomRepository {
    List<Schedule> findSchedulesByFeedId(Long feedId);
    Optional<Schedule> findScheduleByFeedIdAndScheduleId(Long feedId, Long scheduleId);
}