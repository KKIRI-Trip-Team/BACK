package com.kkiri_trip.back.domain.jpa.dashboard.repository;

import com.kkiri_trip.back.domain.jpa.dashboard.entity.Dashboard;
import com.kkiri_trip.back.domain.jpa.dashboard.entity.QDashboard;
import com.kkiri_trip.back.domain.jpa.user.entity.QUser;
import com.kkiri_trip.back.domain.jpa.user.entity.QUserProfile;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DashboardRepositoryCustomImpl implements  DashboardRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Dashboard> searchDashboards(String nickname, Pageable pageable) {
        QDashboard dashboard = QDashboard.dashboard;
        QUser user = QUser.user;
        QUserProfile userProfile = QUserProfile.userProfile;

        List<Dashboard> content = jpaQueryFactory
                .selectFrom(dashboard)
                .join(dashboard.user, user).fetchJoin()
                .join(user.userProfile, userProfile).fetchJoin()
                .where(userProfile.nickname.containsIgnoreCase(nickname))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(dashboard.count())
                .from(dashboard)
                .join(dashboard.user, user)
                .join(user.userProfile, userProfile)
                .where(userProfile.nickname.containsIgnoreCase(nickname))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}
