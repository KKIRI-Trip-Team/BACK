package com.kkiri_trip.back.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkiri_trip.back.domain.user.dto.Request.SignUpRequestDto;
import com.kkiri_trip.back.domain.user.dto.Response.SignUpResponseDto;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import com.kkiri_trip.back.domain.user.service.UserService;
import com.kkiri_trip.back.global.enums.Gender;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.UserException;
import com.kkiri_trip.back.global.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockitoBean private UserService userService;

    @MockitoBean private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();



    @Test
    @DisplayName("회원가입 요청 성공")
    void register_success() throws Exception{
        SignUpRequestDto dto = new SignUpRequestDto(
                "test@example.com", "12345", "12345",  "김테스트", "김스트", "01012345678", null, Gender.M
        );

        SignUpResponseDto responseDto = new SignUpResponseDto(1L, "김스트");

        given(userService.register(any(SignUpRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/user/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nickname").value("김스트"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("중복 이메일 회원가입 실패")
    void register_duplicate_email() throws Exception{
        SignUpRequestDto dto =  new SignUpRequestDto(
                "test@example.com", "12345", "12345", "박테스트", "박스트", "01011112222", null, Gender.F
        );

        given(userService.register(any(SignUpRequestDto.class)))
                .willThrow(new UserException(UserErrorCode.DUPLICATE_EMAIL));

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isConflict())
                        .andExpect(jsonPath("$.message").value("이미 가입된 이메일입니다."));
    }

    @Test
    @DisplayName("이메일 누락 시 400 BadRequest")
    void register_missing_email() throws Exception{
        SignUpRequestDto dto =  new SignUpRequestDto(
                "", "12345", "12345", "박테스트", "박스트", "01011112222", null, Gender.F
        );

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 누락 시 400 BadRequest")
    void register_missing_password() throws Exception{
        SignUpRequestDto dto =  new SignUpRequestDto(
                "test@example.com", "", "", "박테스트", "박스트", "01011112222", null, Gender.F
        );

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 형식이 올바르지 않으면 400 BadRequest")
    void invalid_email_format() throws Exception{
        SignUpRequestDto dto =  new SignUpRequestDto(
                "not-email", "12345", "12345", "박테스트", "박스트", "01011112222", null, Gender.F
        );

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}