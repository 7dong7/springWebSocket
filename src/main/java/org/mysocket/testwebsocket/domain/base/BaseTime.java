package org.mysocket.testwebsocket.domain.base;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTime {

    @CreatedDate
    @Column(updatable = false) // 수정 불가
    private LocalDateTime createAt; // 생성일

    @LastModifiedDate
    private LocalDateTime updateAt; // 수정일


}
