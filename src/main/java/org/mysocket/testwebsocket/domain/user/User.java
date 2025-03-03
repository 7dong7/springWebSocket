package org.mysocket.testwebsocket.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import org.mysocket.testwebsocket.domain.base.BaseTime;
import org.mysocket.testwebsocket.domain.chatroom.ChatRoomUser;

import java.util.List;

@Entity
@Getter
public class User extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;        // 식별자
    
    private String email;   // 이메일, 로그인사용
    private String oauthId; // oauth2 의 경우 로그인사용

    private String nickname; // 닉네임
    private String password; // 비밀번호

    @OneToMany(mappedBy = "user")
    private List<ChatRoomUser> chatRooms;


}
