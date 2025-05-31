package com.kkiri_trip.back.Schedule.ScheduleItem.DELETE;

import com.kkiri_trip.back.api.controller.ScheduleItemController;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.jpa.scheduleItem.service.ScheduleItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleItemErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import com.kkiri_trip.back.global.error.exception.ScheduleItemException;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleItemControllerDeleteTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleItemService scheduleItemService;

    @MockitoBean
    private FeedRepository feedRepository;

    @MockitoBean
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("스케줄을 정상적으로 삭제한다.")
    void deleteScheduleItem() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 1L;
        Long scheduleItemId = 1L;
        doNothing().when(scheduleItemService).deleteScheduleItem(feedId,scheduleId,scheduleItemId);

        // when & then
        mockMvc.perform(delete("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}",feedId, scheduleId, scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("피드가 존재하지 않아 스케줄 아이템 삭제 실패")
    void deleteScheduleItemFailFeedNotFound() throws Exception {
        Long feedId = 999L;
        Long scheduleId = 1L;
        Long scheduleItemId = 1L;

        willThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND))
                .given(scheduleItemService)
                .deleteScheduleItem(feedId, scheduleId, scheduleItemId);

        mockMvc.perform(delete("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}", feedId, scheduleId, scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("해당 피드를 찾을 수 없습니다.")));
    }

    @Test
    @DisplayName("스케줄이 존재하지 않아 스케줄 아이템 삭제 실패")
    void deleteScheduleItemFailScheduleNotFound() throws Exception {
        Long feedId = 1L;
        Long scheduleId = 999L;
        Long scheduleItemId = 1L;

        willThrow(new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND))
                .given(scheduleItemService)
                .deleteScheduleItem(feedId, scheduleId, scheduleItemId);

        mockMvc.perform(delete("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}", feedId, scheduleId, scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("해당 스케줄을 찾을 수 없습니다.")));
    }

    @Test
    @DisplayName("스케줄 아이템이 존재하지 않아 삭제 실패")
    void deleteScheduleItemFailItemNotFound() throws Exception {
        Long feedId = 1L;
        Long scheduleId = 1L;
        Long scheduleItemId = 999L;

        willThrow(new ScheduleItemException(ScheduleItemErrorCode.SCHEDULEITEM_NOT_FOUND))
                .given(scheduleItemService)
                .deleteScheduleItem(feedId, scheduleId, scheduleItemId);

        mockMvc.perform(delete("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}", feedId, scheduleId, scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("해당 스케줄 아이템을 찾을 수 없습니다.")));
    }



}
