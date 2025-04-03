package com.kkiri_trip.back.Schedule;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.schedule.entity.Schedule;
import com.kkiri_trip.back.domain.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ActiveProfiles("test")
@SpringBootTest
public class ScheduleServiceTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FeedRepository feedRepository;

    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAllInBatch();
    }

    @DisplayName("특정 ID로 스케줄을 조회한다.")
    @Test
    @Transactional // lazy
    void findScheduleById() {
        // given
        Feed feed = feedRepository.save(Feed.builder()
                .title("피드제목")
                .content("피드내용")
                .build());

        Schedule schedule = scheduleRepository.save(Schedule.builder()
                .dayNumber(1)
                .feed(feed)
                .build());

        // when
        Schedule foundSchedule = scheduleRepository.findById(schedule.getId())
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        // then
        assertThat(foundSchedule.getDayNumber()).isEqualTo(1);
        assertThat(foundSchedule.getFeed()).isEqualTo(feed);
    }

    @DisplayName("존재하지 않는 ID로 스케줄을 조회하면 예외가 발생한다.")
    @Test
    void findScheduleByIdNotFound() {
        // when & then
        assertThatThrownBy(() -> scheduleRepository.findById(999L)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND)))
                .isInstanceOf(ScheduleException.class)
                .hasMessage("해당 스케줄을 찾을 수 없습니다.");
    }

    @DisplayName("모든 스케줄을 가져온다.")
    @Test
    @Transactional
    void findAllFeed()
    {
        // given
        Feed feed1 = feedRepository.save(Feed.builder()
                .title("피드제목1")
                .content("피드내용1")
                .build());

        Feed feed2 = feedRepository.save(Feed.builder()
                .title("피드제목2")
                .content("피드내용2")
                .build());

        feedRepository.flush();

        Schedule schedule1 = Schedule.builder().dayNumber(1).feed(feed1).build();
        Schedule schedule2 = Schedule.builder().dayNumber(2).feed(feed1).build();
        Schedule schedule3 = Schedule.builder().dayNumber(3).feed(feed1).build();
        Schedule schedule4 = Schedule.builder().dayNumber(1).feed(feed2).build();
        Schedule schedule5 = Schedule.builder().dayNumber(2).feed(feed2).build();
        Schedule schedule6 = Schedule.builder().dayNumber(3).feed(feed2).build();

        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6));
        scheduleRepository.flush();

        // when
        List<Schedule> schedules = scheduleRepository.findAll();

        // then
        assertThat(schedules).hasSize(6)
                .extracting("dayNumber", "feed")
                .containsExactlyInAnyOrder(
                        tuple(1, feed1),
                        tuple(2, feed1),
                        tuple(3, feed1),
                        tuple(1, feed2),
                        tuple(2, feed2),
                        tuple(3, feed2)
                );
    }

    @DisplayName("스케줄의 일차를 업데이트한다.")
    @Transactional
    @Test
    void updateFeedTitle() {
        // given
        Feed feed = feedRepository.save(Feed.builder()
                .title("피드제목1")
                .content("피드내용1")
                .build());

        Schedule schedule = scheduleRepository.save(Schedule.builder()
                .dayNumber(1)
                .feed(feed)
                .build());

        // when
        schedule.setDayNumber(2);

        // then
        Schedule updatedSchedule = scheduleRepository.findById(schedule.getId()).get();
        assertThat(updatedSchedule.getDayNumber()).isEqualTo(2);
    }

    @DisplayName("스케줄을 삭제한다.")
    @Test
    void deleteFeed() {
        // given
        Feed feed = feedRepository.save(Feed.builder()
                .title("피드제목1")
                .content("피드내용1")
                .build());

        Schedule schedule = scheduleRepository.save(Schedule.builder()
                .dayNumber(1)
                .feed(feed)
                .build());

        // when
        scheduleRepository.delete(schedule);

        // then
        boolean exists = scheduleRepository.existsById(schedule.getId());
        assertThat(exists).isFalse();
    }

}
