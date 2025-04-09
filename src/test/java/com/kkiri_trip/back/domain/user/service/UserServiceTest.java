package com.kkiri_trip.back.domain.user.service;

import com.kkiri_trip.back.domain.user.dto.Request.LoginRequestDto;
import com.kkiri_trip.back.domain.user.dto.Request.SignUpRequestDto;
import com.kkiri_trip.back.domain.user.dto.Response.LoginResponseDto;
import com.kkiri_trip.back.domain.user.dto.Response.SignUpResponseDto;
import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import com.kkiri_trip.back.global.enums.Gender;
import com.kkiri_trip.back.global.error.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("회원가입 성공 테스트")
    void register_success(){
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email("test@example.com")
                .password("12345")
                .confirmPassword("12345")
                .name("김테스트")
                .nickname("김스트")
                .mobile_number("01012345678")
                .gender(Gender.M)
                .build();

        // when
        SignUpResponseDto response = userService.register(signUpRequestDto);

        // then
        assertNotNull(response);
        assertEquals("김스트", response.getNickname());
        assertTrue(userRepository.existsByEmail("test@example.com"));
    }

    @Test
    @DisplayName("중복 이메일 예외 발생")
    void duplicate_email_throws_exception(){
        // given
        userRepository.save(User.builder()
                .email("test@example.com")
                .password("12345")
                .name("김테스트")
                .nickname("김스트")
                .mobile_number("01012345678")
                .gender(Gender.M)
                .build());

        SignUpRequestDto dto = SignUpRequestDto.builder()
                .email("test@example.com")
                .password("2345")
                .name("박테스트")
                .nickname("박스트")
                .mobile_number("01011112222")
                .gender(Gender.F)
                .build();

        // when
        // then
        assertThrows(UserException.class, () -> userService.register(dto));
    }

    @Test
    @DisplayName("비밀번호 불일치 발생")
    void password_mismatch_throws_exception(){
        // given
        SignUpRequestDto dto = SignUpRequestDto.builder()
                .email("test@example.com")
                .password("12345")
                .confirmPassword("2345")
                .name("김테스트")
                .nickname("김스트")
                .mobile_number("01012345678")
                .gender(Gender.M)
                .build();

        // when
        // then
        assertThrows(UserException.class, () -> userService.register(dto));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success(){
        // given
        userRepository.save(User.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("12345"))
                .name("김테스트")
                .nickname("김스트")
                .mobile_number("01012345678")
                .gender(Gender.M)
                .build());

        LoginRequestDto loginRequestDto = new LoginRequestDto("test@example.com", "12345");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        LoginResponseDto result = userService.login(loginRequestDto, response);

        // then
        assertThat(result.getAccessToken()).isNotNull();
        assertThat(result.getNickname()).isEqualTo("김스트");
    }

    @Test
    @DisplayName("로그인 시 404 예외")
    void login_fail_exception(){
        // given
        userRepository.save(User.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("12345"))
                .name("김테스트")
                .nickname("김스트")
                .mobile_number("01012345678")
                .gender(Gender.M)
                .build());

        LoginRequestDto loginResponseDto = new LoginRequestDto("nope@example.com", "12345");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when & then
        assertThrows(UserException.class, () -> userService.login(loginResponseDto, response));
    }
}