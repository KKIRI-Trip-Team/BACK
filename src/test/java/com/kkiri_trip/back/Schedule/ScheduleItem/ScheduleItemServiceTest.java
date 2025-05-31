package com.kkiri_trip.back.Schedule.ScheduleItem;

import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;
import com.kkiri_trip.back.domain.jpa.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.jpa.scheduleItem.entity.ScheduleItem;
import com.kkiri_trip.back.domain.jpa.scheduleItem.repository.ScheduleItemRepository;
import com.kkiri_trip.back.global.error.errorcode.ScheduleItemErrorCode;
import com.kkiri_trip.back.global.error.exception.ScheduleItemException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleItemServiceTest {

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FeedRepository feedRepository;

    @AfterEach
    void tearDown() {
        scheduleItemRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        feedRepository.deleteAllInBatch();
    }

    @DisplayName("ScheduleItem을 생성하고 조회한다")
    @Test
    @Transactional
    void createAndFindScheduleItem() {
        Feed feed = feedRepository.save(Feed.builder()
                .title("테스트 피드")
                .content("내용")
                .build());

        Schedule schedule = scheduleRepository.save(Schedule.builder()
                .dayNumber(1)
                .feed(feed)
                .build());

        ScheduleItem item = scheduleItemRepository.save(ScheduleItem.builder()
                .itemOrder(1)
                .schedule(schedule)
                .build());

        ScheduleItem foundItem = scheduleItemRepository.findById(item.getId())
                .orElseThrow(() -> new ScheduleItemException(ScheduleItemErrorCode.SCHEDULEITEM_NOT_FOUND));

        assertThat(foundItem.getItemOrder()).isEqualTo(1);
        assertThat(foundItem.getSchedule()).isEqualTo(schedule);
    }

    @DisplayName("ScheduleItem 삭제")
    @Test
    void deleteScheduleItem() {
        Feed feed = feedRepository.save(Feed.builder().title("삭제 피드").content("삭제").build());
        Schedule schedule = scheduleRepository.save(Schedule.builder().dayNumber(1).feed(feed).build());

        ScheduleItem item = scheduleItemRepository.save(ScheduleItem.builder().itemOrder(1).schedule(schedule).build());

        scheduleItemRepository.delete(item);

        assertThat(scheduleItemRepository.existsById(item.getId())).isFalse();
    }

    @DisplayName("itemOrder를 0 이하로 설정 시 예외 발생")
    @Test
    void invalidItemOrder() {
        Feed feed = feedRepository.save(Feed.builder().title("피드").content("내용").build());
        Schedule schedule = scheduleRepository.save(Schedule.builder().dayNumber(1).feed(feed).build());

        ScheduleItem item = ScheduleItem.builder().schedule(schedule).build();

        assertThatThrownBy(() -> item.setItemOrder(0))
                .isInstanceOf(ScheduleItemException.class)
                .hasMessage(ScheduleItemErrorCode.INVALID_ITEMORDER.getMessage());
    }

}
