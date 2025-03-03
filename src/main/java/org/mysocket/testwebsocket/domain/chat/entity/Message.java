package org.mysocket.testwebsocket.domain.chat.entity;

import jakarta.persistence.*;
import org.mysocket.testwebsocket.domain.base.BaseTime;
import org.mysocket.testwebsocket.domain.user.entity.User;

@Entity
public class Message extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender; // 작성자

    private String content;
}
