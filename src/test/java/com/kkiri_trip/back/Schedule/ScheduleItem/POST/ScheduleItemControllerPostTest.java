package com.kkiri_trip.back.Schedule.ScheduleItem.POST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkiri_trip.back.api.controller.ScheduleItemController;
import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleItemControllerPostTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleItemService scheduleItemService;

    @MockitoBean
    private FeedRepository feedRepository;

    @MockitoBean
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("스케줄 아이템 생성.")
    void createScheduleItem() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 1L;
        Long scheduleItemId = 1L;

        ScheduleItemDto scheduleItemDto = new ScheduleItemDto(1L, 1, 1L);

        given(scheduleItemService.createScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemDto)))
                .willReturn(scheduleItemDto);

        // when & then
        mockMvc.perform(post("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems",feedId, scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(scheduleItemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.itemOrder").value(1));
    }

    @Test
    @DisplayName("유효하지 않는 피드로 인해 생성 실패.")
    void getScheduleItemByIdFailInvalidFeed() throws Exception {
        // given
        Long feedId = 999L;
        Long scheduleId = 1L;
        Long scheduleItemId = 1L;

        ScheduleItemDto scheduleItemDto = new ScheduleItemDto(999L, 1, 1L); // invalid feed

        given(scheduleItemService.createScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemDto)))
                .willThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems",feedId, scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(scheduleItemDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is("해당 피드를 찾을 수 없습니다.")));

    }

    @Test
    @DisplayName("유효하지 않는 스케줄로 인해 생성 실패.")
    void getScheduleItemByIdFailInvalidSchedule() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 999L;
        Long scheduleItemId = 1L;

        ScheduleItemDto scheduleItemDto = new ScheduleItemDto(999L, 1, 1L); // invalid feed

        given(scheduleItemService.createScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemDto)))
                .willThrow(new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems",feedId, scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(scheduleItemDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is("해당 스케줄을 찾을 수 없습니다.")));

    }

    @Test
    @DisplayName("유효하지 않는 스케줄 아이템으로 인해 생성 실패.")
    void getScheduleItemByIdFailInvalidScheduleItem() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 1L;
        Long scheduleItemId = 999L;

        ScheduleItemDto scheduleItemDto = new ScheduleItemDto(1L, 1, 999L); // invalid feed

        given(scheduleItemService.createScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemDto)))
                .willThrow(new ScheduleItemException(ScheduleItemErrorCode.SCHEDULEITEM_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems",feedId, scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(scheduleItemDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is("해당 스케줄 아이템을 찾을 수 없습니다.")));

    }

    @Test
    @DisplayName("유효하지 않는 아이템 순서로 인해 생성 실패.")
    void getScheduleItemByIdFailInvalidItemOrder() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 1L;
        Long scheduleItemId = 1L;

        ScheduleItemDto scheduleItemDto = new ScheduleItemDto(1L, 0, 1L); // invalid itemOrder

        given(scheduleItemService.createScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemDto)))
                .willThrow(new ScheduleItemException(ScheduleItemErrorCode.INVALID_ITEMORDER));

        // when & then
        mockMvc.perform(post("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems",feedId, scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(scheduleItemDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",is("아이템 순서는 0이하일 수 없습니다.")));
    }
}
