package com.kkiri_trip.back.domain.user.repository;

import com.kkiri_trip.back.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
