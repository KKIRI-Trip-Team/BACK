package com.kkiri_trip.back.domain.jpa.scheduleItem.repository;

import com.kkiri_trip.back.domain.jpa.scheduleItem.entity.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Long> {
}
