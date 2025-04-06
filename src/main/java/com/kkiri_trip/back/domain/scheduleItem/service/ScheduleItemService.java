package com.kkiri_trip.back.domain.scheduleItem.service;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.feed.service.FeedService;
import com.kkiri_trip.back.domain.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.scheduleItem.entity.ScheduleItem;
import com.kkiri_trip.back.domain.scheduleItem.repository.ScheduleItemRepository;
import com.kkiri_trip.back.domain.scheduleItem.repository.ScheduleItemRepositoryImpl;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleErrorCode;
import com.kkiri_trip.back.global.error.errorcode.ScheduleItemErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.ScheduleException;
import com.kkiri_trip.back.global.error.exception.ScheduleItemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleItemService {

    private final ScheduleItemRepository scheduleItemRepository;
    private final ScheduleItemRepositoryImpl scheduleItemRepositoryImpl;

    private final FeedRepository feedRepository;
    private final ScheduleRepository scheduleRepository;

    public List<ScheduleItemDto> getAllScheduleItemsInFeed(Long feedId) {

        feedRepository.findById(feedId).orElseThrow(()->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        return ScheduleItem.toDtoList(
                scheduleItemRepositoryImpl.findAllByFeedId(feedId));
    }

    public List<ScheduleItemDto> getAllScheduleItemsInSchedule(Long scheduleId) {

        scheduleRepository.findById(scheduleId).orElseThrow(()->
                new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        return ScheduleItem.toDtoList(
                scheduleItemRepositoryImpl.findAllByScheduleId(scheduleId));
    }

    public ScheduleItemDto getScheduleItem(Long feedId,
                                           Long scheduleId,
                                           Long scheduleItemId)
    {
        feedRepository.findById(feedId).orElseThrow(()->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        scheduleRepository.findById(scheduleId).orElseThrow(()->
                new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        return scheduleItemRepository.findById(scheduleItemId).orElseThrow(()->
                new ScheduleItemException(ScheduleItemErrorCode.SCHEDULEITEM_NOT_FOUND))
                .toDto();

    }
}
