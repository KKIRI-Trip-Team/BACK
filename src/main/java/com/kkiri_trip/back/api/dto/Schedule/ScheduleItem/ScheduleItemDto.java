package com.kkiri_trip.back.api.dto.Schedule.ScheduleItem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduleItemDto {
    private Long id;
    private int itemOrder;
    private Long scheduleId;
    private String place;
}
