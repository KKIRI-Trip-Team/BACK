package com.kkiri_trip.back.domain.user.service;

import com.kkiri_trip.back.domain.user.dto.Request.LoginRequestDto;
import com.kkiri_trip.back.domain.user.dto.Request.SignUpRequestDto;
import com.kkiri_trip.back.domain.user.dto.Request.UserProfileCreateRequestDto;
import com.kkiri_trip.back.domain.user.dto.Request.UserUpdateRequestDto;
import com.kkiri_trip.back.domain.user.dto.Response.LoginResponseDto;
import com.kkiri_trip.back.domain.user.dto.Response.SignUpResponseDto;
import com.kkiri_trip.back.domain.user.dto.Response.UserResponseDto;
import com.kkiri_trip.back.domain.user.dto.Response.UserUpdateResponseDto;
import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.domain.user.entity.UserProfile;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import com.kkiri_trip.back.global.jwt.JwtUtil;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.UserException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        UserProfile userProfile = user.getUserProfile();

        // 닉네임 중복 검사
        if (userRepository.existsByNickname(userProfileCreateRequestDto.getNickname())){
            throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
        }

        if(userProfileCreateRequestDto.getNickname() == null || userProfileCreateRequestDto.getNickname().isEmpty()){
            throw new UserException(UserErrorCode.EMPTY_NICKNAME);
        }

        if(profileUrl == null || profileUrl.isEmpty()){
            profileUrl = DEFAULT_PROFILE_URL;
        }

        userProfile.createProfile(userProfileCreateRequestDto.getNickname(), profileUrl);
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

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 60);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 14);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return new LoginResponseDto(accessToken, userProfile.getNickname());
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
                .map(UserResponseDto::from)
                .toList();
    }

    public UserResponseDto getMyInfo(User user) {
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserUpdateResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto, User loginUser){
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        UserProfile userProfile = user.getUserProfile();

        if(!user.getId().equals(loginUser.getId())){
            throw new UserException(UserErrorCode.UNAUTHORIZED_UPDATE);
        }

        if(userRepository.existsByEmail(userUpdateRequestDto.getEmail())
                && !user.getEmail().equals(userUpdateRequestDto.getEmail())){
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        if(userRepository.existsByNickname(userUpdateRequestDto.getNickname())
                && !userProfile.getNickname().equals(userUpdateRequestDto.getNickname())){
            throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
        }

        user.setEmail(userUpdateRequestDto.getEmail());
        userProfile.setNickname(userUpdateRequestDto.getNickname());
        userProfile.setProfileUrl(userUpdateRequestDto.getProfileUrl());

        return new UserUpdateResponseDto(user.getId(),user.getEmail(), userProfile.getNickname(), userProfile.getProfileUrl());
    }
}
