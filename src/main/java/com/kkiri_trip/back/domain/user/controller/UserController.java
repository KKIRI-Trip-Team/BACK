package com.kkiri_trip.back.domain.user.controller;

import com.kkiri_trip.back.api.dto.Feed.FeedDto;
import com.kkiri_trip.back.domain.dashboard.dto.DashboardDto;
import com.kkiri_trip.back.domain.dashboard.service.DashboardService;
import com.kkiri_trip.back.domain.feed.service.FeedService;
import com.kkiri_trip.back.domain.user.dto.Request.LoginRequestDto;
import com.kkiri_trip.back.domain.user.dto.Request.SignUpRequestDto;
import com.kkiri_trip.back.domain.user.dto.Request.UserProfileCreateRequestDto;
import com.kkiri_trip.back.domain.user.dto.Request.UserUpdateRequestDto;
import com.kkiri_trip.back.domain.user.dto.Response.LoginResponseDto;
import com.kkiri_trip.back.domain.user.dto.Response.SignUpResponseDto;
import com.kkiri_trip.back.domain.user.dto.Response.UserResponseDto;
import com.kkiri_trip.back.domain.user.dto.Response.UserUpdateResponseDto;
import com.kkiri_trip.back.domain.user.service.UserService;
import com.kkiri_trip.back.domain.user.util.CustomUserDetails;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import com.kkiri_trip.back.global.common.dto.PageResponseDto;
import com.kkiri_trip.back.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final FeedService feedService;
    private final DashboardService dashboardService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<SignUpResponseDto>> register(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        SignUpResponseDto signUpResponseDto = userService.register(signUpRequestDto);
        return ApiResponseDto.from(HttpStatus.CREATED, "회원가입이 성공적으로 등록되었습니다.", signUpResponseDto);
    }

    @PostMapping("/register/profile")
    public ResponseEntity<ApiResponseDto<UserProfileCreateRequestDto>> profileCreate(@RequestBody @Valid UserProfileCreateRequestDto userProfileCreateRequestDto){
        userService.registerProfile(userProfileCreateRequestDto);
        return ApiResponseDto.from(HttpStatus.OK, "프로필이 성공적으로 등록되었습니다.", userProfileCreateRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto, response);
        return ApiResponseDto.from(HttpStatus.OK, "로그인이 성공적으로 완료되었습니다.", loginResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        userService.logout(token);
        return ApiResponseDto.from(HttpStatus.OK, "로그아웃이 성공적으로 되었습니다.", null);
    }

    @GetMapping()
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers(){
        List<UserResponseDto> userResponseDtoList = userService.getAllUsers();
        return ApiResponseDto.from(HttpStatus.OK, "모든 유저를 조회했습니다.", userResponseDtoList);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        UserResponseDto userResponseDto = userService.getMyInfo(userDetails.getUser(), userDetails.getUser().getUserProfile());
        return ApiResponseDto.from(HttpStatus.OK, "본인 정보를 조회했습니다.", userResponseDto);
    }

    // TODO : 게시글에 대한 정홗한 데이터 나오면 DTO 생성 후 응답 값 수정
    @GetMapping("/me/feeds")
    public ResponseEntity<ApiResponseDto<PageResponseDto<FeedDto>>> getMyFeeds(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                               @RequestParam(defaultValue = "1") int page,
                                                                               @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        PageResponseDto<FeedDto> feeds = feedService.getMyFeeds(userDetails.getUser().getId(), pageable);
        return ApiResponseDto.from(HttpStatus.OK, "본인의 작성 글 목록을 조회했습니다.", feeds);
    }

    @GetMapping("/me/dashboard")
    public ResponseEntity<ApiResponseDto<DashboardDto>> getMyDashboard(@AuthenticationPrincipal CustomUserDetails userDetails){
        DashboardDto dashboardDto = dashboardService.getMyDashboard(userDetails.getUser().getId());
        return ApiResponseDto.from(HttpStatus.OK, "본인의 대시보드를 조회했습니다.", dashboardDto);
    }

    @PutMapping("/information")
    public ResponseEntity<ApiResponseDto<UserUpdateResponseDto>> updateUser(
                                                        @RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails
                                                        ){
        UserUpdateResponseDto userUpdateResponseDto = userService.updateUser(userUpdateRequestDto,userDetails.getUser());
        return ApiResponseDto.from(HttpStatus.OK, "회원 정보 수정 완료되었습니다.", userUpdateResponseDto);
    }
}
