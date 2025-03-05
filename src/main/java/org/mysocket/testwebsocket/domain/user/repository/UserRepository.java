package org.mysocket.testwebsocket.domain.user.repository;

import org.mysocket.testwebsocket.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // form 로그인 박식으로 로그인 할 때 사용한다
    Optional<User> findByEmail(String username);
}
