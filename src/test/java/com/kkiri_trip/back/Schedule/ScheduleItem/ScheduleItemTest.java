package com.kkiri_trip.back.Schedule.ScheduleItem;

import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;
import com.kkiri_trip.back.domain.jpa.scheduleItem.entity.ScheduleItem;
import com.kkiri_trip.back.global.error.exception.ScheduleItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureMockMvc(addFilters = false)
public class ScheduleItemTest {

    private Feed feed;
    private Schedule schedule;
    private ScheduleItem scheduleItem;

    @BeforeEach
    void setUp() {
        feed = Feed.builder()
                .title("피드제목")
                .content("피드내용")
                .build();

        schedule = Schedule.builder()
                .dayNumber(1)
                .feed(feed)
                .build();

        scheduleItem = ScheduleItem.builder()
                .schedule(schedule)
                .itemOrder(1)
                .build();
    }

    @Test
    @DisplayName("ScheduleItem 생성 시 정상 값 확인")
    void createScheduleItemSuccessfully() {
        assertThat(scheduleItem.getSchedule().getFeed().getTitle()).isEqualTo("피드제목");
        assertThat(scheduleItem.getItemOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("itemOrder가 0 이하이면 예외 발생")
    void setItemOrderInvalid() {
        assertThatThrownBy(() -> scheduleItem.setItemOrder(0))
                .isInstanceOf(ScheduleItemException.class);
    }

    @Test
    @DisplayName("schedule이 null이면 예외 발생")
    void setScheduleInvalid() {
        assertThatThrownBy(() -> scheduleItem.setSchedule(null))
                .isInstanceOf(ScheduleItemException.class);
    }
}