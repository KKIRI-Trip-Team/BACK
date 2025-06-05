package com.kkiri_trip.back.Schedule.POST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkiri_trip.back.api.controller.ScheduleController;
import com.kkiri_trip.back.api.dto.schedule.ScheduleDto;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.jpa.schedule.service.ScheduleService;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleControllerPostTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleService scheduleService;

    @MockitoBean
    private FeedRepository feedRepository;

    @MockitoBean
    private ScheduleRepository scheduleRepository;


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
        ScheduleDto requestDto = new ScheduleDto(null, 0, feedId); // invalid dayNumber

        when(scheduleService.createSchedule(eq(feedId), any(ScheduleDto.class)))
                .thenThrow(new ScheduleException(ScheduleErrorCode.INVALID_DAYNUMBER));

        // when & then
        mockMvc.perform(post("/api/feeds/{feedId}/schedules", feedId)
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
        mockMvc.perform(post("/api/feeds/{feedId}/schedules", feedId) // ✅ URL 수정
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("해당 피드를 찾을 수 없습니다."))); // ✅ 메시지 통일 추천
    }
}
