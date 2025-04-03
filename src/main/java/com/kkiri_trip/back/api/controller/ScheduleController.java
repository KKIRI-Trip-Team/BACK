package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{id}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;



}
