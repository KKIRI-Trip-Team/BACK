package com.kkiri_trip.back.api.dto.Schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduleDto {
    private Long id;
    private int dayNumber;
    private Long feedId;
}

