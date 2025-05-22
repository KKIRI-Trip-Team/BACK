package com.kkiri_trip.back.domain.mongo.place.entity;

import com.kkiri_trip.back.api.dto.Place.PlaceDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "places")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {

    @Id
    private String id;
    private String placeName;
    private String addressName;
    private String roadAddressName;
    private String placeUrl;
    private String phone;
    private String categoryName;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    private int distance; // Optional: 검색 기준 거리 값

    public PlaceDto toDto() {
        return new PlaceDto(
                this.getId(),
                this.getPlaceName(),
                this.getAddressName(),
                this.getRoadAddressName(),
                this.getPlaceUrl(),
                this.getCategoryName(),
                this.getPhone(),
                this.getLocation() != null ? this.getLocation().getX() : 0.0,
                this.getLocation() != null ? this.getLocation().getY() : 0.0
        );
    }


    public static List<PlaceDto> toDtoList(List<Place> places) {
        return places.stream()
                .map(Place::toDto)
                .collect(Collectors.toList());
    }
}