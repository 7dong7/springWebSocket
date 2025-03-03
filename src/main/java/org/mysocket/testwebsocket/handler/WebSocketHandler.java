package org.mysocket.testwebsocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    /**
     *  TextWebSocketHandler
     *      텍스트 기반 웹소켓 메시지를 처리하기 위한 기본 클래스
     *
     *  List<WebSocketSession> sessions
     *      현재 연결된 모든 클라이언트 세션을 저장한다
     */
    private final List<WebSocketSession> sessions = new ArrayList<>();

    /**
     *  afterConnectionEstablished
     *      클라이언트가 연결되면 호출된다
     *      새로운 WebSocketSession을 sessions 에 추가한다
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("새로운 연결 Connected: {}", session.getId());
    }
    /**
     *  handleTextMessage
     *      클라이언트가 메시지를 보내면 호출된다
     *      받은 메시지(payload)를 모든 연결된 세션에 브로드캐스트합니다
     *      isOpen() 으로 세션이 유효한지 확인후 전송
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage(payload));
            }
        }
    }

    /**
     *  afterConnectionClosed
     *      클라이언트 연결이 종료되면 호출된다
     *      해당 세션을 sessions 에서 제거한다
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        sessions.remove(session);
        log.info("연결종료: {}", session.getId());
    }
}
