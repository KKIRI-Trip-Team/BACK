package com.kkiri_trip.back.domain.mongo.place.service;

import com.kkiri_trip.back.api.dto.Place.PlaceDto;
import com.kkiri_trip.back.domain.mongo.place.entity.Place;
import com.kkiri_trip.back.domain.mongo.place.repository.PlaceRepository;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.PlaceErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.PlaceExceptoin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceDto savePlace(PlaceDto placeDto) {
        Place place = Place.of(placeDto);
        return placeRepository.save(place).toDto();
    }


    public List<PlaceDto> getAllPlaces() {
        return Place.toDtoList(placeRepository.findAll()) ;
    }

    public PlaceDto getPlaceById(String id) {
        return placeRepository.findById(id).orElseThrow(()->
                new PlaceExceptoin(PlaceErrorCode.PLACE_NOT_FOUND)).toDto();
    }

    @Transactional
    public PlaceDto updatePlace(String id, PlaceDto placeDto) {
        placeRepository.findById(id).orElseThrow(() ->
                new PlaceExceptoin(PlaceErrorCode.PLACE_NOT_FOUND));

        Place updatedPlace = Place.of(placeDto);
        updatedPlace.setId(id);

        placeRepository.save(updatedPlace);

        return updatedPlace.toDto();
    }


    @Transactional
    public void deleteFeed(String id) {
        placeRepository.findById(id)
                .orElseThrow(() -> new PlaceExceptoin(PlaceErrorCode.PLACE_NOT_FOUND));

        placeRepository.deleteById(id);
    }
}