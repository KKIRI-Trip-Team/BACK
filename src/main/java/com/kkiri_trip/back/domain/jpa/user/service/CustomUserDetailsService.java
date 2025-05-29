package com.kkiri_trip.back.domain.jpa.user.service;

import com.kkiri_trip.back.domain.jpa.user.entity.User;
import com.kkiri_trip.back.domain.jpa.user.repository.UserRepository;
import com.kkiri_trip.back.domain.jpa.user.util.CustomUserDetails;
import com.kkiri_trip.back.global.error.errorcode.UserErrorCode;
import com.kkiri_trip.back.global.error.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }
}
