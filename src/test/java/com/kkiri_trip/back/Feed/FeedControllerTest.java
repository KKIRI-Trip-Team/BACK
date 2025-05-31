package com.kkiri_trip.back.Feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkiri_trip.back.api.controller.FeedController;
import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.feed.service.FeedService;
import com.kkiri_trip.back.domain.jpa.feedUser.service.FeedUserService;
import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.kkiri_trip.back.domain.jpa.user.service.UserService;
import com.kkiri_trip.back.domain.jpa.user.util.CustomUserDetails;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedController.class)
@AutoConfigureMockMvc(addFilters = false)
class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeedService feedService;

    @MockitoBean
    private FeedRepository feedRepository;

    @MockitoBean
    private FeedUserService feedUserService;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("피드 목록을 조회한다.")
    void getFeedList() throws Exception {
        // given
        List<FeedDto> feeds = List.of(
                new FeedDto(1L, "제목1", "내용1"),
                new FeedDto(2L, "제목2", "내용2"),
                new FeedDto(3L, "제목3", "내용3")
        );
        given(feedService.getAllFeeds()).willReturn(feeds);

        // when & then
        mockMvc.perform(get("/api/feeds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].title").value("제목1"))
                .andExpect(jsonPath("$.data[1].title").value("제목2"))
                .andExpect(jsonPath("$.data[2].title").value("제목3"));
    }

    @Test
    @DisplayName("피드 id로 피드를 조회한다.")
    void getFeedById() throws Exception {
        // given
        FeedDto feed = new FeedDto(1L, "제목1", "내용1");
        given(feedService.getFeedById(1L)).willReturn(feed);

        // when & then
        mockMvc.perform(get("/api/feeds/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("제목1"))
                .andExpect(jsonPath("$.data.content").value("내용1"));
    }

    @Test
    @DisplayName("피드 id로 피드를 조회할 때, 존재하지 않으면 예외 처리한다.")
    void getFeedByIdException() throws Exception {
        // given
        given(feedService.getFeedById(1L))
                .willThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/feeds/1"))
                .andExpect(status().isNotFound()) // 400 상태 코드로 수정
                .andExpect(jsonPath("$.message", is("해당 피드를 찾을 수 없습니다.")));
    }

    @Test
    @DisplayName("특정 유저가 참여한 피드 목록을 조회한다.")
    void getFeedsByUser() throws Exception {
        // given
        Long userId = 1L; // 테스트용 userId
        User user = new User();

        // 피드 목록 생성
        FeedDto feedDto1 = new FeedDto(1L, "제목1", "내용1");
        FeedDto feedDto2 = new FeedDto(2L, "제목2", "내용2");

        List<FeedDto> feeds = List.of(feedDto1, feedDto2);

        // feedUserService가 특정 user에 대해 피드를 반환하도록 설정
        given(feedUserService.findFeedsByUser(userId)).willReturn(feeds);

        // when & then
        mockMvc.perform(get("/api/feeds/user/{userId}/feeds", userId))  // URL에 userId를 넣어서 호출
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))  // 반환된 데이터의 크기가 2인지 확인
                .andExpect(jsonPath("$.data[0].title").value("제목1"))
                .andExpect(jsonPath("$.data[1].title").value("제목2"));
    }

    @Test
    @DisplayName("특정 피드에 참여한 유저 목록을 조회한다.")
    void getUsersByFeed() throws Exception {
        // given
        Feed feed = new Feed();
        // feed.setId()가 불가능하므로 Feed 객체 생성 시 ID 값을 자동 할당하는 방식 필요

        // 예시로 feed 객체를 mock으로 사용한다고 가정하고
        User user1 = new User();
        User user2 = new User();

        List<User> users = List.of(user1, user2);

        // feedUserService.findUsersByFeed()를 mock 처리
        given(feedUserService.findUsersByFeed(1L)).willReturn(users);

        // when & then
        mockMvc.perform(get("/api/feeds/{feedId}/users", 1L))  // 피드 ID를 받는 엔드포인트 예시
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }



    @Test
    @DisplayName("피드를 생성한다.")
    void createFeed() throws Exception {
        // given
        FeedDto responseDto = new FeedDto(1L, "제목1", "내용1");

        given(feedService.createFeed(any(FeedDto.class))).willReturn(responseDto);
        willDoNothing().given(feedUserService).createFeedUser(any(FeedDto.class), any(CustomUserDetails.class)); // 🔥 추가

        // when & then
        mockMvc.perform(post("/api/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목1\", \"content\": \"내용1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("피드 생성")))
                .andExpect(jsonPath("$.data.title", is("제목1")))
                .andExpect(jsonPath("$.data.content", is("내용1")));
    }

    @Test
    @DisplayName("빈 제목으로 피드를 생성하면 예외가 발생해야 한다.")
    void createFeedWithInvalidTitle() throws Exception {
        // given
        given(feedService.createFeed(any(FeedDto.class)))
                .willThrow(new FeedException(FeedErrorCode.EMPTY_TITLE));

        // when & then
        mockMvc.perform(post("/api/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\", \"content\": \"내용\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("제목은 비어 있을 수 없습니다.")));
    }

    @Test
    @DisplayName("빈 내용으로 피드를 생성하면 예외가 발생해야 한다.")
    void createFeedWithInvalidContent() throws Exception {
        // given
        given(feedService.createFeed(any(FeedDto.class)))
                .willThrow(new FeedException(FeedErrorCode.EMPTY_CONTENT));

        // when & then
        mockMvc.perform(post("/api/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목\", \"content\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("내용은 비어 있을 수 없습니다.")));
    }

    @Test
    @DisplayName("피드 수정을 성공한다.")
    void updateFeedSuccess() throws Exception {
        // given
        Long feedId = 1L;
        FeedDto updateDto = new FeedDto(feedId, "수정된 제목", "수정된 내용");
        String jsonContent = new ObjectMapper().writeValueAsString(updateDto);

        given(feedService.updateFeed(eq(feedId), any(FeedDto.class)))
                .willReturn(updateDto);

        // when & then
        mockMvc.perform(patch("/api/feeds/{id}", feedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(feedId))
                .andExpect(jsonPath("$.data.title").value("수정된 제목"))
                .andExpect(jsonPath("$.data.content").value("수정된 내용"));
    }

    @Test
    @DisplayName("존재하지 않는 피드 업데이트 시 예외가 발생한다.")
    void updateFeedNotFound() throws Exception {
        // given
        FeedDto updateDto = new FeedDto(1L, "새 제목", "새 내용");
        String jsonContent = new ObjectMapper().writeValueAsString(updateDto);

        doThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND))
                .when(feedService).updateFeed(eq(1L), any(FeedDto.class));

        // when & then
        mockMvc.perform(patch("/api/feeds/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("해당 피드를 찾을 수 없습니다.")));
    }

    @Test
    @DisplayName("피드를 정상적으로 삭제한다.")
    void deleteFeedSuccess() throws Exception {
        // given
        Long feedId = 1L;
        doNothing().when(feedService).deleteFeed(feedId);

        // when & then
        mockMvc.perform(delete("/api/feeds/{id}", feedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // HTTP 204 응답
    }

    @Test
    @DisplayName("존재하지 않는 피드를 삭제하면 예외가 발생한다.")
    void deleteFeedNotFound() throws Exception {
        // given
        Long feedId = 999L; // 없는 ID
        doThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND))
                .when(feedService).deleteFeed(feedId);

        // when & then
        mockMvc.perform(delete("/api/feeds/{id}", feedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("해당 피드를 찾을 수 없습니다.")));
    }
}
