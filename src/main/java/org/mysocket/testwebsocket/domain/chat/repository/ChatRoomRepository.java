package org.mysocket.testwebsocket.domain.chat.repository;

import org.mysocket.testwebsocket.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
}
