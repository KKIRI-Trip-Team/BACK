package com.kkiri_trip.back.domain.jpa.schedule.service;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleDto;
import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;
import com.kkiri_trip.back.domain.jpa.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.jpa.schedule.repository.ScheduleRepositoryImpl;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleRepositoryImpl scheduleRepositoryImpl;
    private final FeedRepository feedRepository;
    public List<ScheduleDto> getAllSchedules(Long feedId) {

        feedRepository.findById(feedId).orElseThrow(()->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        return Schedule.toDtoList(scheduleRepositoryImpl.findSchedulesByFeedId(feedId));
    }

    public ScheduleDto getScheduleById(Long feedId, Long scheduleId) {
        return scheduleRepositoryImpl.findScheduleByFeedIdAndScheduleId(feedId,scheduleId)
                .orElseThrow(()-> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND))
                .toDto();
    }

    public ScheduleDto createSchedule(Long feedId, ScheduleDto scheduleDto) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        validateFeedDto(scheduleDto);

        Schedule schedule = Schedule.builder()
                .dayNumber(scheduleDto.getDayNumber())
                .feed(feed)
                .build();

        return scheduleRepository.save(schedule).toDto();
    }

    public void validateFeedDto(ScheduleDto scheduleDto)
    {
        if (scheduleDto.getDayNumber() <= 0) {
            throw new ScheduleException(ScheduleErrorCode.INVALID_DAYNUMBER);
        }

        if (scheduleDto.getFeedId() == null || scheduleDto.getFeedId() <= 0) {
            throw new ScheduleException(ScheduleErrorCode.INVALID_FEED);
        }
    }

    public ScheduleDto updateSchedule(Long feedId, Long scheduleId, ScheduleDto scheduleDto) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(() ->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() ->
                new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        validateFeedDto(scheduleDto);

        schedule.setDayNumber(scheduleDto.getDayNumber());
        schedule.setFeed(feed);

        return scheduleRepository.save(schedule).toDto();
    }

    @Transactional
    public void deleteSchedule(Long feedId, Long id) {

        feedRepository.findById(feedId).orElseThrow(()->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        scheduleRepository.deleteById(id);
    }
}
