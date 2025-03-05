package org.mysocket.testwebsocket.security.form.dto;

import org.mysocket.testwebsocket.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(final User user) {
        this.user = user;
    }

    // 권한
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() { // 로그안 아이디 이메일 반환
        return user.getEmail();
    }

    // 인덱스
    public Long getId() {
        return user.getId();
    }
    
    // 닉네임
    public String nickname() {
        return user.getNickname();
    }

}
