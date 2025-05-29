package com.kkiri_trip.back.domain.mongo.place.repository;

import com.kkiri_trip.back.domain.mongo.place.entity.Place;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaceRepository  extends MongoRepository<Place, String> {
}
