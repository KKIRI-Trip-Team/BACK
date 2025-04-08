package com.kkiri_trip.back.domain.scheduleItem.repository;

import com.kkiri_trip.back.domain.scheduleItem.entity.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Long> {
}
