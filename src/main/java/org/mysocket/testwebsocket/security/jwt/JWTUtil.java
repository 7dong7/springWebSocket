package org.mysocket.testwebsocket.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    static String ACCESS_TOKEN = "access";
    static String REFRESH_TOKEN = "refresh";

    // 비밀키
    private SecretKey secretKey;

// ===== 비밀키 생성 ====== //
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());

    }

// ===== 토큰 생성 ===== //
    public String createJwt(String category, String username, String role, Long expiredMs) {
        // category -> "access", "refresh" 토큰 종류 지정

        return Jwts.builder() // jwt 생성 준비
                .claim("category", category) // 속성 설정
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // 발급일
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료일
                .signWith(secretKey) // 사인
                .compact(); // 생성
        // 1초 = 1,000 밀리초
        // 1분 = 60 * 1,000 밀리초 = 60,000 밀리초
        // 1시간 = 60 * 60 * 1,000 밀리초 = 3,600,000 밀리초
    }

    // ===== 토큰 검증 ===== //
    // 토큰에서 이름 속성 추출
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    // 토큰에서 권한 속성 추출
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // 토큰에서 카테고리 속성 추출
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // 토큰 만료일 검증 (만료: true, 유효: false) -- 토큰이 만료되었습니까? 네 true 만료됨
    public Boolean isTokenExpired(String token) {
        // 토큰에서 설정된 만료일을 추출하고 추출된 만료일이 오늘 보다 이후(미래)인 경우 토큰이 만료되었다는 뜻 -> 유효하지 않은 토큰: true 리턴
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
}
