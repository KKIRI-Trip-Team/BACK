package com.kkiri_trip.back.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessTokenFromHeader(request);

        try{
            if(accessToken != null && jwtUtil.validateToken(accessToken)){
                // AccessToken이 유효하면 + 인증 처리
                setAuthentication(accessToken, request);
            }else{
                // AccessToken이 만료됐거나 없으면 + RefreshToken 확인
                String refreshToken = getRefreshTokenFromCookie(request);
                if(refreshToken != null && jwtUtil.validateToken(refreshToken)){
                    // RefreshToken도 유효하면 -> 새 AccessToken 발급
                    String userId = jwtUtil.getUserIdFromAccessToken(refreshToken);
                    String newAccessToken = jwtUtil.generateAccessToken(Long.parseLong(userId));

                    // 응답 헤더에 새 AccessToken 추가
                    response.setHeader("Authorization", "Bearer " + newAccessToken);

                    // 새 토큰으로 인증 처리
                    setAuthentication(newAccessToken, request);
                }
            }
        }catch(Exception e){
            log.warn("JWT 처리 중 오류 발생: {}", e.getMessage());
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    // 사용자 인증
    private void setAuthentication(String token, HttpServletRequest request) {
        String userId = jwtUtil.getUserIdFromAccessToken(token);

        // DB에서 사용자 정보 조회
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Spring Security 컨텍스트에 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 요청 헤더에서 Bearer Access Token 추출
    private String getAccessTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }

    // 쿠키에서 refreshToken 추출
    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if("refreshToken".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
