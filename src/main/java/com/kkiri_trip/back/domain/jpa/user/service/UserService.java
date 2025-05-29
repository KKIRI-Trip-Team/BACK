package com.kkiri_trip.back.domain.user.service;

import com.kkiri_trip.back.domain.jpa.dashboard.entity.Dashboard;
import com.kkiri_trip.back.domain.jpa.dashboard.repository.DashboardRepository;
import com.kkiri_trip.back.domain.jpa.user.dto.Request.LoginRequestDto;
import com.kkiri_trip.back.domain.jpa.user.dto.Request.SignUpRequestDto;
import com.kkiri_trip.back.domain.jpa.user.dto.Request.UserProfileCreateRequestDto;
import com.kkiri_trip.back.domain.jpa.user.dto.Request.UserUpdateRequestDto;
import com.kkiri_trip.back.domain.jpa.user.dto.Response.LoginResponseDto;
import com.kkiri_trip.back.domain.jpa.user.dto.Response.SignUpResponseDto;
import com.kkiri_trip.back.domain.jpa.user.dto.Response.UserResponseDto;
import com.kkiri_trip.back.domain.jpa.user.dto.Response.UserUpdateResponseDto;
import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.kkiri_trip.back.domain.jpa.user.entity.UserProfile;
import com.kkiri_trip.back.domain.jpa.user.repository.UserProfileRepository;
import com.kkiri_trip.back.domain.jpa.user.repository.UserRepository;
import com.kkiri_trip.back.global.jwt.JwtUtil;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.UserException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final String DEFAULT_PROFILE_URL = "";
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserProfileRepository userProfileRepository;
    private final DashboardService dashboardService;
    private final DashboardRepository dashboardRepository;

    @Transactional
    public SignUpResponseDto register(SignUpRequestDto signupRequestDto){
        // 이메일 중복 검사
        if(userRepository.existsByEmail(signupRequestDto.getEmail())){
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호와 확인 비밀번호 일치 검사
        if(!signupRequestDto.getPassword().equals(signupRequestDto.getConfirmPassword())){
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }

        User user = User.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build();

        userRepository.save(user);

        return new SignUpResponseDto(user.getId(), user.getEmail());
    }

    @Transactional
    public void registerProfile(UserProfileCreateRequestDto userProfileCreateRequestDto) {
        User user = userRepository.findByEmail(userProfileCreateRequestDto.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        String profileUrl = userProfileCreateRequestDto.getProfileUrl();

        // 닉네임 중복 검사
        if (userProfileRepository.existsByNickname(userProfileCreateRequestDto.getNickname())){
            throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
        }

        if(userProfileCreateRequestDto.getNickname() == null || userProfileCreateRequestDto.getNickname().isEmpty()){
            throw new UserException(UserErrorCode.EMPTY_NICKNAME);
        }

        if(profileUrl == null || profileUrl.isEmpty()){
            profileUrl = DEFAULT_PROFILE_URL;
        }

        UserProfile userProfile = user.getUserProfile();
        if(userProfile == null){
            userProfile = new UserProfile();
            userProfile.setUser(user);
            user.setUserProfile(userProfile);
        }

        userProfile.createProfile(userProfileCreateRequestDto.getNickname(), profileUrl);

        userRepository.save(user);

        if (user.getDashboard() == null) {
            dashboardService.createDashboard(user);
        }
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        UserProfile userProfile = user.getUserProfile();

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // RefreshToken Redis 저장 (Key: refresh:{userId})
        stringRedisTemplate.opsForValue().set(
                "refresh:" + user.getId(),
                refreshToken,
                14, TimeUnit.DAYS // 만료시간 설정
        );

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return new LoginResponseDto(user.getId(), user.getEmail(),userProfile.getNickname(), userProfile.getProfileUrl(), accessToken);
    }

    public void logout(String accessToken){
        // 남은 유효시간 계산
        long remainingTime = jwtUtil.getRemainingTime(accessToken);

        // 블랙리스트 등록
        stringRedisTemplate.opsForValue().set(
                "blacklist:" + accessToken,
                "logout",
                remainingTime,
                TimeUnit.MILLISECONDS
        );

    }

    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(user -> UserResponseDto.from(user, user.getUserProfile()))
                .toList();
    }

    public UserResponseDto getMyInfo(User user, UserProfile userProfile) {
        return UserResponseDto.from(user, userProfile);
    }

    @Transactional
    public UserUpdateResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto, User loginUser){
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        UserProfile userProfile = user.getUserProfile();

        if(!user.getId().equals(loginUser.getId())){
            throw new UserException(UserErrorCode.UNAUTHORIZED_UPDATE);
        }

        // 이메일(변경 x)

        // 닉네임(변경 O)
        String newNickname = userUpdateRequestDto.getNickname();
        String currentNickname = userProfile.getNickname();

        if(newNickname != null) {
            if(newNickname.equals(currentNickname)){
                throw new UserException(UserErrorCode.SAME_NICKNAME);
            }

            if (userProfileRepository.existsByNickname(newNickname)) {
                throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
            }
            userProfile.setNickname(newNickname);
        }

        // 프로필 이미지(변경 O)
        if(userUpdateRequestDto.getProfileUrl() != null){
            userProfile.setProfileUrl(userUpdateRequestDto.getProfileUrl());
        }

        // 비밀번호(변경 O)
        if(userUpdateRequestDto.getPassword() != null){
            if (!userUpdateRequestDto.getPassword().equals(userUpdateRequestDto.getConfirmPassword())){
                throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
            }

            String password = passwordEncoder.encode(userUpdateRequestDto.getPassword());
            user.setPassword(password);
        }

        // 생년월일(추후 정하기)
        // 이름(추후 정하기)
        // 전화번호(추후 정하기)

        return new UserUpdateResponseDto(user.getId(),user.getEmail(), userProfile.getNickname(), userProfile.getProfileUrl());
    }
}
