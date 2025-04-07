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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 14);
        response.addCookie(cookie);

        return new LoginResponseDto(accessToken, user.getNickname());
    }

    // TODO : 프로필 업로드 논의
    // TODO : 닉네임 중복 검사 논의
    // TODO : 이메일 검증 추가 논의

}
