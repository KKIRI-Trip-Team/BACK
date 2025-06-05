package com.kkiri_trip.back.Schedule.GET;

import com.kkiri_trip.back.api.controller.ScheduleController;
import com.kkiri_trip.back.api.dto.schedule.ScheduleDto;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.jpa.schedule.service.ScheduleService;
import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleControllerGetTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleService scheduleService;

    @MockitoBean
    private FeedRepository feedRepository;

    @MockitoBean
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("스케줄 목록을 조회한다.")
    void getScheduleList() throws Exception {
        // given
        Long feedId = 1L;
        List<ScheduleDto> schedules = List.of(
                new ScheduleDto(1L, 1, feedId),
                new ScheduleDto(2L, 2, feedId),
                new ScheduleDto(3L, 3, feedId)
        );

        given(scheduleService.getAllSchedules(eq(feedId))).willReturn(schedules);

        // when & then
        mockMvc.perform(get("/api/feeds/1/schedules")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].dayNumber").value(1))
                .andExpect(jsonPath("$.data[1].dayNumber").value(2))
                .andExpect(jsonPath("$.data[2].dayNumber").value(3));
    }

    @Test
    @DisplayName("스케줄 id로 스케줄을 조회한다.")
    void getScheduleById() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 1L;
        ScheduleDto schedule = new ScheduleDto(1L, 1, feedId);

        given(scheduleService.getScheduleById(eq(feedId), eq(scheduleId))).willReturn(schedule);

        // when & then
        mockMvc.perform(get("/api/feeds/1/schedules/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.dayNumber").value(1));
    }

    @Test
    @DisplayName("스케줄 id로 스케줄을 조회한다.")
    void getScheduleByIdException() throws Exception {
        // given
        given(scheduleService.getScheduleById(1L, 1L))
                .willThrow(new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/feeds/1/schedules/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is("해당 스케줄을 찾을 수 없습니다.")));
    }
}
