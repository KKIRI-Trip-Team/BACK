package com.kkiri_trip.back.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.access-expiration}")
    private long accessExpiration;

    @Value("${spring.jwt.refresh-expiration}")
    private long refreshExpiration;

    public String generateAccessToken(Long userId){
       return createToken(userId, accessExpiration);
    }

    public String generateRefreshToken(Long userId){
      return createToken(userId, refreshExpiration);
    }

    private String createToken(Long userId, long expiration){
        Date now = new Date();

        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId.toString()) // 사용자 정보 저장
                .setIssuedAt(now) // 발급 시간
                .setExpiration(expiry) // 만료 시간
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256) // 서명
                .compact(); // 토큰 문자열 생성
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes()) // 비밀키 설정
                    .build()
                    .parseClaimsJws(token); // 토큰 검증
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getUserIdFromAccessToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()) // 비밀키 설정
                .build()
                .parseClaimsJws(token) // 토큰 파싱 및 검증
                .getBody(); // 토큰의 body 추출
        return claims.getSubject();  // subject는 사용자 이메일
    }

    public long getRemainingTime(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()) // 비밀키 설정
                .build()
                .parseClaimsJws(token) // 토큰 파싱 및 검증
                .getBody(); // 토큰의 body 추출
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후 부분만 반환
        }
        return null;
    }

}
