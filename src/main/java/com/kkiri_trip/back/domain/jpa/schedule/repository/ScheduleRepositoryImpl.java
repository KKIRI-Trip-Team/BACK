package com.kkiri_trip.back.domain.jpa.schedule.repository;

import static com.kkiri_trip.back.domain.jpa.feed.entity.QFeed.feed;
import static com.kkiri_trip.back.domain.jpa.schedule.entity.QSchedule.schedule;
import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Schedule> findSchedulesByFeedId(Long feedId) {
        return jpaQueryFactory
                .selectFrom(schedule)
                .innerJoin(schedule.feed, feed).fetchJoin()
                .where(schedule.feed.id.eq(feedId))
                .orderBy(schedule.dayNumber.asc())  // 오름차순 정렬 추가
                .fetch();
    }

    @Override
    public Optional<Schedule> findScheduleByFeedIdAndScheduleId(Long feedId, Long scheduleId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(schedule)
                        .innerJoin(schedule.feed, feed).fetchJoin()
                        .where(
                                schedule.feed.id.eq(feedId),
                                schedule.id.eq(scheduleId)  // 특정 스케줄 ID 필터링
                        )
                        .fetchOne()
        );
    }
}
