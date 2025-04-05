package com.kkiri_trip.back.domain.user.service;

import com.kkiri_trip.back.domain.user.dto.SignUpRequestDto;
import com.kkiri_trip.back.domain.user.dto.SignUpResponseDto;
import com.kkiri_trip.back.domain.user.entity.User;
import com.kkiri_trip.back.domain.user.repository.UserRepository;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    // TODO : 프로필 업로드 논의
    // TODO : 닉네임 중복 검사 논의
    // TODO : 이메일 검증 추가 논의

}
