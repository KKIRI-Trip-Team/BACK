package com.kkiri_trip.back.domain.jpa.scheduleItem.repository;

import com.kkiri_trip.back.domain.jpa.scheduleItem.entity.ScheduleItem;
import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;

import java.util.List;


public interface ScheduleItemCustomRepository {
    boolean isItemOrderDuplicated(Schedule schedule, int itemOrder);
    List<ScheduleItem> findAllByFeedId(Long feedId);
    List<ScheduleItem> findAllByScheduleId(Long scheduleId);
}
