package com.kkiri_trip.back.domain.jpa.dashboard.repository;

import com.kkiri_trip.back.domain.jpa.dashboard.entity.Dashboard;
import com.kkiri_trip.back.domain.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long>, DashboardRepositoryCustom {
    Optional<Dashboard> findByUser(User user);

    boolean existsByUser(User user);
}
