package com.kkiri_trip.back.domain.jpa.feed.entity;

import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.domain.jpa.common.entity.BaseEntity;
import com.kkiri_trip.back.api.dto.Feed.attribute.AgeGroup;
import com.kkiri_trip.back.api.dto.Feed.attribute.Gender;
import com.kkiri_trip.back.api.dto.Feed.attribute.Period;
import com.kkiri_trip.back.api.dto.Feed.attribute.Region;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Feed extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private Period period;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(nullable = false)
    private Long cost;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FeedTripStyle> feedTripStyles = new ArrayList<>();

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new FeedException(FeedErrorCode.EMPTY_TITLE);
        }
        this.title = title;
    }

    public void setContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new FeedException(FeedErrorCode.EMPTY_CONTENT);
        }
        this.content = content;
    }

    public void setRegion(Region region) {
        if (region == null) {
            throw new FeedException(FeedErrorCode.INVALID_REGION);
        }
        this.region = region;
    }

    public void setPeriod(Period period) {
        if (period == null) {
            throw new FeedException(FeedErrorCode.INVALID_PERIOD);
        }
        this.period = period;
    }

    public void setGender(Gender gender) {
        if (gender == null) {
            throw new FeedException(FeedErrorCode.INVALID_GENDER);
        }
        this.gender = gender;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        if (ageGroup == null) {
            throw new FeedException(FeedErrorCode.INVALID_AGE_GROUP);
        }
        this.ageGroup = ageGroup;
    }

    public void setCost(Long cost) {
        if (cost == null || cost < 0) {
            throw new FeedException(FeedErrorCode.INVALID_COST);
        }
        this.cost = cost;
    }

    public void setFeedTripStyles(List<FeedTripStyle> styles) {
        if(!this.feedTripStyles.isEmpty())
        {
            this.feedTripStyles.clear();
        }
        this.feedTripStyles.clear();
        if (styles != null) {
            this.feedTripStyles.addAll(styles);
        }
    }

    // Feed 엔티티 내
    public FeedDto toDto() {
        return FeedDto.builder()
                .id(this.getId())
                .title(this.title)
                .content(this.content)
                .region(this.region)
                .period(this.period)
                .gender(this.gender)
                .ageGroup(this.ageGroup)
                .cost(this.cost)
                .tripStyles(this.feedTripStyles.stream()
                        .map(ft -> ft.getTripStyle() != null ? ft.getTripStyle().getName() : "")
                        .collect(Collectors.toList()))
                .build();
    }

    public static List<FeedDto> toDtoList(List<Feed> feeds) {
        return feeds.stream()
                .map(Feed::toDto)
                .collect(Collectors.toList());
    }
}
