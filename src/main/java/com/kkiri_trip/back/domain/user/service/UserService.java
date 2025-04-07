package com.kkiri_trip.back.domain.user.service;

import com.kkiri_trip.back.domain.user.dto.Request.LoginRequestDto;
import com.kkiri_trip.back.domain.user.dto.Request.SignUpRequestDto;
import com.kkiri_trip.back.domain.user.dto.Response.LoginResponseDto;
import com.kkiri_trip.back.domain.user.dto.Response.SignUpResponseDto;
import com.kkiri_trip.back.domain.user.entity.User;
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

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;

    @Transactional
    public SignUpResponseDto register(SignUpRequestDto signupRequestDto){
        if(userRepository.existsByEmail(signupRequestDto.getEmail())){
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        if(!signupRequestDto.getPassword().equals(signupRequestDto.getConfirmPassword())){
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }

        User user = User.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .name(signupRequestDto.getName())
                .nickname(signupRequestDto.getNickname())
                .mobile_number(signupRequestDto.getMobile_number())
                .gender(signupRequestDto.getGender())
                .build();

        userRepository.save(user);

        return new SignUpResponseDto(user.getId(), user.getNickname());
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

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

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 14);
        response.addCookie(cookie);

        return new LoginResponseDto(accessToken, user.getNickname());
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

    // TODO : 프로필 업로드 논의
    // TODO : 닉네임 중복 검사 논의
    // TODO : 이메일 검증 추가 논의

}
