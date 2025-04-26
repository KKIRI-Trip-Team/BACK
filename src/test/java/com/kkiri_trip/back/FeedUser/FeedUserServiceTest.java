package com.kkiri_trip.back.FeedUser;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.feedUser.repository.FeedUserRepository;
import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class FeedUserServiceTest {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedUserRepository feedUserRepository;

    @PersistenceContext
    private EntityManager em;

    @AfterEach
    void tearDown() {
        feedUserRepository.deleteAllInBatch();
        feedRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("FeedUser를 생성하고 저장한다.")
    @Test
    void createFeedUser() {
        // given
        Feed feed = feedRepository.save(Feed.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build());

        User user = userRepository.save(User.builder()
                .nickname("홍길동")
                .email("hong@example.com")
                .password("1234")
                .mobile_number("01011111111")
                .name("홍길동")
                .build());

        // when
        FeedUser feedUser = feedUserRepository.save(FeedUser.builder()
                .feed(feed)
                .user(user)
                .build());

        // then
        assertThat(feedUser.getFeed()).isEqualTo(feed);
        assertThat(feedUser.getUser()).isEqualTo(user);
    }

    @DisplayName("FeedUser 리스트를 조회한다.")
    @Transactional
    @Test
    void findAllFeedUsers() {
        // given
        Feed feed = feedRepository.save(Feed.builder()
                .title("피드 제목")
                .content("내용")
                .build());

        User user1 = userRepository.save(User.builder()
                .nickname("유저1")
                .email("user1@example.com")
                .password("pw1")
                .mobile_number("01011111111")
                .name("홍길동")
                .build());

        User user2 = userRepository.save(User.builder()
                .nickname("유저2")
                .email("user2@example.com")
                .password("pw2")
                .mobile_number("01011111111")
                .name("홍길동")
                .build());

        feedUserRepository.saveAll(List.of(
                FeedUser.builder().feed(feed).user(user1).build(),
                FeedUser.builder().feed(feed).user(user2).build()
        ));

        // when
        List<FeedUser> feedUsers = feedUserRepository.findAll();

        // then
        assertThat(feedUsers).hasSize(2)
                .extracting(fu -> fu.getUser().getNickname())
                .containsExactlyInAnyOrder("유저1", "유저2");
    }

    @DisplayName("FeedUser를 삭제한다.")
    @Test
    void deleteFeedUser() {
        // given
        Feed feed = feedRepository.save(Feed.builder()
                .title("삭제용 피드")
                .content("삭제용 내용")
                .build());

        User user = userRepository.save(User.builder()
                .nickname("삭제유저")
                .email("delete@example.com")
                .password("pw")
                .mobile_number("01011111111")
                .name("홍길동")
                .build());

        FeedUser feedUser = feedUserRepository.save(
                FeedUser.builder().feed(feed).user(user).build());

        // when
        feedUserRepository.delete(feedUser);

        // then
        boolean exists = feedUserRepository.existsById(feedUser.getId());
        assertThat(exists).isFalse();
    }
}
