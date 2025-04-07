package com.kkiri_trip.back.Schedule.DELETE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkiri_trip.back.api.controller.ScheduleController;
import com.kkiri_trip.back.api.dto.Schedule.ScheduleDto;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.schedule.service.ScheduleService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleService scheduleService;

    @MockitoBean
    private FeedRepository feedRepository;

    @Test
    @DisplayName("피드를 정상적으로 삭제한다.")
    void deleteFeedSuccess() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 1L;
        doNothing().when(scheduleService).deleteSchedule(scheduleId);

        // when & then
        mockMvc.perform(delete("/api/feeds/{feedId}/schedules/{scheduleId}",feedId, scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 피드를 삭제하면 예외가 발생한다.")
    void deleteFeedNotFound() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 999L; // 없는 ID
        doThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND))
                .when(scheduleService).deleteSchedule(scheduleId);

        // when & then
        mockMvc.perform(delete("/api/feeds/{feedId}/schedules/{scheduleId}",feedId, scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("해당 피드를 찾을 수 없습니다.")));
    }

}
