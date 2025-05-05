package com.kkiri_trip.back.api.dto.Feed;

import com.kkiri_trip.back.domain.feed.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeedDto {
    private Long id;
    private String title;
    private String content;

    public static FeedDto from(Feed feed) {
        return FeedDto.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .build();
    }


}
