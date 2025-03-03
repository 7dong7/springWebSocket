package org.mysocket.testwebsocket.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 생성자
    // 폼로그인 + JWT 필터 체인
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {

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
