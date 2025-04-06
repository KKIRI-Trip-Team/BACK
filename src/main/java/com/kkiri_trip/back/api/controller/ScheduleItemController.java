package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.scheduleItem.service.ScheduleItemService;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleItemController {

    private final ScheduleItemService scheduleItemService;

    @GetMapping("feeds/{feedId}/scheduleItems")
    ResponseEntity<ApiResponseDto<List<ScheduleItemDto>>> getAllScheduleItemsInFeed(@PathVariable("feedId") Long feedId)
    {
        return ApiResponseDto.from(HttpStatus.OK, "피드의 스케줄 아이템 조회", scheduleItemService.getAllScheduleItemsInFeed(feedId));
    }

    @GetMapping("/schedules/{scheduleId}/scheduleItems")
    ResponseEntity<ApiResponseDto<List<ScheduleItemDto>>> getAllScheduleItemsInSchedule(@PathVariable("scheduleId") Long scheduleId)
    {
        return ApiResponseDto.from(HttpStatus.OK, "피드의 스케줄 아이템 조회", scheduleItemService.getAllScheduleItemsInSchedule(scheduleId));
    }

    @GetMapping("/feeds/{feedId}/schedules/{scheduleId}/items/{id}")
    ResponseEntity<ApiResponseDto<ScheduleItemDto>> getScheduleItem(@PathVariable("feedId") Long feedId,
                                                                          @PathVariable("scheduleId") Long scheduleId,
                                                                          @PathVariable("id") Long id) {
        return ApiResponseDto.from(HttpStatus.OK,
                "피드의 스케줄 아이템 조회",
                scheduleItemService.getScheduleItem(
                        feedId,
                        scheduleId,
                        id
                ));
    }


}
