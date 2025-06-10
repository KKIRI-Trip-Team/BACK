package com.kkiri_trip.back.domain.jpa.feed.service;

import com.kkiri_trip.back.api.dto.feed.FeedDto;
import com.kkiri_trip.back.api.dto.feed.UserDto;
import com.kkiri_trip.back.domain.jpa.feed.entity.*;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepositoryCustomImpl;
import com.kkiri_trip.back.domain.jpa.feed.repository.TripStyleRepository;
import com.kkiri_trip.back.domain.jpa.feedUser.repository.FeedUserRepositoryImpl;
import com.kkiri_trip.back.domain.jpa.feedUser.service.FeedUserService;
import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.kkiri_trip.back.domain.jpa.user.repository.UserRepository;
import com.kkiri_trip.back.global.common.dto.PageResponseDto;
import com.kkiri_trip.back.global.error.errorcode.FeedErrorCode;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.FeedException;
import com.kkiri_trip.back.global.error.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    private final FeedRepositoryCustomImpl feedRepositoryCustom;

    private final UserRepository userRepository;

    private final FeedUserService feedUserService;

    private final TripStyleRepository tripStyleRepository;

    private final FeedUserRepositoryImpl feedUserRepository;

    @Transactional(readOnly = true)
    public List<FeedDto> getAllFeeds() {
        List<Feed> feeds = feedRepositoryCustom.findAllWithHostAndTripStyles();

        return feeds.stream()
                .map(feed -> {
                    FeedDto feedDto = feed.toDto();

                    // feedId로 호스트 User 조회
                    User owner = feedUserRepository.findHostByFeedId(feed.getId()).orElseThrow(()->
                            new UserException(UserErrorCode.USER_NOT_FOUND));
                        feedDto.setOwner(convertToUserDto(owner));
                    return feedDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FeedDto getFeedById(Long id) {
        Feed feed = feedRepository.findWithTripStylesById(id)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        FeedDto feedDto = feed.toDto();

        User owner = feedUserRepository.findHostByFeedId(feed.getId()).orElseThrow(()->
                new UserException(UserErrorCode.USER_NOT_FOUND));
        feedDto.setOwner(convertToUserDto(owner));

        return feedDto;
    }



    public FeedDto createFeed(FeedDto feedDto, Long userId) {
        validateFeedDto(feedDto);

        Feed feed = Feed.builder()
                .title(feedDto.getTitle())
                .content(feedDto.getContent())
                .region(feedDto.getRegion())
                .period(feedDto.getPeriod())
                .gender(feedDto.getGender())
                .ageGroup(feedDto.getAgeGroup())
                .cost(feedDto.getCost())
                .build();

        // 이미지 URL 리스트를 FeedImage 엔티티 리스트로 변환
        if (feedDto.getImageUrls() != null) {
            List<FeedImage> feedImages = feedDto.getImageUrls().stream()
                    .map(url -> FeedImage.builder()
                            .url(url)
                            .feed(feed) // 연관관계 설정 중요
                            .build())
                    .collect(Collectors.toList());
            feed.setFeedImages(feedImages);
        }

        // TripStyle 변환
        if (feedDto.getTripStyles() != null) {
            List<FeedTripStyle> feedTripStyles = new ArrayList<>();
            for (TripStyleType dtoTripStyle : feedDto.getTripStyles()) {
                TripStyle tripStyle = tripStyleRepository.findByType(dtoTripStyle)
                        .orElseThrow(() -> new FeedException(FeedErrorCode.INVALID_TRIP_STYLE));

                FeedTripStyle fts = FeedTripStyle.builder()
                        .feed(feed)
                        .tripStyle(tripStyle)
                        .build();

                feedTripStyles.add(fts);
            }
            feed.setFeedTripStyles(feedTripStyles);
        }

        Feed savedFeed = feedRepository.save(feed);

        feedUserService.createFeedHost(savedFeed.getId(), userId);
        return savedFeed.toDto();
    }




    @Transactional
    public FeedDto updateFeed(Long id, FeedDto feedDto) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        validateFeedDto(feedDto);

        // TripStyle 변환
        List<FeedTripStyle> newFeedTripStyles = feedDto.getTripStyles().stream()
                .map(name -> {
                    TripStyle tripStyle = tripStyleRepository.findByType(name)
                            .orElseThrow(() -> new FeedException(FeedErrorCode.INVALID_TRIP_STYLE));
                    return FeedTripStyle.of(feed, tripStyle);
                })
                .collect(Collectors.toList());

        // 이미지 URL을 FeedImage 리스트로 변환 및 세팅
        List<FeedImage> newFeedImages = new ArrayList<>();
        if (feedDto.getImageUrls() != null) {
            newFeedImages = feedDto.getImageUrls().stream()
                    .map(url -> FeedImage.builder()
                            .url(url)
                            .feed(feed)
                            .build())
                    .collect(Collectors.toList());
        }

        // 변경 사항 반영
        feed.setTitle(feedDto.getTitle());
        feed.setContent(feedDto.getContent());
        feed.setRegion(feedDto.getRegion());
        feed.setPeriod(feedDto.getPeriod());
        feed.setGender(feedDto.getGender());
        feed.setAgeGroup(feedDto.getAgeGroup());
        feed.setCost(feedDto.getCost());
        feed.setFeedTripStyles(newFeedTripStyles);

        // 기존 이미지 제거 후 새 이미지로 교체 (orphanRemoval = true 시 자동 삭제됨)
        feed.setFeedImages(newFeedImages);

        return feed.toDto();
    }



    private void validateFeedDto(FeedDto feedDto) {
        if (feedDto.getTitle() == null || feedDto.getTitle().trim().isEmpty()) {
            throw new FeedException(FeedErrorCode.EMPTY_TITLE);
        }

        if (feedDto.getContent() == null || feedDto.getContent().trim().isEmpty()) {
            throw new FeedException(FeedErrorCode.EMPTY_CONTENT);
        }

        if (feedDto.getRegion() == null) {
            throw new FeedException(FeedErrorCode.INVALID_REGION);
        }

        if (feedDto.getPeriod() == null) {
            throw new FeedException(FeedErrorCode.INVALID_PERIOD);
        }

        if (feedDto.getGender() == null) {
            throw new FeedException(FeedErrorCode.INVALID_GENDER);
        }

        if (feedDto.getAgeGroup() == null) {
            throw new FeedException(FeedErrorCode.INVALID_AGE_GROUP);
        }

        if (feedDto.getCost() == null || feedDto.getCost() < 0) {
            throw new FeedException(FeedErrorCode.INVALID_COST);
        }

        if (feedDto.getTripStyles() == null || feedDto.getTripStyles().isEmpty()) {
            throw new FeedException(FeedErrorCode.INVALID_TRIP_STYLE); // FeedErrorCode에 새로 추가 필요
        }
    }


    private void validateUser(Long userId)
    {
        userRepository.findById(userId).orElseThrow(()->
                new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void deleteFeed(Long id) {
        feedRepository.findById(id)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        feedRepository.deleteById(id);
    }

    public PageResponseDto<FeedDto> getFeeds(String keyword, Pageable pageable){
        Page<Feed> feedPage = feedRepository.searchFeeds(keyword, pageable);
        Page<FeedDto> dtoPage = feedPage.map(FeedDto::from); // Feed → FeedDto 변환
        return new PageResponseDto<>(dtoPage); // Page<FeedDto>로 감싸기
    }

    // TODO : 게시글에 대한 정홗한 데이터 나오면 DTO 생성 후 응답 값 수정
    public PageResponseDto<FeedDto> getMyFeeds(Long userId, Pageable pageable){
        Page<Feed> feedPage = feedRepository.findMyFeeds(userId, pageable);
        Page<FeedDto> dtoPage = feedPage.map(FeedDto::from);
        return new PageResponseDto<>(dtoPage);
    }

    private UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .profileUrl(user.getUserProfile().getProfileUrl() != null ? user.getUserProfile().getProfileUrl() : null)
                .build();
    }
}
