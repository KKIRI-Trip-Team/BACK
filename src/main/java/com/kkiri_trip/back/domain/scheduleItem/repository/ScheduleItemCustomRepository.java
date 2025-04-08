package com.kkiri_trip.back.domain.scheduleItem.repository;

import com.kkiri_trip.back.domain.schedule.entity.Schedule;
import com.kkiri_trip.back.domain.scheduleItem.entity.ScheduleItem;

import java.util.List;


public interface ScheduleItemCustomRepository {
    boolean isItemOrderDuplicated(Schedule schedule, int itemOrder);
    List<ScheduleItem> findAllByFeedId(Long feedId);
    List<ScheduleItem> findAllByScheduleId(Long scheduleId);
}
