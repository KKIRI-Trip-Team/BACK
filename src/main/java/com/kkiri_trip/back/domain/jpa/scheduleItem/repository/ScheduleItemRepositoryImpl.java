package com.kkiri_trip.back.domain.jpa.scheduleItem.repository;

import com.kkiri_trip.back.domain.jpa.schedule.entity.QSchedule;
import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;
import com.kkiri_trip.back.domain.jpa.scheduleItem.entity.QScheduleItem;
import com.kkiri_trip.back.domain.jpa.scheduleItem.entity.ScheduleItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kkiri_trip.back.domain.jpa.scheduleItem.entity.QScheduleItem.scheduleItem;

@Repository
@RequiredArgsConstructor
public class ScheduleItemRepositoryImpl implements ScheduleItemCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isItemOrderDuplicated(Schedule schedule, int itemOrder) {
        Integer count = queryFactory
                .selectOne()
                .from(scheduleItem)
                .where(
                        scheduleItem.schedule.eq(schedule),
                        scheduleItem.itemOrder.eq(itemOrder)
                )
                .fetchFirst();

        return count != null;
    }

    @Override
    public List<ScheduleItem> findAllByFeedId(Long feedId) {
        QScheduleItem item = QScheduleItem.scheduleItem;
        QSchedule schedule = QSchedule.schedule;

        return queryFactory
                .selectFrom(item)
                .join(item.schedule, schedule)
                .where(schedule.feed.id.eq(feedId))
                .fetch();
    }

    @Override
    public List<ScheduleItem> findAllByScheduleId(Long scheduleId) {
        QScheduleItem scheduleItem = QScheduleItem.scheduleItem;

        return queryFactory
                .select(scheduleItem)
                .from(scheduleItem)
                .where(scheduleItem.schedule.id.eq(scheduleId))
                .fetch();
    }

}
