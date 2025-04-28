package com.kkiri_trip.back.global.common;

import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import com.kkiri_trip.back.global.common.dto.UserDummyDto;
import com.kkiri_trip.back.global.enums.Gender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class DummyController {
    @GetMapping("/userDummy")
    public ResponseEntity<ApiResponseDto<UserDummyDto>> getUserProfile() {
        UserDummyDto userDummyDto = new UserDummyDto(1L, "test@example.com", "김스트", "김테스트", "010-1234-5678", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg" , Gender.M.getKorean());

        return ApiResponseDto.from(HttpStatus.OK, "회원의 개인정보입니다.", userDummyDto);
    }
}