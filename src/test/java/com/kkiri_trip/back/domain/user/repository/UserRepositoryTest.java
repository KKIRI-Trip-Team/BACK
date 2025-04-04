package com.kkiri_trip.back.domain.user.repository;

import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.global.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("이메일 존재 여부 확인")
    void existsByEmailTest(){
        User user = User.builder()
                .email("test@example.com")
                .password("12345")
                .name("김테스트")
                .nickname("김스트")
                .mobile_number("01012345678")
                .gender(Gender.M)
                .build();

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail(user.getEmail());

        assertTrue(exists);
    }

    @Test
    @DisplayName("이메일이 없으면 false 반환")
    void notExistsByEmailTest(){
        boolean exists = userRepository.existsByEmail("asd@example.com");

        assertFalse(exists);
    }
}