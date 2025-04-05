package com.kkiri_trip.back.domain.user.service;

import com.kkiri_trip.back.domain.user.dto.SignUpRequestDto;
import com.kkiri_trip.back.domain.user.dto.SignUpResponseDto;
import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import com.kkiri_trip.back.global.enums.Gender;
import com.kkiri_trip.back.global.error.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


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
}