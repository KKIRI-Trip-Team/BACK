package com.kkiri_trip.back.domain.mongo.place.entity;

import com.kkiri_trip.back.api.dto.Place.PlaceDto;
import com.kkiri_trip.back.global.error.errorcode.PlaceErrorCode;
import com.kkiri_trip.back.global.error.exception.PlaceExceptoin;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @NonNull
    private String placeName;

    @NonNull
    private String addressName;

    private String roadAddressName;

    @Indexed(unique = true)
    private String placeUrl;

    private String phone;

    private String categoryName;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    @NonNull
    private GeoJsonPoint location;

    private int distance;

    public PlaceDto toDto() {
        return new PlaceDto(
                this.getId(),
                this.getPlaceName(),
                this.getAddressName(),
                this.getRoadAddressName(),
                this.getPlaceUrl(),
                this.getCategoryName(),
                this.getPhone(),
                this.getLocation().getX(),
                this.getLocation().getY()
        );
    }


    public static List<PlaceDto> toDtoList(List<Place> places) {
        return places.stream()
                .map(Place::toDto)
                .collect(Collectors.toList());
    }

    public static Place of(PlaceDto dto) {
        validate(dto);
        GeoJsonPoint location = new GeoJsonPoint(dto.getX(), dto.getY());

        return Place.builder()
                .placeName(dto.getPlaceName())
                .addressName(dto.getAddressName())
                .roadAddressName(dto.getRoadAddressName())
                .placeUrl(dto.getPlaceUrl())
                .phone(dto.getPhone())
                .categoryName(dto.getCategoryName())
                .location(location)
                .build();
    }

    private static void validate(PlaceDto dto) {
        if (isBlank(dto.getPlaceName())) {
            throw new PlaceExceptoin(PlaceErrorCode.BLANK_PLACE_NAME);
        }
        if (isBlank(dto.getAddressName())) {
            throw new PlaceExceptoin(PlaceErrorCode.BLANK_ADDRESS_NAME);
        }
        if (dto.getX() == 0.0 || dto.getY() == 0.0) {
            throw new PlaceExceptoin(PlaceErrorCode.INVALID_COORDINATES);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}