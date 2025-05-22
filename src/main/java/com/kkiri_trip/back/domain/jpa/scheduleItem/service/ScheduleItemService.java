package com.kkiri_trip.back.domain.jpa.scheduleItem.service;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.scheduleItem.repository.ScheduleItemRepository;
import com.kkiri_trip.back.domain.jpa.scheduleItem.repository.ScheduleItemRepositoryImpl;
import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;
import com.kkiri_trip.back.domain.jpa.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.jpa.scheduleItem.entity.ScheduleItem;
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

    public ScheduleItemDto createScheduleItem(Long feedId, Long scheduleId, ScheduleItemDto scheduleItemDto) {

        feedRepository.findById(feedId).orElseThrow(()->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()->
                new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        validateScheduleDto(scheduleItemDto.getItemOrder());

        ScheduleItem scheduleItem = ScheduleItem.builder()
                        .itemOrder(scheduleItemDto.getItemOrder())
                        .schedule(schedule)
                        .build();
        return scheduleItemRepository.save(scheduleItem).toDto();
    }

    void validateScheduleDto(int itemOrder)
    {
        if (itemOrder <= 0 )
            throw new ScheduleItemException(ScheduleItemErrorCode.INVALID_ITEMORDER);
    }

    public ScheduleItemDto updateScheduleItem(Long feedId,
                                          Long scheduleId,
                                          Long scheduleItemId,
                                          ScheduleItemDto scheduleItemDto) {
        feedRepository.findById(feedId).orElseThrow(()->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()->
                new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        validateScheduleDto(scheduleItemDto.getItemOrder());

        ScheduleItem scheduleItem = scheduleItemRepository.findById(scheduleItemId).orElseThrow(()->
                new ScheduleItemException(ScheduleItemErrorCode.SCHEDULEITEM_NOT_FOUND));

        scheduleItem.setItemOrder(scheduleItemDto.getItemOrder());
        scheduleItem.setSchedule(schedule);

        return scheduleItem.toDto();
    }

    public void deleteScheduleItem(Long feedId,
                               Long scheduleId,
                               Long scheduleItemId) {

        feedRepository.findById(feedId).orElseThrow(()->
                new FeedException(FeedErrorCode.FEED_NOT_FOUND));

         scheduleRepository.findById(scheduleId).orElseThrow(()->
                new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        ScheduleItem scheduleItem = scheduleItemRepository.findById(scheduleItemId).orElseThrow(()->
                new ScheduleItemException(ScheduleItemErrorCode.SCHEDULEITEM_NOT_FOUND));

        scheduleItemRepository.delete(scheduleItem);
    }
}
