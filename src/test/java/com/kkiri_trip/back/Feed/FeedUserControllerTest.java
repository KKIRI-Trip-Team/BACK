package com.kkiri_trip.back.Feed;

import com.kkiri_trip.back.api.controller.FeedUserController;
import com.kkiri_trip.back.api.dto.FeedUser.FeedUserDto;
import com.kkiri_trip.back.domain.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.feedUser.entity.FeedUserStatus;
import com.kkiri_trip.back.domain.feedUser.repository.FeedUserRepository;
import com.kkiri_trip.back.domain.feedUser.service.FeedUserService;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class FeedUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeedUserService feedUserService;

    @MockitoBean
    private FeedUserRepository feedUserRepository;


    @Test
    @DisplayName("피드 참여 승인을 한다.")
    void approveJoin() throws Exception {
        // given
        Long feedUserId = 1L;
        doNothing().when(feedUserService).approveJoin(feedUserId);

        // when & then
        mockMvc.perform(post("/api/feed-users/{feedUserId}/approve", feedUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("피드 참여 승인 완료")));
    }

    @Test
    @DisplayName("피드 참여 거절을 한다.")
    void rejectJoin() throws Exception {
        // given
        Long feedUserId = 1L;
        doNothing().when(feedUserService).rejectJoin(feedUserId);

        // when & then
        mockMvc.perform(post("/api/feed-users/{feedUserId}/reject", feedUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("피드 참여 거절 완료")));
    }

    @Test
    @DisplayName("피드에 참여 신청한 유저 목록을 조회한다.")
    void getPendingUsers() throws Exception {
        // given
        Long feedId = 1L;
        List<FeedUserDto> pendingUsers = List.of(
                new FeedUserDto(1L, feedId, 1L, FeedUserStatus.PENDING),
                new FeedUserDto(2L, feedId, 2L, FeedUserStatus.PENDING)
        );
        given(feedUserService.getPendingUsers(feedId)).willReturn(pendingUsers);

        // when & then
        mockMvc.perform(get("/api/feed-users/{feedId}/pending", feedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].feedId", is(1))) // Long에서 int로 비교
                .andExpect(jsonPath("$.data[0].userId", is(1)))
                .andExpect(jsonPath("$.data[0].status", is(FeedUserStatus.PENDING.name())))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].feedId", is(1))) // Long에서 int로 비교
                .andExpect(jsonPath("$.data[1].userId", is(2)))
                .andExpect(jsonPath("$.data[1].status", is(FeedUserStatus.PENDING.name())));
    }

    @Test
    @DisplayName("피드 참여 승인시 이미 승인된 경우 예외가 발생한다.")
    void approveJoinAlreadyApproved() throws Exception {
        // given
        Long feedUserId = 1L;
        FeedUser feedUser = new FeedUser();
        given(feedUserRepository.findById(feedUserId)).willReturn(Optional.of(feedUser));

        // when & then
        mockMvc.perform(post("/api/feed-users/{feedUserId}/approve", feedUserId))
                .andExpect(status().isBadRequest())  // 이미 승인된 경우
                .andExpect(jsonPath("$.error").value("이미 승인되었습니다."));
    }

    @Test
    @DisplayName("피드 참여 거절시 이미 거절된 경우 예외가 발생한다.")
    void rejectJoinAlreadyRejected() throws Exception {
        // given
        Long feedUserId = 1L;
        FeedUser feedUser = new FeedUser();
        given(feedUserRepository.findById(feedUserId)).willReturn(Optional.of(feedUser));

        // when & then
        mockMvc.perform(post("/api/feed-users/{feedUserId}/reject", feedUserId))
                .andExpect(status().isBadRequest())  // 이미 거절된 경우
                .andExpect(jsonPath("$.error").value("이미 거절되었습니다."));
    }

    @Test
    @DisplayName("피드 참여 대기중인 유저 목록을 조회할 때 예외가 발생한다.")
    void getPendingUsersFeedNotFound() throws Exception {
        // given
        Long invalidFeedId = 999L;  // 존재하지 않는 피드 ID
        given(feedUserRepository.findByFeedIdAndStatus(invalidFeedId, FeedUserStatus.PENDING))
                .willThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/feed-users/{feedId}/pending", invalidFeedId))
                .andExpect(status().isNotFound())  // FeedException 발생
                .andExpect(jsonPath("$.error").value("피드 없음"));
    }

}
