package com.kkiri_trip.back.Schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkiri_trip.back.api.controller.FeedController;
import com.kkiri_trip.back.api.controller.ScheduleController;
import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.api.dto.Schedule.ScheduleDto;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.feed.service.FeedService;
import com.kkiri_trip.back.domain.schedule.entity.Schedule;
import com.kkiri_trip.back.domain.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.schedule.service.ScheduleService;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {
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

        // scheduleService.getAllSchedules(feedId) 호출 시, schedules 리스트 반환
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

    @Test
    @DisplayName("정상적으로 스케줄을 생성한다.")
    void createSchedule_Success() throws Exception {
        // given
        Long feedId = 1L;
        ScheduleDto requestDto = new ScheduleDto(null, 1, feedId); // 요청 DTO
        ScheduleDto responseDto = new ScheduleDto(1L, 1, feedId); // 응답 DTO

        when(scheduleService.createSchedule(eq(feedId), any(ScheduleDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/feeds/{feedId}/schedules", feedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.dayNumber").value(1))
                .andExpect(jsonPath("$.data.feedId").value(feedId));
    }

    @Test
    @DisplayName("유효하지 않은 dayNumber로 인해 생성 실패")
    void createScheduleFailInvalidDayNumber() throws Exception {
        // given
        Long feedId = 1L;
        ScheduleDto requestDto = new ScheduleDto(null, 0, feedId); // dayNumber가 0 (유효하지 않음)

        when(scheduleService.createSchedule(eq(feedId), any(ScheduleDto.class)))
                .thenThrow(new ScheduleException(ScheduleErrorCode.INVALID_DAYNUMBER));

        // when & then
        mockMvc.perform(post("/api/feeds/{feedId}/schedules/1", feedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",is("일차는 0이하일 수 없습니다.")));

    }

    @Test
    @DisplayName("존재하지 않는 피드 ID로 인해 생성 실패")
    void createScheduleFailInvalidFeed() throws Exception {
        // given
        Long feedId = 999L; // 존재하지 않는 피드
        ScheduleDto requestDto = new ScheduleDto(null, 1, feedId);

        when(scheduleService.createSchedule(eq(feedId), any(ScheduleDto.class)))
                .thenThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/api/feeds/{feedId}/schedules/1", feedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",is("유효하지 않는 피드입니다.")));

    }
}
