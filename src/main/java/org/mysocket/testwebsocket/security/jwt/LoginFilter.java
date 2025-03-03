package org.mysocket.testwebsocket.security.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

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
        log.info("로그인 회원: {}, {}",email, password);

        // 미인증된 인증 객체 생성  ( isAuthentication() = false )                        (username(로그인아이디), 비번, 권한)
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password, null);

        /**
         *  인증을 처리하는 인증 매니저한테 (미인증)인증 객체를 넘겨주면
         *  인증매니저가 적절한 provider 를 찾아 인증객체를 넘겨주고 로그인을 처리한다
         */
        // 인증 매니저한테 미인증 객체를 넘겨줌
        return null; //
//        return authenticationManager.authenticate(token);
    }


    /**
     *  로그인 성공시 응답
     *  로그인 회원의 민감하지 않은 정보를 가지고 토큰을 생성하고 응답한다
     *  헤더에 access 토큰을 담아서 응답 -- 인가처리
     *  쿠키에 refresh 토큰을 담아서 응답 -- access 토큰을 재발급 받기만을 위한 토큰
     */
    // 로그인을 성공하는 경우 처리되는 로직
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    // 로그인을 실패하는 경우 처리되는 로직
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
