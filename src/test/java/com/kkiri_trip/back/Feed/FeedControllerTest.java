package com.kkiri_trip.back.Feed;

import com.kkiri_trip.back.api.controller.FeedController;
import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.feed.service.FeedService;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedController.class)
class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedService feedService;

    @MockBean
    private FeedRepository feedRepository;

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
        FeedDto feeds = new FeedDto(1L, "제목1", "내용1");
        given(feedService.getFeedById(1L)).willReturn(feeds);

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
    @DisplayName("피드를 생성한다.")
    void createFeed() throws Exception {
        // given
        FeedDto responseDto = new FeedDto(1L, "제목1", "내용1");
        given(feedService.createFeed(any(FeedDto.class))).willReturn(responseDto);

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
        given(feedService.createFeed(any(FeedDto.class))).willThrow(new FeedException(FeedErrorCode.EMPTY_TITLE));

        // when & then
        mockMvc.perform(post("/api/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\", \"content\": \"내용\"}"))
                .andExpect(status().isBadRequest()) // 400 상태 코드로 수정
                .andExpect(jsonPath("$.message", is("제목은 비어 있을 수 없습니다.")));
    }

    @Test
    @DisplayName("빈 내용으로 피드를 생성하면 예외가 발생해야 한다.") // 중복된 DisplayName 수정
    void createFeedWithInvalidContent() throws Exception {
        // given
        given(feedService.createFeed(any(FeedDto.class))).willThrow(new FeedException(FeedErrorCode.EMPTY_CONTENT));

        // when & then
        mockMvc.perform(post("/api/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목\", \"content\": \"\"}"))
                .andExpect(status().isBadRequest()) // 400 상태 코드로 수정
                .andExpect(jsonPath("$.message", is("내용은 비어 있을 수 없습니다.")));
    }
}
