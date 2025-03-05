package org.mysocket.testwebsocket.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.mysocket.testwebsocket.domain.user.entity.User;
import org.mysocket.testwebsocket.security.config.AccessURL;
import org.mysocket.testwebsocket.security.form.dto.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    // 생성자
    public JwtAuthFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // ==== 인증 필터를 무시할 경로 설정 ==== //
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return List.of(AccessURL.WHITELIST).contains(request.getRequestURI());
    }

    // 로그인 인증 필터 UsernamePasswordAuthenticationFilter 를 대체하는 filter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // === 모든 헤더 출력 === //
        // 모든 헤더 출력 (디버깅)
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            log.info("Header - {}: {}", headerName, request.getHeader(headerName));
//        }
        // 로그인 접근
//        String requestURI = request.getRequestURI();
//        log.info("JwtAuthFilter doFilterInternal requestURI: {}", requestURI);
//        if(requestURI.equals("/login")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        response.setContentType("text/html; charset=UTF-8");
    // === 토큰 검증 === 토큰 존재유무(null), 만료여부, 서명(secretkey), 토큰형식
        // 요청 Header 에 토큰 존재 여부
        String Authorization = request.getHeader("Authorization");
        log.info("JwtAuthFilter doFilterInternal Authorization: {}", Authorization);
        if (Authorization == null || !Authorization.startsWith("Bearer ")) { // 토큰이 null 인 경우 || Bearer 로 시작하지 않는 경우 => 잘못된 경우
            PrintWriter writer = response.getWriter();
            writer.print("다시 로그인 요청: token null");

            // response status code 상태코드
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 획득
        String accessToken = Authorization.split(" ")[1]; // "bearer " 제거
        log.info("AuthFilter doFilterInternal 요청헤더에서 넘어온 값 accessToken: {}", accessToken); // 토큰 값 확인

        // 토큰이 만료가 되지 않은 경우 그 토큰이 Access 토큰인지 Refresh 토큰인지 확인
        // 토큰이 access 인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals(JWTUtil.ACCESS_TOKEN)) {
            log.info("ACCESS_TOKEN 토큰 아님 category: {}", category);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("not access token");
            return;
        }


        // 토큰 검증 유효하지 않은경우 exception 발생
        try {
            Claims claims = jwtUtil.validClaims(accessToken); // 얘기 exception 을 발생함
            log.info("claims 검증 통과");
            // ====== 토큰 검증 통과 ===== 세션생성 (일시적)
            // 토큰에서 username, role 값을 획득
            String email = jwtUtil.getEmail(accessToken);
            String role = jwtUtil.getRole(accessToken);

            User user = User.builder()
                    .email(email)
                    .role(role)
                    .nickname(jwtUtil.getNickname(accessToken))
                    .build();
            /**
             *  access 토큰을 통해 인증이 완료되면 context 에 authentication 객체를 저장해 일시적으로 세션을 활성화(연결)시킨다
             *
             *  현재는 form 로그인 방식만 구현됨
             *  oauth2 방식으로 구현시 인증객체로 OAuth2User 를 커스텀해서 만든것도 사용해야 됨
             */
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            boolean authenticated = authToken.isAuthenticated();
            log.info("인증된 객체 생성 isAuthenticated(): {}", authenticated);

            // 여기에 등록시키면 일시적으로 세션이 만들어진다. 로그인된 상태로 변경
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // 다음 필터 호출
            filterChain.doFilter(request, response);
        /**
         *  response status code
         *  응답에 관련된 상태코드를 프론트 쪽으로 응답해준다
         *  프론트 쪽에서는 응답받은 상태 코드에 따라서 어떤 기능을 수행할지 결정한다 (미리 상태코드에 대한 약속을 해 두어야 한다)
         *  access 토큰이 만료되었으니까 refresh 토큰을 사용해서 access 토큰을 재발급 받는 경로, 기능을 수행하게 한다
         *  토큰 만료 -> refresh 토큰 요청 -> access 토큰 재발급 -> 다시 요청
         *  상태 코드에 따라서 결정
         */
        } catch (IllegalArgumentException e) { // 토큰 존재 유무 - null => IllegalArgumentException
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("token is null or empty");
        } catch (MalformedJwtException e) { // 토큰 형식 ( header.payloaad.signature 아님 ) - MalformedJwtException
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("malformed token");
            return;
        } catch (SignatureException e) { // 서명 유효(secretKey 일치 여부) - SignatureException
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("invalid token signature");
            return;
        } catch (ExpiredJwtException e) { // 토큰 만료 여부: expired => ExpiredJwtException
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("access token expired"); // access 토큰이 만료되었다는 응답
            return;
        }


    }
}
