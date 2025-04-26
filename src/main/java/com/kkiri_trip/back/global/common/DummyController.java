package com.kkiri_trip.back.global.common;

import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import com.kkiri_trip.back.global.enums.Gender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DummyController {
    @GetMapping("/userDummy")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getUserProfile() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", "test1@example.com");
        dataMap.put("name", "김테스트");
        dataMap.put("nickname", "김스트");
        dataMap.put("mobile_number", "010-1234-5678");
        dataMap.put("profileUrl", "https://i.ibb.co/DfzGjPSr/butterfly-fairies-3840x2160-17273.jpg");
        dataMap.put("gender", Gender.M.getKorean());

        return ApiResponseDto.from(HttpStatus.OK, "회원의 개인정보입니다.", dataMap);
    }
}