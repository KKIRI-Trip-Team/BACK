package com.kkiri_trip.back.domain.jpa.scheduleItem.entity;

import com.kkiri_trip.back.api.dto.Schedule.ScheduleItem.ScheduleItemDto;
import com.kkiri_trip.back.domain.jpa.common.entity.BaseEntity;
import com.kkiri_trip.back.domain.jpa.schedule.entity.Schedule;
import com.kkiri_trip.back.global.error.errorcode.ScheduleItemErrorCode;
import com.kkiri_trip.back.global.error.exception.ScheduleItemException;
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
public class ScheduleItem extends BaseEntity {

    @Column(nullable = false)
    private int itemOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;


    public void setItemOrder(int itemOrder) {
        if (itemOrder <= 0 ) {
            throw new ScheduleItemException(ScheduleItemErrorCode.INVALID_ITEMORDER);
        }
        this.itemOrder = itemOrder;
    }

    public void setSchedule(Schedule schedule) {
        if (schedule == null || schedule.getId() == null || schedule.getId() <= 0) {
            throw new ScheduleItemException(ScheduleItemErrorCode.INVALID_SCHEDULE);
        }
        this.schedule = schedule;
    }

    public ScheduleItemDto toDto() {
        return new ScheduleItemDto(this.getId(), this.itemOrder, this.schedule.getId());
    }

    public static List<ScheduleItemDto> toDtoList(List<ScheduleItem> scheduleItems) {
        return scheduleItems.stream()
                .map(ScheduleItem::toDto)
                .collect(Collectors.toList());
    }
}
