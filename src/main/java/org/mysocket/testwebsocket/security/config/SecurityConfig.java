package org.mysocket.testwebsocket.security.config;

import org.mysocket.testwebsocket.security.jwt.JWTUtil;
import org.mysocket.testwebsocket.security.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    // 생성자
    public SecurityConfig(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 인증 매니저
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 폼로그인 + JWT 필터 체인
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {

        // csrf 비활성화 & basic 로그인 비활성화
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 폼 로그인 방식 사용
        http
                .formLogin(form -> form
                        .loginPage("/login")
                );

        // 인가 설정
        http
                .authorizeHttpRequests( auth -> auth
                        .anyRequest().permitAll()
                );

    // ==== form login JWT 방식 ==== //
        // 세션 session 전략 Stateless
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // 폼 로그인 방식으로 로그인을 진행하고 JWT 발급을 위해서는 필터의 successHandler 가 필요하기 때문에
        // 필터를 커스텀해서 만들어야 한다 // UsernamePasswordAuthenticationFilter 를 LoginFilter 로 대체
        http
                .addFilterAt(
                        new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class
                );
//        // 토큰 검증 필터 추가 (LoginFilter 전에 추가)
//        http
//                .addFilterBefore(
//                        new JWTAuthFilter(jwtUtil), LoginFilter.class
//                );




        // oauth2 로그인 방식 사용
//        http
//                .oauth2Login(oauth2 -> oauth2
//                                .loginPage("/login") // 로그인 페이지
//                                .userInfoEndpoint( userInfoEndpointConfig -> userInfoEndpointConfig
//                                        .userService(customOAuth2UserService))
//                        // 외부 인증 제공자 엔드포인트 지정
//                        // 회원의 프로필 정보를 조회할 때 사용
//                );

        // 로그아웃
        http
                .logout(logout -> logout
                        .logoutUrl("/logout")           // 로그아웃 경로
                        .logoutSuccessUrl("/login?logout")     // 로그아웃 이후 경로
                        .invalidateHttpSession(true)    // 세션 무효화
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );


        return http.build();
    }
}
