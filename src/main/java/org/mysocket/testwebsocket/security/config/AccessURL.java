package org.mysocket.testwebsocket.security.config;

public class AccessURL {

    // JwtAuthFilter 에 shouldNotFilter 설정되어있음
    // securityFilterChain 안에 permitAll 에 설정되어있음
    public static final String[] WHITELIST = { // 권한이 없어도 접근 가능한
            "/",        // 메인 페이지
            "/login" // 로그인
    };
}
