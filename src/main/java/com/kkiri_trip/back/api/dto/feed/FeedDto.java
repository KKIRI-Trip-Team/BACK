package com.kkiri_trip.back.api.dto.feed;

import com.kkiri_trip.back.api.dto.feed.attribute.AgeGroup;
import com.kkiri_trip.back.api.dto.feed.attribute.Gender;
import com.kkiri_trip.back.api.dto.feed.attribute.Period;
import com.kkiri_trip.back.api.dto.feed.attribute.Region;
import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feed.entity.TripStyleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class FeedDto {
    private Long id;
    private String title;
    private String content;

    private Region region;
    private Period period;
    private Gender gender;
    private AgeGroup ageGroup;
    private Long cost;

    private List<TripStyleType> tripStyles;  // 단순 문자열 리스트 (예: "휴식", "액티비티" 등)

    public static FeedDto from(Feed feed) {
        return FeedDto.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .region(feed.getRegion())
                .period(feed.getPeriod())
                .gender(feed.getGender())
                .ageGroup(feed.getAgeGroup())
                .cost((long) feed.getCost())  // Feed 엔티티 cost가 int라면 Long 변환
                .tripStyles(feed.getFeedTripStyles().stream()
                        .map(ft -> ft.getTripStyle().getType())
                        .collect(Collectors.toList()))
                .build();
    }
}
