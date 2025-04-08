package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.scheduleItem.service.ScheduleItemService;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleItemController {

    private final ScheduleItemService scheduleItemService;

    @GetMapping("feeds/{feedId}/scheduleItems")
    ResponseEntity<ApiResponseDto<List<ScheduleItemDto>>> getAllScheduleItemsInFeed(@PathVariable("feedId") Long feedId)
    {
        return ApiResponseDto.from(HttpStatus.OK, "피드의 스케줄 아이템 리스트 조회", scheduleItemService.getAllScheduleItemsInFeed(feedId));
    }

    @GetMapping("/schedules/{scheduleId}/scheduleItems")
    ResponseEntity<ApiResponseDto<List<ScheduleItemDto>>> getAllScheduleItemsInSchedule(@PathVariable("scheduleId") Long scheduleId)
    {
        return ApiResponseDto.from(HttpStatus.OK, "스케줄의 스케줄 아이템 리스트 조회", scheduleItemService.getAllScheduleItemsInSchedule(scheduleId));
    }

    @GetMapping("/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}")
    ResponseEntity<ApiResponseDto<ScheduleItemDto>> getScheduleItem(@PathVariable("feedId") Long feedId,
                                                                          @PathVariable("scheduleId") Long scheduleId,
                                                                          @PathVariable("id") Long id) {
        return ApiResponseDto.from(HttpStatus.OK,
                "스케줄 아이템 조회",
                scheduleItemService.getScheduleItem(
                        feedId,
                        scheduleId,
                        id
                )
        );
    }


    @PostMapping("/feeds/{feedId}/schedules/{scheduleId}/scheduleItems")
    ResponseEntity<ApiResponseDto<ScheduleItemDto>> createScheduleItem(@PathVariable("feedId") Long feedId,
                                                                    @PathVariable("scheduleId") Long scheduleId,
                                                                    @RequestBody ScheduleItemDto scheduleItemDto) {
        return ApiResponseDto.from(HttpStatus.CREATED,
                "스케줄 아이템 조회",
            scheduleItemService.createScheduleItem(feedId,scheduleId,scheduleItemDto)
        );
    }

    @PutMapping("/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}")
    ResponseEntity<ApiResponseDto<ScheduleItemDto>> updateScheduleItem(@PathVariable("feedId") Long feedId,
                                                                       @PathVariable("scheduleId") Long scheduleId,
                                                                       @PathVariable("id") Long id,
                                                                       @RequestBody ScheduleItemDto scheduleItemDto)
    {
        return ApiResponseDto.from(HttpStatus.OK,
                "스케줄 아이템 수정",
                scheduleItemService.updateScheduleItem(feedId,scheduleId,id,scheduleItemDto));
    }

    @DeleteMapping("/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}")
    ResponseEntity<ApiResponseDto<Void>> deleteScheduleItem(@PathVariable("feedId") Long feedId,
                                                                       @PathVariable("scheduleId") Long scheduleId,
                                                                       @PathVariable("id") Long id)
    {
        scheduleItemService.deleteScheduleItem(feedId,scheduleId,id);
        return ApiResponseDto.from(HttpStatus.OK,
                "스케줄 아이템 수정",
                null);
    }


}
