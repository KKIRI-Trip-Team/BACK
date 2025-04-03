package com.kkiri_trip.back.Feed;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ActiveProfiles("test")
@SpringBootTest
public class FeedServiceTest {

    @Autowired
    private FeedRepository feedRepository;

    @AfterEach
    void tearDown() {
        feedRepository.deleteAllInBatch();
    }

    @DisplayName("특정 ID로 피드를 조회한다.")
    @Test
    void findFeedById() {
        // given
        Feed feed = feedRepository.save(Feed.builder()
                .title("피드제목")
                .content("피드내용")
                .build());

        // when
        Feed foundFeed = feedRepository.findById(feed.getId())
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        // then
        assertThat(foundFeed)
                .extracting("title", "content")
                .containsExactly("피드제목", "피드내용");
    }

    @DisplayName("존재하지 않는 ID로 피드를 조회하면 예외가 발생한다.")
    @Test
    void findFeedByIdNotFound() {
        // when & then
        assertThatThrownBy(() -> feedRepository.findById(999L)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND)))
                .isInstanceOf(FeedException.class)
                .hasMessage("해당 피드를 찾을 수 없습니다.");
    }


    @DisplayName("모든 피드를 가져온다.")
    @Test
    void findAllFeed()
    {
        // given
        Feed feed1 = Feed.builder()
                .title("피드제목1")
                .content("피드내용1")
                .build();

        Feed feed2 = Feed.builder()
                .title("피드제목2")
                .content("피드내용2")
                .build();

        feedRepository.saveAll(List.of(feed1, feed2));

        // when
        List<Feed> feeds = feedRepository.findAll();

        // then
        assertThat(feeds).hasSize(2)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("피드제목1", "피드내용1"),
                        tuple("피드제목2", "피드내용2")
                );
    }

    @DisplayName("피드의 제목을 업데이트한다.")
    @Transactional
    @Test
    void updateFeedTitle() {
        // given
        Feed feed = feedRepository.save(Feed.builder()
                .title("원래 제목")
                .content("내용")
                .build());

        // when
        feed.setTitle("수정된 제목");

        // then
        Feed updatedFeed = feedRepository.findById(feed.getId()).orElseThrow();
        assertThat(updatedFeed.getTitle()).isEqualTo("수정된 제목"); // ✅ 성공
    }

    @DisplayName("피드를 삭제한다.")
    @Test
    void deleteFeed() {
        // given
        Feed feed = feedRepository.save(Feed.builder()
                .title("삭제할 피드")
                .content("삭제할 피드 내용")
                .build());

        // when
        feedRepository.delete(feed);

        // then
        boolean exists = feedRepository.existsById(feed.getId());
        assertThat(exists).isFalse();
    }
}
