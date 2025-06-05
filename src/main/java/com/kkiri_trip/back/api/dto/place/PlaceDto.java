package com.kkiri_trip.back.api.dto.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("road_address_name")
    private String roadAddressName;

    @JsonProperty("place_url")
    private String placeUrl;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("x")
    private double x;

    @JsonProperty("y")
    private double y;
}
