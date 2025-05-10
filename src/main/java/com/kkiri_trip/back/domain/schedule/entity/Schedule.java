package com.kkiri_trip.back.domain.schedule.entity;

import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.api.dto.Schedule.ScheduleDto;
import com.kkiri_trip.back.domain.common.entity.BaseEntity;
import com.kkiri_trip.back.domain.feed.entity.Feed;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends BaseEntity {

    @Column(nullable = false)
    private int dayNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    public void setDayNumber(int dayNumber) {
        if (dayNumber <= 0) {
            throw new ScheduleException(ScheduleErrorCode.INVALID_DAYNUMBER);
        }
        this.dayNumber = dayNumber;
    }

    public void setFeed(Feed feed) {
        if (feed == null || feed.getId() == null || feed.getId() <= 0) {
            throw new ScheduleException(ScheduleErrorCode.INVALID_FEED);
        }
        this.feed = feed;
    }

    public ScheduleDto toDto() {
        return new ScheduleDto(this.getId(), this.dayNumber, this.feed.getId());
    }

    public static List<ScheduleDto> toDtoList(List<Schedule> schedules) {
        return schedules.stream()
                .map(Schedule::toDto)
                .collect(Collectors.toList());
    }
}
