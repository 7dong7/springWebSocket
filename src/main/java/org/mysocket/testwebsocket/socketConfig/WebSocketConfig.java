package org.mysocket.testwebsocket.socketConfig;


import org.mysocket.testwebsocket.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     *  @EnableWebSocket
     *      - 스프링 부트에서 웹소켓 지원을 활성화한다
     *
     *  implements WebSocketConfigurer
     *      - registerWebSocketHandlers 메소드를 오버라이드해 웹소켓 핸들러를 등록한다
     *  
     *  registry.addHandler
     *      - "/chat" 경로로 들어오는 웹소켓 요청을 WebSocketHandler 로 처리하도록 매핑한다
     *      - setAllowedOrigins("*")
     *          모든 도메인에서 오는 요청을 허용한다 (테스트용으로만 한다)
     *
     *  WebSocketHandler 와 연결한다. 이제 클라이언트가 ws://localhost:8080//chat 으로 요청을 보내면 이 설정에 따라 해당 핸들러가 호출된다
     */
    @Override // 요청 경로에 대한 응답 핸들러를 지정한다
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), "/chat").setAllowedOrigins("*");
    }
}
