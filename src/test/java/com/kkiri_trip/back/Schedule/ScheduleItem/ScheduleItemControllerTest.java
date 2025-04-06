package com.kkiri_trip.back.Schedule.ScheduleItem;

import com.kkiri_trip.back.api.controller.ScheduleController;
import com.kkiri_trip.back.api.controller.ScheduleItemController;
import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.schedule.service.ScheduleService;
import com.kkiri_trip.back.domain.scheduleItem.service.ScheduleItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleItemControllerTest {
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
        mockMvc.perform(get("/api/feeds/{feedId}/schedules/{scheduleId}/items/{id}",feedId, scheduleId,scheduleItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.itemOrder").value(1));

    }
}
