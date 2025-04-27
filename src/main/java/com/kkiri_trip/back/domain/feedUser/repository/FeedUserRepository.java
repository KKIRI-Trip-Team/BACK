package com.kkiri_trip.back.domain.feedUser.repository;

import com.kkiri_trip.back.domain.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.feedUser.entity.FeedUserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedUserRepository extends JpaRepository<FeedUser, Long> {
    List<FeedUser> findByFeedIdAndStatus(Long feedId, FeedUserStatus status);
}
