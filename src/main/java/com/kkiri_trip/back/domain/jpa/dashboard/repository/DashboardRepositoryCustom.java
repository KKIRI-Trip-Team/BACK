package com.kkiri_trip.back.domain.jpa.dashboard.repository;

import com.kkiri_trip.back.domain.jpa.dashboard.entity.Dashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DashboardRepositoryCustom {
    Page<Dashboard> searchDashboards(String nickname, Pageable pageable);
}
