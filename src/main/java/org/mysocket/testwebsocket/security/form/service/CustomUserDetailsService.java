package org.mysocket.testwebsocket.security.form.service;

import lombok.extern.slf4j.Slf4j;
import org.mysocket.testwebsocket.domain.user.entity.User;
import org.mysocket.testwebsocket.domain.user.repository.UserRepository;
import org.mysocket.testwebsocket.security.form.dto.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // === 생성자 ===
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 로그인 회원 정보 찾기
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow( () -> new UsernameNotFoundException("해당하는 회원이 없습니다. 로그인 Email: "+username) );

        return new CustomUserDetails(user);
    }
}
