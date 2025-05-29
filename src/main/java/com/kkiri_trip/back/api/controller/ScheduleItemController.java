package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.jpa.scheduleItem.service.ScheduleItemService;
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

    @GetMapping("/scheduleItems/dummy")
    ResponseEntity<ApiResponseDto<ScheduleItemDto>> getDummyScheduleItems()
    {
        ScheduleItemDto scheduleItemDto = new ScheduleItemDto(1L,1,1L);
        return ApiResponseDto.from(HttpStatus.OK, "피드의 스케줄 아이템 조회", scheduleItemDto);
    }

    @GetMapping("/scheduleItems/dummylist")
    ResponseEntity<ApiResponseDto<List<ScheduleItemDto>>> getAllDummyScheduleItems()
    {
        ScheduleItemDto scheduleItemDto1 = new ScheduleItemDto(1L,1,1L);
        ScheduleItemDto scheduleItemDto2 = new ScheduleItemDto(2L,2,1L);
        ScheduleItemDto scheduleItemDto3 = new ScheduleItemDto(3L,3,1L);
        return ApiResponseDto.from(HttpStatus.OK, "피드의 스케줄 아이템 리스트 조회", List.of(scheduleItemDto1,scheduleItemDto2,scheduleItemDto3));
    }


}
