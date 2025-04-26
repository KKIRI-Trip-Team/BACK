package com.kkiri_trip.back.domain.feedUser.repository;

import com.kkiri_trip.back.domain.feedUser.entity.FeedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedUserRepository extends JpaRepository<FeedUser, Long> {
}
