package com.kkiri_trip.back.domain.jpa.scheduleItem.service;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.scheduleItem.repository.ScheduleItemRepository;
import com.kkiri_trip.back.domain.jpa.scheduleItem.repository.ScheduleItemRepositoryImpl;
import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;
import com.kkiri_trip.back.domain.jpa.schedule.repository.ScheduleRepository;
import com.kkiri_trip.back.domain.jpa.scheduleItem.entity.ScheduleItem;
import com.kkiri_trip.back.domain.mongo.place.entity.Place;
import com.kkiri_trip.back.domain.mongo.place.repository.PlaceRepository;
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
    private final PlaceRepository placeRepository;

    public List<ScheduleItemDto> getAllScheduleItemsInFeed(Long feedId) {
        validateFeedExistence(feedId);
        return ScheduleItem.toDtoList(scheduleItemRepositoryImpl.findAllByFeedId(feedId));
    }

    public List<ScheduleItemDto> getAllScheduleItemsInSchedule(Long scheduleId) {
        validateScheduleExistence(scheduleId);
        return ScheduleItem.toDtoList(scheduleItemRepositoryImpl.findAllByScheduleId(scheduleId));
    }

    public ScheduleItemDto getScheduleItem(Long feedId, Long scheduleId, Long scheduleItemId) {
        validateFeedExistence(feedId);
        validateScheduleExistence(scheduleId);
        ScheduleItem scheduleItem = getScheduleItemById(scheduleItemId);
        return scheduleItem.toDto();
    }

    public ScheduleItemDto createScheduleItem(Long feedId, Long scheduleId, ScheduleItemDto dto) {
        validateFeedExistence(feedId);
        Schedule schedule = getScheduleById(scheduleId);
        validateItemOrder(dto.getItemOrder());

        Place place = getPlaceById(dto.getPlace());
        ScheduleItem scheduleItem = ScheduleItem.builder()
                .itemOrder(dto.getItemOrder())
                .schedule(schedule)
                .place(place.getId())
                .build();

        return scheduleItemRepository.save(scheduleItem).toDto();
    }

    public ScheduleItemDto updateScheduleItem(Long feedId, Long scheduleId, Long scheduleItemId, ScheduleItemDto dto) {
        validateFeedExistence(feedId);
        Schedule schedule = getScheduleById(scheduleId);
        validateItemOrder(dto.getItemOrder());

        ScheduleItem scheduleItem = getScheduleItemById(scheduleItemId);
        Place place = getPlaceById(dto.getPlace());

        scheduleItem.setItemOrder(dto.getItemOrder());
        scheduleItem.setSchedule(schedule);
        scheduleItem.setPlace(place);

        return scheduleItem.toDto();
    }

    public void deleteScheduleItem(Long feedId, Long scheduleId, Long scheduleItemId) {
        validateFeedExistence(feedId);
        validateScheduleExistence(scheduleId);

        ScheduleItem scheduleItem = getScheduleItemById(scheduleItemId);
        Place place = getPlaceById(scheduleItem.getPlace());

        placeRepository.delete(place);
        scheduleItemRepository.delete(scheduleItem);
    }


    private void validateFeedExistence(Long feedId) {
        if (!feedRepository.existsById(feedId)) {
            throw new FeedException(FeedErrorCode.FEED_NOT_FOUND);
        }
    }

    private void validateScheduleExistence(Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
        }
    }

    private void validateItemOrder(int itemOrder) {
        if (itemOrder <= 0) {
            throw new ScheduleItemException(ScheduleItemErrorCode.INVALID_ITEMORDER);
        }
    }

    private Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    private ScheduleItem getScheduleItemById(Long scheduleItemId) {
        return scheduleItemRepository.findById(scheduleItemId)
                .orElseThrow(() -> new ScheduleItemException(ScheduleItemErrorCode.SCHEDULEITEM_NOT_FOUND));
    }

    private Place getPlaceById(String placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ScheduleItemException(ScheduleItemErrorCode.PLACE_NOT_FOUND));
    }
}
