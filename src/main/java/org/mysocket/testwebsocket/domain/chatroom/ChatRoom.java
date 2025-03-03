package org.mysocket.testwebsocket.domain.chatroom;

import jakarta.persistence.*;
import org.mysocket.testwebsocket.domain.base.BaseTime;

import java.util.List;

@Entity
public class ChatRoom extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoom_id")
    private Long id; // 채팅방 식별

    private String name; // 채팅방 이름  -- 1:1 채팅의 경우 채팅방의 이름이 없을 수 있음
    private boolean isGroup; // 그룹 여부
    /**
     *  채팅방에 참가하고 있는 사용자
     */
    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomUser> participants;

}
