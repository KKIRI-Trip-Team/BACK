package com.kkiri_trip.back.Feed;

import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.global.error.exception.FeedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@AutoConfigureMockMvc(addFilters = false)
public class FeedTest {


    private Feed feed;

    @BeforeEach
    void setUp() {
        feed = Feed.builder()
                .title("피드제목")
                .content("피드내용")
                .build();
    }

    @Test
    @DisplayName("피드의 제목을 가져온다")
    void getTitle() {
        assertThat(feed.getTitle()).isEqualTo("피드제목");
    }

    @Test
    @DisplayName("피드의 내용을 가져온다")
    void getContent() {
        assertThat(feed.getContent()).isEqualTo("피드내용");
    }

    @Test
    @DisplayName("피드의 제목을 공백으로 설정해서 예외를 처리한다.")
    void setTitleNull()
    {
        assertThatThrownBy(() -> feed.setTitle(""))
                .isInstanceOf(FeedException.class);
    }

    @Test
    @DisplayName("피드의 내용을 공백으로 설정해서 예외를 처리한다.")
    void setContentNull()
    {
        assertThatThrownBy(() -> feed.setContent(""))
                .isInstanceOf(FeedException.class);
    }

}
