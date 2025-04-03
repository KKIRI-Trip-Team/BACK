package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleDto;
import com.kkiri_trip.back.domain.schedule.service.ScheduleService;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds/{feedId}/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<ScheduleDto>>> getScheduleList(@PathVariable("feedId") Long feedId) {
        return ApiResponseDto.from(HttpStatus.OK, "스케줄 리스트 조회", scheduleService.getAllSchedules(feedId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ScheduleDto>> getScheduleById(@PathVariable("feedId") Long feedId, @PathVariable("id") Long id) {
        return ApiResponseDto.from(HttpStatus.OK, "스케줄 조회", scheduleService.getScheduleById(feedId,id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ScheduleDto>> createSchedule(@PathVariable("feedId") Long feedId, @RequestBody ScheduleDto scheduleDto) {
        return ApiResponseDto.from(HttpStatus.OK, "스케줄 생성", scheduleService.createSchedule(feedId,scheduleDto));
    }

}
