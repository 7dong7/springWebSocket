package org.mysocket.testwebsocket.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mysocket.testwebsocket.domain.base.BaseTime;
import org.mysocket.testwebsocket.domain.chat.entity.ChatRoomUser;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;        // 식별자
    
    private String email;   // 이메일, 로그인사용
    private String oauthId; // oauth2 의 경우 로그인사용

    private String nickname; // 닉네임
    private String password; // 비밀번호
    private String role;

    @OneToMany(mappedBy = "user")
    private List<ChatRoomUser> chatRooms;

    @Builder
    public User(String email, String oauthId, String nickname, String password, String role) {
        this.email = email;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }
}
