package org.mysocket.testwebsocket.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.mysocket.testwebsocket.domain.chat.dto.ChatRoomForm;
import org.mysocket.testwebsocket.domain.chat.entity.ChatRoom;
import org.mysocket.testwebsocket.domain.chat.entity.Message;
import org.mysocket.testwebsocket.domain.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ChatService {

    // 레포지토리
    private final ChatRoomRepository chatRoomRepository;

    // 서비스
    private final ChatSelectService chatSelectService;

    // 생성자
    public ChatService(ChatRoomRepository chatRoomRepository, ChatSelectService chatSelectService) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatSelectService = chatSelectService;
    }

    private final List<Message> messageBuffer = Collections.synchronizedList(new ArrayList<>());

// ======= 처리 ======= //

    /**
     *  채팅방을 생성하는 기능
     *  로그인 여부
     *      시큐리티로 로그인 여부를 확인하고 채팅방을 만든다
     *
     *  그룹 채팅 여부
     *      그룹채팅인지 여부를 확인하고,
     *      그룹채팅인 경우 채팅방의 이름을 표시, 1:1 채팅인 경우 상대방의 이름을 표시
     */
    // == chatroom 생성 ==
    public void createChatRoom(ChatRoomForm form) {

        // *** 로그인 유무
        
        
        // *** 채팅방 생성

        // 그룹 확인
        ChatRoom room = ChatRoom.builder()
                .name(form.getName()) //
                .isGroup(form.getIsGroup())
                .build();
        // 저장
        chatRoomRepository.save(room);

    }






    // == 메시지 저장 (메모리) ==

    // == 메시지 저장 (DB) ==


}
