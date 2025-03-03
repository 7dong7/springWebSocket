package org.mysocket.testwebsocket.domain.chat.dto;

import lombok.Data;

@Data
public class ChatRoomForm {

    private String name; // 챗룸 이름
    private Boolean isGroup; // 그룹 유무

}
