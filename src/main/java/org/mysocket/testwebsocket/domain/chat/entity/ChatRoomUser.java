package org.mysocket.testwebsocket.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.mysocket.testwebsocket.domain.base.BaseTime;
import org.mysocket.testwebsocket.domain.user.entity.User;

/**
 *  사용자와 채팅방의 조인 테이블
 */
@Entity
@Getter
@Table(name = "chatroom_user")
public class ChatRoomUser extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom; // 채팅방

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 사용자
}
