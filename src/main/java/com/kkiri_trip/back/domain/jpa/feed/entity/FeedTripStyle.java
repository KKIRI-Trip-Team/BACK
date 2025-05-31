package com.kkiri_trip.back.domain.jpa.feed.entity;

import com.kkiri_trip.back.domain.jpa.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedTripStyle extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_style_id")
    private TripStyle tripStyle;

    public static FeedTripStyle of(Feed feed, TripStyle tripStyle) {
        return FeedTripStyle.builder()
                .feed(feed)
                .tripStyle(tripStyle)
                .build();
    }

}