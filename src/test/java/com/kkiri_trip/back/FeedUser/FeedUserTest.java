package com.kkiri_trip.back.FeedUser;

import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureMockMvc(addFilters = false)
public class FeedUserTest {

    @Test
    @DisplayName("FeedUser 엔티티 생성 - 성공")
    void createFeedUser_success() {
        // given
        Feed feed = Feed.builder()
                .title("테스트 피드 제목")
                .content("테스트 피드 내용")
                .build();

        User user = User.builder()
                .nickname("홍길동")
                .email("hong@example.com")
                .password("1234")
                .build();

        // when
        FeedUser feedUser = FeedUser.builder()
                .feed(feed)
                .user(user)
                .build();

        // then
        assertThat(feedUser.getFeed()).isEqualTo(feed);
        assertThat(feedUser.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("FeedUser 엔티티 생성 - feed가 null이면 예외 발생")
    void createFeedUser_nullFeed() {
        // given
        User user = User.builder()
                .nickname("홍길동")
                .email("hong@example.com")
                .password("1234")
                .build();

        // when & then
        assertThatThrownBy(() -> FeedUser.builder()
                .feed(null)
                .user(user)
                .build())
                .isInstanceOf(FeedException.class); // 또는 IllegalArgumentException, 커스텀 예외 등
    }

    @Test
    @DisplayName("FeedUser 엔티티 생성 - user가 null이면 예외 발생")
    void createFeedUser_nullUser() {
        // given
        Feed feed = Feed.builder()
                .title("테스트 피드 제목")
                .content("테스트 피드 내용")
                .build();

        // when & then
        assertThatThrownBy(() -> FeedUser.builder()
                .feed(feed)
                .user(null)
                .build())
                .isInstanceOf(UserException.class); // 또는 IllegalArgumentException
    }
}
