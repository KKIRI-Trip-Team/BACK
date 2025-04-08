package com.kkiri_trip.back.Schedule.ScheduleItem.GET;

import com.kkiri_trip.back.api.controller.ScheduleItemController;
import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.scheduleItem.service.ScheduleItemService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleItemControllerGetTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleItemService scheduleItemService;

    @MockitoBean
    private FeedRepository feedRepository;

    @MockitoBean
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("피드에 있는 스케줄 아이템 목록을 조회한다.")
    void getScheduleItemListInFeed() throws Exception {
        // given
        Long feedId = 1L;
        List<ScheduleItemDto> scheduleItems = List.of(
                new ScheduleItemDto(1L, 1, 1L),
                new ScheduleItemDto(2L, 2, 1L),
                new ScheduleItemDto(3L, 3, 1L)
        );

        given(scheduleItemService.getAllScheduleItemsInFeed(eq(feedId))).willReturn(scheduleItems);

        // when & then
        mockMvc.perform(get("/api/feeds/{feedId}/scheduleItems", feedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].itemOrder").value(1))
                .andExpect(jsonPath("$.data[1].itemOrder").value(2))
                .andExpect(jsonPath("$.data[2].itemOrder").value(3));
    }

    @Test
    @DisplayName("스케줄에 있는 스케줄 아이템 목록을 조회한다.")
    void getScheduleItemListInSchedule() throws Exception {
        // given
        Long scheduleId = 1L;
        List<ScheduleItemDto> scheduleItems = List.of(
                new ScheduleItemDto(1L, 1, 1L),
                new ScheduleItemDto(2L, 2, 1L),
                new ScheduleItemDto(3L, 3, 1L)
        );

        given(scheduleItemService.getAllScheduleItemsInSchedule(eq(scheduleId))).willReturn(scheduleItems);

        // when & then
        mockMvc.perform(get("/api/schedules/{scheduleId}/scheduleItems", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].itemOrder").value(1))
                .andExpect(jsonPath("$.data[1].itemOrder").value(2))
                .andExpect(jsonPath("$.data[2].itemOrder").value(3));
    }

    @Test
    @DisplayName("스케줄 아이템을 조회한다.")
    void getScheduleItemById() throws Exception {
        // given
        Long feedId = 1L;
        Long scheduleId = 1L;
        Long scheduleItemId = 1L;

        ScheduleItemDto scheduleItemDto = new ScheduleItemDto(1L, 1, 1L);

        given(scheduleItemService.getScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemId)))
                .willReturn(scheduleItemDto);

        // when & then
        mockMvc.perform(get("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}",feedId, scheduleId,scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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

        given(scheduleItemService.getScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemId)))
                .willThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}",feedId, scheduleId,scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
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

        given(scheduleItemService.getScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemId)))
                .willThrow(new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}",feedId, scheduleId,scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
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

        given(scheduleItemService.getScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemId)))
                .willThrow(new ScheduleItemException(ScheduleItemErrorCode.SCHEDULEITEM_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}",feedId, scheduleId,scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
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

        given(scheduleItemService.getScheduleItem(
                eq(feedId),
                eq(scheduleId),
                eq(scheduleItemId)))
                .willThrow(new ScheduleItemException(ScheduleItemErrorCode.INVALID_ITEMORDER));

        // when & then
        mockMvc.perform(get("/api/feeds/{feedId}/schedules/{scheduleId}/scheduleItems/{id}",feedId, scheduleId,scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",is("아이템 순서는 0이하일 수 없습니다.")));
    }


}
