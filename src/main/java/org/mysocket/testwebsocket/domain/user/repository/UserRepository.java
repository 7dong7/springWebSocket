package org.mysocket.testwebsocket.domain.user.repository;

import org.mysocket.testwebsocket.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
