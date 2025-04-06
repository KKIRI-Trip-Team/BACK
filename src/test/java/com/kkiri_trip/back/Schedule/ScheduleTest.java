package com.kkiri_trip.back.Schedule;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.domain.schedule.entity.Schedule;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@AutoConfigureMockMvc(addFilters = false)
public class ScheduleTest {

    private Feed feed;
    private Schedule schedule;

    @BeforeEach
    void setUp()
    {
        feed = Feed.builder()
                .title("피드제목")
                .content("피드내용")
                .build();

        schedule = Schedule.builder()
                .dayNumber(1)
                .feed(feed)
                .build();
    }

    @Test
    @DisplayName("스케줄에 대한 피드의 제목을 가져온다")
    void getFeedTitle() {
        assertThat(schedule.getFeed().getTitle()).isEqualTo("피드제목");
    }

    @Test
    @DisplayName("스케줄의 일차를 0 이하로 설정해서 예외를 처리한다.")
    void setDayNumberInvalid()
    {
        assertThatThrownBy(() -> schedule.setDayNumber(0))
                .isInstanceOf(ScheduleException.class);
    }

    @Test
    @DisplayName("스케줄의 피드를 null로 설정해서 예외를 처리한다.")
    void setFeedNull()
    {
        assertThatThrownBy(() -> schedule.setFeed(null))
                .isInstanceOf(ScheduleException.class);
    }
}
