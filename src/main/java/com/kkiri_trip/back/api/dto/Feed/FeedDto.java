package com.kkiri_trip.back.api.dto.Feed;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedDto {
    private Long id;
    private String title;
    private String content;
}
