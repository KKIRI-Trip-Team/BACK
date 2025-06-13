package com.kkiri_trip.back.domain.jpa.feed.repository;

import com.kkiri_trip.back.api.dto.feed.FeedFilterDto;
import com.kkiri_trip.back.api.dto.feed.attribute.AgeGroup;
import com.kkiri_trip.back.api.dto.feed.attribute.Gender;
import com.kkiri_trip.back.api.dto.feed.attribute.Period;
import com.kkiri_trip.back.api.dto.feed.attribute.Region;
import com.kkiri_trip.back.domain.jpa.feed.entity.*;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.QFeedUser;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.kkiri_trip.back.domain.jpa.feed.entity.QTripStyle.tripStyle;

@RequiredArgsConstructor
public class FeedRepositoryCustomImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Feed> searchFeeds(String keyword, Pageable pageable) {
        QFeed feed = QFeed.feed;

        List<Feed> content = jpaQueryFactory
                .selectFrom(feed)
                .where(
                        feed.title.containsIgnoreCase(keyword)
                                .or(feed.content.containsIgnoreCase(keyword))
                )
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(feed.count())
                .from(feed)
                .where(feed.title.containsIgnoreCase(keyword)
                        .or(feed.content.containsIgnoreCase(keyword)))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    // TODO : 게시글에 대한 정홗한 데이터 나오면 DTO 생성 후 응답 값 수정
    @Override
    public Page<Feed> findMyFeeds(Long userId, Pageable pageable) {
        QFeed feed = QFeed.feed;
        QFeedUser feedUser = QFeedUser.feedUser;

        List<Feed> content = jpaQueryFactory
                .select(feed)
                .from(feedUser)
                .join(feedUser.feed, feed)
                .where(feedUser.user.id.eq(userId)
                        .and(feedUser.isHost.isTrue()))
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(feed.count())
                .from(feedUser)
                .join(feedUser.feed, feed)
                .where(feedUser.user.id.eq(userId)
                        .and(feedUser.isHost.isTrue()))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Feed> findAllWithTripStyles() {
        QFeed feed = QFeed.feed;
        QFeedTripStyle feedTripStyle = QFeedTripStyle.feedTripStyle;

        return jpaQueryFactory.selectFrom(feed)
                .leftJoin(feed.feedTripStyles, feedTripStyle).fetchJoin()
                .leftJoin(feedTripStyle.tripStyle, tripStyle).fetchJoin()  // 추가
                .distinct()
                .fetch();
    }

    public Optional<Feed> findWithTripStylesById(Long id) {
        QFeed feed = QFeed.feed;
        QFeedTripStyle feedTripStyle = QFeedTripStyle.feedTripStyle;
        QTripStyle tripStyle = QTripStyle.tripStyle;

        Feed result = jpaQueryFactory.selectFrom(feed)
                .leftJoin(feed.feedTripStyles, feedTripStyle).fetchJoin()
                .leftJoin(feedTripStyle.tripStyle, tripStyle).fetchJoin()
                .where(feed.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public Page<Feed> filterFeeds(FeedFilterDto feedFilterRequestDto, Pageable pageable) {
        QFeed feed = QFeed.feed;
        QFeedTripStyle feedTripStyle = QFeedTripStyle.feedTripStyle;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (feedFilterRequestDto.getRegion() != null) {
            try {
                booleanBuilder.and(feed.region.eq(Region.valueOf(feedFilterRequestDto.getRegion().toUpperCase())));
            } catch (IllegalArgumentException e) {
                throw new FeedException(FeedErrorCode.INVALID_REGION);
            }
        }
        if (feedFilterRequestDto.getPeriod() != null) {
            try {
                booleanBuilder.and(feed.period.eq(Period.valueOf(feedFilterRequestDto.getPeriod().toUpperCase())));
            } catch (IllegalArgumentException e) {
                throw new FeedException(FeedErrorCode.INVALID_PERIOD);
            }
        }
        if (feedFilterRequestDto.getGender() != null) {
            try {
                booleanBuilder.and(feed.gender.eq(Gender.valueOf(feedFilterRequestDto.getGender().toUpperCase())));
            } catch (IllegalArgumentException e) {
                throw new FeedException(FeedErrorCode.INVALID_GENDER);
            }
        }
        if (feedFilterRequestDto.getAgeGroup() != null) {
            try {
                booleanBuilder.and(feed.ageGroup.eq(AgeGroup.valueOf(feedFilterRequestDto.getAgeGroup().toUpperCase())));
            } catch (IllegalArgumentException e) {
                throw new FeedException(FeedErrorCode.INVALID_AGE_GROUP);
            }
        }
        if (feedFilterRequestDto.getTripStyle() != null) {
            try {
                TripStyleType tripStyleEnum = TripStyleType.valueOf(feedFilterRequestDto.getTripStyle().toUpperCase());
                booleanBuilder.and(feed.feedTripStyles.any().tripStyle.type.eq(tripStyleEnum));
            } catch (IllegalArgumentException e) {
                throw new FeedException(FeedErrorCode.INVALID_TRIP_STYLE);
            }
        }
        if (feedFilterRequestDto.getCost() != null) {
            try {
                Long cost = Long.parseLong(feedFilterRequestDto.getCost());
                booleanBuilder.and(feed.cost.eq(cost));
            } catch (NumberFormatException e) {
                throw new FeedException(FeedErrorCode.INVALID_COST);
            }
        }

        List<Feed> content = jpaQueryFactory
                .selectFrom(feed)
                .distinct()
                .leftJoin(feed.feedTripStyles, feedTripStyle).fetchJoin()
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(feed.createdAt.desc())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(feed.count())
                .from(feed)
                .where(booleanBuilder)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);

    }
}
