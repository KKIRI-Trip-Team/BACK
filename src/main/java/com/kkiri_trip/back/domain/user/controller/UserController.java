package com.kkiri_trip.back.domain.user.controller;

import com.kkiri_trip.back.domain.user.dto.Request.LoginRequestDto;
import com.kkiri_trip.back.domain.user.dto.Request.SignUpRequestDto;
import com.kkiri_trip.back.domain.user.dto.Response.LoginResponseDto;
import com.kkiri_trip.back.domain.user.dto.Response.SignUpResponseDto;
import com.kkiri_trip.back.domain.user.service.UserService;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<SignUpResponseDto>> register(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        SignUpResponseDto signUpResponseDto = userService.register(signUpRequestDto);
        return ApiResponseDto.from(HttpStatus.CREATED, "회원가입이 성공적으로 등록되었습니다.", signUpResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto, response);
        return ApiResponseDto.from(HttpStatus.OK, "로그인이 성공적으로 완료되었습니다.", loginResponseDto);
    }
}
