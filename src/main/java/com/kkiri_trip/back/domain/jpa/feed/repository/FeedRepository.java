package com.kkiri_trip.back.domain.jpa.feed.repository;

import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
}
