package com.kkiri_trip.back.domain.jpa.feed.service;

import com.kkiri_trip.back.api.dto.feed.FeedDto;
import com.kkiri_trip.back.api.dto.feed.UserDto;
import com.kkiri_trip.back.domain.jpa.feed.entity.Feed;
import com.kkiri_trip.back.domain.jpa.feed.entity.FeedTripStyle;
import com.kkiri_trip.back.domain.jpa.feed.entity.TripStyle;
import com.kkiri_trip.back.domain.jpa.feed.entity.TripStyleType;
import com.kkiri_trip.back.domain.jpa.feed.repository.FeedRepository;
import com.kkiri_trip.back.domain.jpa.feed.repository.TripStyleRepository;
import com.kkiri_trip.back.domain.jpa.feedUser.entity.FeedUser;
import com.kkiri_trip.back.domain.jpa.feedUser.repository.FeedUserRepository;
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

    private final UserRepository userRepository;

    private final FeedUserService feedUserService;

    private final TripStyleRepository tripStyleRepository;

    private final FeedUserRepositoryImpl feedUserRepository;

    @Transactional(readOnly = true)
    public List<FeedDto> getAllFeeds() {
        List<Feed> feeds = feedRepository.findAllWithTripStyles();

        return feeds.stream()
                .map(feed -> {
                    FeedDto feedDto = feed.toDto();

                    // 👇 호스트 조회
                    User host = feedUserRepository.findHostByFeedId(feed.getId());
                    if (host != null) {
                        UserDto hostDto = convertToUserDto(host);
                        feedDto.setOwner(hostDto);
                    } else {
                        feedDto.setOwner(null);
                    }

                    return feedDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FeedDto getFeedById(Long id) {
        Feed feed = feedRepository.findWithTripStylesById(id)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        FeedDto feedDto = feed.toDto();

        User host = feedUserRepository.findHostByFeedId(id);
        if (host != null) {
            feedDto.setOwner(convertToUserDto(host));
        } // host가 없으면 null 그대로

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

        // travelStyles가 List<TravelStyle> 또는 List<String>이라면 변환 필요
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
        // 피드 존재 확인
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

        // 입력 값 검증
        validateFeedDto(feedDto);

        // TripStyle 이름으로 실제 TripStyle 엔티티 조회
        List<FeedTripStyle> newFeedTripStyles = feedDto.getTripStyles().stream()
                .map(name -> {
                    TripStyle tripStyle = tripStyleRepository.findByType(name)
                            .orElseThrow(() -> new FeedException(FeedErrorCode.INVALID_TRIP_STYLE));
                    return FeedTripStyle.of(feed, tripStyle); // 정적 팩토리 메서드 활용
                })
                .collect(Collectors.toList());

        // Dirty Checking
        feed.setTitle(feedDto.getTitle());
        feed.setContent(feedDto.getContent());
        feed.setRegion(feedDto.getRegion());
        feed.setPeriod(feedDto.getPeriod());
        feed.setGender(feedDto.getGender());
        feed.setAgeGroup(feedDto.getAgeGroup());
        feed.setCost(feedDto.getCost());
        feed.setFeedTripStyles(newFeedTripStyles); // 기존 스타일을 새 목록으로 교체

        return feed.toDto(); // 기존의 toDto() 재활용
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
