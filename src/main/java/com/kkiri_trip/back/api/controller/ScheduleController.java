package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleDto;
import com.kkiri_trip.back.domain.jpa.schedule.service.ScheduleService;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import jakarta.validation.Valid;
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

    @PostMapping("")
    public ResponseEntity<ApiResponseDto<ScheduleDto>> createSchedule(@PathVariable("feedId") Long feedId, @RequestBody ScheduleDto scheduleDto) {
        return ApiResponseDto.from(HttpStatus.OK, "스케줄 생성", scheduleService.createSchedule(feedId,scheduleDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ScheduleDto>> updateScheduleById(@PathVariable("feedId") Long feedId, @PathVariable("id") Long id, @Valid @RequestBody ScheduleDto scheduleDto) {
        return ApiResponseDto.from(HttpStatus.OK, "스케줄 수정", scheduleService.updateSchedule(feedId,id,scheduleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteScheculdeById(@PathVariable ("feedId") Long feedId, @PathVariable Long id) {
        scheduleService.deleteSchedule(feedId, id);
        return ApiResponseDto.from(HttpStatus.OK, "스케줄 삭제", null);
    }

    @GetMapping("/dummy")
    public ResponseEntity<ApiResponseDto<ScheduleDto>> getDummyScheduleById()
    {
        ScheduleDto scheduleDto = new ScheduleDto(1L,1,1L);

        return ApiResponseDto.from(HttpStatus.OK, "스케줄 조회", scheduleDto);
    }

    @GetMapping("/dummylist")
    public ResponseEntity<ApiResponseDto<List<ScheduleDto>>> getDummyScheduleList()
    {
        ScheduleDto scheduleDto1 = new ScheduleDto(1L,1,1L);
        ScheduleDto scheduleDto2 = new ScheduleDto(2L,2,1L);
        ScheduleDto scheduleDto3 = new ScheduleDto(3L,3,1L);
        return ApiResponseDto.from(HttpStatus.OK, "스케줄 리스트 조회", List.of(scheduleDto1,scheduleDto2,scheduleDto3));
    }

}
