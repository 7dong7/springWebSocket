package org.mysocket.testwebsocket.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  === 로그인 필터 ===
 *  form 로그인의 경우 UsernamePasswordAuthenticationFilter 가 응답을 하는데 (session 방식)
 *  현재 로그인은 form 방식으로 로그인을 하지만 JWT 방식으로 인증,인가 처리를 하기 위해서
 *  로그인 성공 이후에 JWT 발급을 처리하는 로직이 필요하기 때문에 
 *  LoginFilter 를 JWT 를 발급하기 위해 커스텀이 필요하다
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    // 생성자
    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // 로그인 처리 요청 로직
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 로그인을 시도하는 username(email), password 를 받는다
        String email = obtainUsername(request);
        String password = obtainPassword(request);
        log.info("LoginFilter attemptAuthentication 로그인 회원 username: {},password: {}",email, password);

        // 미인증된 인증 객체 생성  ( isAuthentication() = false )                        (username(로그인아이디), 비번, 권한)
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password, null);

        /**
         *  인증을 처리하는 인증 매니저한테 (미인증)인증 객체를 넘겨주면
         *  인증매니저가 적절한 provider 를 찾아 인증객체를 넘겨주고 로그인을 처리한다
         */
        // 인증 매니저한테 미인증 객체를 넘겨줌
        return authenticationManager.authenticate(token);
    }


    /**
     *  로그인 성공시 응답
     *  로그인 회원의 민감하지 않은 정보를 가지고 토큰을 생성하고 응답한다
     *  헤더에 access 토큰을 담아서 응답 -- 인가처리
     *  쿠키에 refresh 토큰을 담아서 응답 -- access 토큰을 재발급 받기만을 위한 토큰
     */
// 로그인을 성공하는 경우 처리되는 로직
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // === access token, refresh token === //
        log.info("LoginFilter successfulAuthentication 로그인 성공");

        // 회원 정보
        String email = authentication.getName();// 로그인 할때 사용한 username; 로그인 아이디
        
        // 권한
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        
        // 토큰 생성
        String access = jwtUtil.createJwt(JWTUtil.ACCESS_TOKEN, email, role, 15 * 60 * 1000L); // 15분 유지
        String refresh = jwtUtil.createJwt(JWTUtil.REFRESH_TOKEN, email, role, 25 * 60 * 60 * 1000L); // 24시간 유지
        /**
         *  1초 = 1,000 밀리초
         *  1분 = 60 * 1,000 밀리초 = 60,000 밀리초
         *  1시간 = 60 * 60 * 1,000 밀리초 = 3,600,000 밀리초
         */
//        log.info("access: {}", access); // 생생된 access 토큰 확인
//        log.info("refresh: {}", refresh); // 생생된 refresh 토큰 확인


        // === 응답 생성 === //
        response.setHeader("Authorization", "Bearer "+ access);
        response.addCookie(createCookie(JWTUtil.REFRESH_TOKEN, refresh));
        response.setStatus(HttpStatus.OK.value());

        // 응답 설정 JSON
        PrintWriter out = response.getWriter();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", access);
        responseBody.put("message", "successful");
        response.setContentType("application/json");
        out.write(new ObjectMapper().writeValueAsString(responseBody));
        out.flush();
    }

    // == 쿠키 생성 ==
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value); // 쿠키 만들기
        cookie.setMaxAge(24*60*60); // 쿠키 유효 기간 설정 (초 단위)
        //cookie.setSecure(true); // HTTPS 의 보안 상태에서만 쿠키 유효 설정
        cookie.setPath("/");  // 애플리케이션내의 모든 경로에서 쿠키가 유효하게 설정
        cookie.setHttpOnly(true); // HttpOnly 쿠키가 클라이언트 측 스크립트에서 접근할 수 없게 된다 (XSS) 공격 보호 설정

        return cookie;
    }

// 로그인을 실패하는 경우 처리되는 로직
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // === 실패 응답 생성 === //
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", "invalid credentials");

        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(responseBody));
        out.flush();
    }
}
