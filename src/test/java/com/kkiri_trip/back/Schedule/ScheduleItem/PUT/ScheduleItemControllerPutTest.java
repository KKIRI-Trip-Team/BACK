package com.kkiri_trip.back.Schedule.ScheduleItem.PUT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkiri_trip.back.api.controller.ScheduleItemController;
import com.kkiri_trip.back.api.dto.schedule.scheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.jpa.scheduleItem.service.ScheduleItemService;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleItemErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import com.kkiri_trip.back.global.error.exception.ScheduleItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.eq;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ScheduleItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleItemControllerPutTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleItemService scheduleItemService;

    @MockitoBean
    private FeedRepository feedRepository;

    @MockitoBean
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("스케줄 아이템 수정 성공")
    void updateScheduleItem() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 1L;
        Long scheduleItemId = 1L;

        ScheduleItemDto requestDto = new ScheduleItemDto(scheduleItemId, 2, scheduleId); // 수정할 데이터
        ScheduleItemDto updatedDto = new ScheduleItemDto(scheduleItemId, 2, scheduleId); // 응답받을 DTO

        given(scheduleItemService.updateScheduleItem(eq(feedId), eq(scheduleId), eq(scheduleItemId), eq(requestDto)))
                .willReturn(updatedDto);

        // when & then
        mockMvc.perform(put("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{itemId}", feedId, scheduleId, scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.itemOrder").value(2));
    }
    @Test
    @DisplayName("피드가 존재하지 않아 수정 실패")
    void updateScheduleItemFail_feedNotFound() throws Exception {
        Long feedId = 999L;
        Long scheduleId = 1L;
        Long scheduleItemId = 1L;

        ScheduleItemDto requestDto = new ScheduleItemDto(scheduleItemId, 2, scheduleId);

        given(scheduleItemService.updateScheduleItem(eq(feedId), eq(scheduleId), eq(scheduleItemId), eq(requestDto)))
                .willThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        mockMvc.perform(put("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{itemId}", feedId, scheduleId, scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 피드를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("스케줄이 존재하지 않아 수정 실패")
    void updateScheduleItemFail_scheduleNotFound() throws Exception {
        Long feedId = 1L;
        Long scheduleId = 999L;
        Long scheduleItemId = 1L;

        ScheduleItemDto requestDto = new ScheduleItemDto(scheduleItemId, 2, scheduleId);

        given(scheduleItemService.updateScheduleItem(eq(feedId), eq(scheduleId), eq(scheduleItemId), eq(requestDto)))
                .willThrow(new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        mockMvc.perform(put("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{itemId}", feedId, scheduleId, scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 스케줄을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("중복된 아이템 순서로 인해 수정 실패")
    void updateScheduleItemFail_duplicatedItemOrder() throws Exception {
        Long feedId = 1L;
        Long scheduleId = 1L;
        Long scheduleItemId = 1L;

        ScheduleItemDto requestDto = new ScheduleItemDto(scheduleItemId, 1, scheduleId); // 중복된 itemOrder 가정

        given(scheduleItemService.updateScheduleItem(eq(feedId), eq(scheduleId), eq(scheduleItemId), eq(requestDto)))
                .willThrow(new ScheduleItemException(ScheduleItemErrorCode.INVALID_ITEMORDER));

        mockMvc.perform(put("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{itemId}", feedId, scheduleId, scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("아이템 순서는 0이하일 수 없습니다."));
    }

}
