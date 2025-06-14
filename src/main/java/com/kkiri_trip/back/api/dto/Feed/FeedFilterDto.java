package com.kkiri_trip.back.api.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeedFilterDto {
    private String region;
    private String period;
    private String gender;
    private String ageGroup;
    private String tripStyle;
    private String cost;
}
