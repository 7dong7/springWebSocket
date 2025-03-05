package org.mysocket.testwebsocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  이 설정을 통해서 컨트롤러 단에서 보내준 데이터도 받을 수 있게 한다
 */
//@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**") // 모든 경로 매핑
                .allowedMethods("GET", "POST")
                .allowedOriginPatterns("*")
                .allowCredentials(true);
//                .exposedHeaders("Set-Cookie")   // 노출 헤더값은 쿠키
//                .exposedHeaders("Authorization");
//                .allowedOrigins("http://localhost:8080") // 웹 앱이 동작할 프론트 포트
//                .allowedOrigins("http://localhost:3000");
//                .allowedOrigins("http://localhost:3000") // 프론트 포트
    }
}
